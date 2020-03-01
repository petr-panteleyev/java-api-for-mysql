package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.ForeignKey;
import org.panteleyev.mysqlapi.annotations.Index;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.panteleyev.mysqlapi.annotations.RecordBuilder;
import org.panteleyev.mysqlapi.annotations.Table;
import javax.sql.DataSource;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import static org.panteleyev.mysqlapi.DataTypes.AUTO_INCREMENT_TYPES;
import static org.panteleyev.mysqlapi.DataTypes.CLASS_NOT_ANNOTATED;
import static org.panteleyev.mysqlapi.DataTypes.TYPE_ENUM;
import static org.panteleyev.mysqlapi.DataTypes.TYPE_INT;
import static org.panteleyev.mysqlapi.DataTypes.TYPE_INTEGER;
import static org.panteleyev.mysqlapi.DataTypes.TYPE_LONG;
import static org.panteleyev.mysqlapi.DataTypes.TYPE_LONG_PRIM;

/**
 * MySQL API entry point.
 */
@SuppressWarnings("rawtypes")
public class MySqlClient {
    static class ParameterHandle {
        final String name;
        final Class<?> type;

        ParameterHandle(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }
    }

    static class ConstructorHandle {
        final MethodHandle handle;
        final List<ParameterHandle> parameters;

        ConstructorHandle(MethodHandle handle, List<ParameterHandle> parameters) {
            this.handle = handle;
            this.parameters = parameters;
        }
    }

    static class PrimaryKeyHandle {
        private final Field field;
        private final VarHandle handle;
        private final boolean autoIncrement;

        PrimaryKeyHandle(Field field, VarHandle handle, boolean autoIncrement) {
            this.field = field;
            this.handle = handle;
            this.autoIncrement = autoIncrement;
        }

        public Field getField() {
            return field;
        }

        public VarHandle getHandle() {
            return handle;
        }

        public boolean isAutoIncrement() {
            return autoIncrement;
        }
    }

    private static final String NOT_ANNOTATED = "Class is not properly annotated";

    private final Map<Class<? extends Record>, Number> primaryKeys = new ConcurrentHashMap<>();

    private final Map<Class<? extends Record>, String> selectAllSql = new ConcurrentHashMap<>();
    private final Map<Class<? extends Record>, String> selectByIdSql = new ConcurrentHashMap<>();
    private final Map<Class<? extends Record>, String> insertSql = new ConcurrentHashMap<>();
    private final Map<Class<? extends Record>, String> updateSql = new ConcurrentHashMap<>();
    private final Map<Class<? extends Record>, String> deleteSql = new ConcurrentHashMap<>();

    private static final Map<Class<? extends Record>, PrimaryKeyHandle> PRIMARY_KEY_HANDLE_MAP
        = new ConcurrentHashMap<>();
    private static final Map<Class<? extends Record>, ConstructorHandle> CONSTRUCTOR_MAP = new ConcurrentHashMap<>();
    private static final Map<Class<? extends Record>, Map<String, VarHandle>> COLUMN_MAP = new ConcurrentHashMap<>();

    private DataSource datasource;

    private final MySqlProxy proxy = new MySqlProxy();

    /**
     * Creates MySQLClient object. Data source should be set later using {@link #setDataSource(DataSource)}
     * method.
     */
    public MySqlClient() {
    }

    /**
     * Creates MySQLClient object with predefined data source.
     *
     * @param ds data source
     */
    public MySqlClient(DataSource ds) {
        this.datasource = ds;
    }

    /**
     * Return current data source object.
     *
     * @return data source object
     */
    public DataSource getDataSource() {
        return datasource;
    }

    /**
     * Sets a new data source.
     *
     * @param ds data source
     */
    public void setDataSource(DataSource ds) {
        this.datasource = ds;
        primaryKeys.clear();
        insertSql.clear();
        deleteSql.clear();
    }

    /**
     * Returns connection for the current data source.
     *
     * @return connection
     * @throws SQLException in case of SQL error
     */
    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    /**
     * Retrieves record from the database using record primary key.
     *
     * @param <K>   primary key type
     * @param <T>   type of the record
     * @param id    record id
     * @param clazz record class
     * @return record
     */
    public <K, T extends Record<K>> Optional<T> get(K id, Class<? extends T> clazz) {
        try (var conn = getDataSource().getConnection()) {
            if (!clazz.isAnnotationPresent(Table.class)) {
                throw new IllegalStateException(NOT_ANNOTATED);
            }

            var ps = conn.prepareStatement(getSelectByIdSql(clazz));

            var primaryKey = findPrimaryKey(clazz);
            setColumnToPreparedStatement(ps, 1, primaryKey.field, id);

            try (var set = ps.executeQuery()) {
                return (set.next()) ? Optional.of(fromSQL(set, clazz)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Retrieves all records of the specified type.
     *
     * @param <T>   type of the record
     * @param conn  connection
     * @param clazz record class
     * @return list of records
     */
    public <T extends Record> List<T> getAll(Connection conn, Class<T> clazz) {
        var result = new ArrayList<T>();

        try {
            if (!clazz.isAnnotationPresent(Table.class)) {
                throw new IllegalStateException(NOT_ANNOTATED);
            }

            var ps = conn.prepareStatement(getSelectAllSql(clazz));
            try (var set = ps.executeQuery()) {
                while (set.next()) {
                    result.add(fromSQL(set, clazz));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }

    /**
     * Retrieves all records of the specified type.
     *
     * @param <T>   type of the record
     * @param clazz record class
     * @return list of records
     */
    public <T extends Record> List<T> getAll(Class<T> clazz) {
        try (var conn = getDataSource().getConnection()) {
            return getAll(conn, clazz);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Retrieves all records of the specified type and fills the map.
     *
     * @param <K>    type of the primary key
     * @param <T>    type of the record
     * @param conn   connection
     * @param clazz  record class
     * @param result map to fill
     */
    public <K, T extends Record<K>> void getAll(Connection conn, Class<T> clazz, Map<K, T> result) {
        try {
            if (!clazz.isAnnotationPresent(Table.class)) {
                throw new IllegalStateException(NOT_ANNOTATED);
            }

            var ps = conn.prepareStatement(getSelectAllSql(clazz));
            try (var set = ps.executeQuery()) {
                while (set.next()) {
                    T r = fromSQL(set, clazz);
                    result.put(r.getPrimaryKey(), r);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Retrieves all records of the specified type and fills the map.
     *
     * @param <K>    type of the primary key
     * @param <T>    type of the record
     * @param clazz  record class
     * @param result map to fill
     */
    public <K, T extends Record<K>> void getAll(Class<T> clazz, Map<K, T> result) {
        try (var conn = getDataSource().getConnection()) {
            getAll(conn, clazz, result);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    static Map<String, VarHandle> computeColumns(Class<? extends Record> clazz) {
        try {
            var lookup = MethodHandles.privateLookupIn(clazz, MethodHandles.lookup());

            var result = new HashMap<String, VarHandle>();
            for (var field : clazz.getDeclaredFields()) {
                var column = field.getAnnotation(Column.class);
                if (column != null) {
                    var handle = lookup.unreflectVarHandle(field);
                    result.put(column.value(), handle);
                }
            }
            return result;
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }


    private void fromSQL(ResultSet set, Record record, Map<String, VarHandle> columns) throws SQLException {
        for (var entry : columns.entrySet()) {
            var handle = entry.getValue();
            var value = proxy.getFieldValue(entry.getKey(), handle.varType(), set);

            switch (handle.varType().getName()) {
                case "int":
                    handle.set(record, value == null ? 0 : (int) value);
                    break;
                case "long":
                    handle.set(record, value == null ? 0L : (long) value);
                    break;
                case "boolean":
                    handle.set(record, value != null && (boolean) value);
                    break;
                default:
                    handle.set(record, value);
                    break;
            }
        }
    }

    private <T extends Record> T fromSQL(ResultSet set, ConstructorHandle builder) {
        try {
            var params = new ArrayList<>(builder.parameters.size());
            for (ParameterHandle ph : builder.parameters) {
                params.add(proxy.getFieldValue(ph.name, ph.type, set));
            }

            //noinspection unchecked
            return (T) builder.handle.invokeWithArguments(params);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    <T extends Record> T fromSQL(ResultSet set, Class<T> clazz) {
        var builder = CONSTRUCTOR_MAP.computeIfAbsent(clazz, MySqlClient::cacheConstructorHandle);

        try {
            if (builder != null) {
                return fromSQL(set, builder);
            } else {
                var columns = COLUMN_MAP.computeIfAbsent(clazz, MySqlClient::computeColumns);
                if (columns.isEmpty()) {
                    throw new IllegalStateException("Class " + clazz.getName() + " has no column annotations");
                }

                T result = clazz.getDeclaredConstructor().newInstance();
                fromSQL(set, result, columns);
                return result;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method creates table for the specified classes according to their annotations.
     *
     * @param tables list of tables
     */
    public void createTables(List<Class<? extends Record>> tables) {
        if (getDataSource() == null) {
            throw new IllegalStateException("Database not opened");
        }

        try (var conn = getDataSource().getConnection()) {
            createTables(conn, tables);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method creates table for the specified classes according to their annotations.
     *
     * @param conn   connection
     * @param tables list of tables
     */
    public void createTables(Connection conn, List<Class<? extends Record>> tables) {
        try (var st = conn.createStatement()) {
            // Step 1: drop tables in reverse order
            for (int index = tables.size() - 1; index >= 0; index--) {
                var cl = tables.get(index);
                if (!cl.isAnnotationPresent(Table.class)) {
                    throw new IllegalStateException(NOT_ANNOTATED);
                }

                var table = cl.getAnnotation(Table.class);
                st.executeUpdate("DROP TABLE IF EXISTS " + table.value());
            }

            // Step 2: create new tables in natural order
            for (Class<?> cl : tables) {
                var table = cl.getAnnotation(Table.class);

                try {
                    var b = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                        .append(table.value())
                        .append(" (");

                    var constraints = new ArrayList<String>();
                    var indexed = new HashSet<Field>();

                    boolean first = true;
                    for (var field : cl.getDeclaredFields()) {
                        if (field.isAnnotationPresent(Column.class)) {
                            var column = field.getAnnotation(Column.class);
                            var fName = column.value();

                            var getterType = field.getType();
                            var typeName = getterType.isEnum() ?
                                TYPE_ENUM : getterType.getTypeName();

                            if (!first) {
                                b.append(",");
                            }
                            first = false;

                            b.append(fName).append(" ")
                                .append(proxy.getColumnString(column, field.getAnnotation(PrimaryKey.class),
                                    field.getAnnotation(ForeignKey.class), typeName, constraints));

                            if (field.isAnnotationPresent(Index.class)) {
                                indexed.add(field);
                            }
                        }
                    }

                    if (!constraints.isEmpty()) {
                        b.append(",");
                        b.append(String.join(",", constraints));
                    }

                    b.append(")");

                    st.executeUpdate(b.toString());

                    // Create indexes
                    for (var field : indexed) {
                        st.executeUpdate(proxy.buildIndex(table, field));
                    }
                } catch (SecurityException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    String getSelectAllSql(Class<? extends Record> recordClass) {
        return selectAllSql.computeIfAbsent(recordClass, clazz -> {
            var table = clazz.getAnnotation(Table.class);
            if (table == null) {
                throw new IllegalStateException(CLASS_NOT_ANNOTATED + clazz.getName());
            }

            var columnString = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(proxy::getSelectColumnString)
                .collect(Collectors.joining(","));

            if (columnString.isEmpty()) {
                throw new IllegalStateException("No fields");
            }

            return "SELECT " + columnString + " FROM " + table.value();
        });
    }

    String getSelectByIdSql(Class<? extends Record> recordClass) {
        return selectByIdSql.computeIfAbsent(recordClass, clazz ->
            getSelectAllSql(clazz) +
                " WHERE " +
                proxy.getWhereColumnString(findPrimaryKey(clazz).field) + "=?");
    }

    private String getInsertSQL(Record record) {
        return insertSql.computeIfAbsent(record.getClass(), clazz -> {
            var b = new StringBuilder("INSERT INTO ");

            var table = clazz.getAnnotation(Table.class);
            if (table == null) {
                throw new IllegalStateException("Class " + clazz.getName() + " is not properly annotated");
            }

            b.append(table.value()).append(" (");

            int fCount = 0;

            var valueString = new StringBuilder();
            try {
                for (var field : clazz.getDeclaredFields()) {
                    var column = field.getAnnotation(Column.class);
                    if (column != null) {
                        if (fCount != 0) {
                            b.append(",");
                            valueString.append(",");
                        }
                        b.append(column.value());
                        valueString.append(proxy.getInsertColumnPattern(field));
                        fCount++;
                    }
                }
            } catch (SecurityException ex) {
                throw new RuntimeException(ex);
            }

            if (fCount == 0) {
                throw new IllegalStateException("No fields");
            }

            b.append(") VALUES (")
                .append(valueString)
                .append(")");

            return b.toString();
        });
    }

    private String getUpdateSQL(Record record) {
        return updateSql.computeIfAbsent(record.getClass(), clazz -> {
            var b = new StringBuilder("update ");

            var table = clazz.getAnnotation(Table.class);
            if (table == null) {
                throw new IllegalStateException(NOT_ANNOTATED);
            }

            b.append(table.value()).append(" set ");

            int fCount = 0;

            try {
                for (var field : record.getClass().getDeclaredFields()) {
                    var column = field.getAnnotation(Column.class);
                    if (column != null && field.getAnnotation(PrimaryKey.class) == null) {
                        if (fCount != 0) {
                            b.append(", ");
                        }
                        b.append(column.value())
                            .append("=")
                            .append(proxy.getUpdateColumnPattern(field));
                        fCount++;
                    }
                }
            } catch (SecurityException ex) {
                throw new RuntimeException(ex);
            }

            if (fCount == 0) {
                throw new IllegalStateException("No fields");
            }

            var primaryKeyField = findPrimaryKey(clazz);
            b.append(" WHERE ")
                .append(proxy.getWhereColumnString(primaryKeyField.field))
                .append("=?");

            return b.toString();
        });
    }

    String getDeleteSQL(Class<? extends Record> clazz) {
        return deleteSql.computeIfAbsent(clazz, cl -> {
            var b = new StringBuilder("DELETE FROM ");
            var table = cl.getAnnotation(Table.class);
            if (table == null) {
                throw new IllegalStateException(NOT_ANNOTATED);
            }
            b.append(table.value());

            var primaryKeyField = findPrimaryKey(clazz);
            b.append(" WHERE ")
                .append(proxy.getWhereColumnString(primaryKeyField.field))
                .append("=?");

            return b.toString();
        });
    }

    private String getDeleteSQL(Record record) {
        return getDeleteSQL(record.getClass());
    }

    private void setColumnToPreparedStatement(Record record, PreparedStatement st, int index, Field field,
                                              VarHandle handle) throws SQLException
    {
        var fieldType = field.getType();

        Object value = handle.get(record);
        var typeName = fieldType.isEnum() ? TYPE_ENUM : fieldType.getTypeName();
        proxy.setFieldData(st, index, value, typeName);
    }

    private void setColumnToPreparedStatement(PreparedStatement st, int index, Field field,
                                              Object value) throws SQLException
    {
        var fieldType = field.getType();
        var typeName = fieldType.isEnum() ? TYPE_ENUM : fieldType.getTypeName();
        proxy.setFieldData(st, index, value, typeName);
    }

    private void setData(Record record, PreparedStatement st, boolean update) {
        try {
            int index = 1;

            var columns = COLUMN_MAP.computeIfAbsent(record.getClass(), MySqlClient::computeColumns);

            for (var field : record.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    // if update skip ID at this point
                    var fld = field.getAnnotation(Column.class);
                    if (update && field.getAnnotation(PrimaryKey.class) != null) {
                        continue;
                    }

                    var handle = columns.get(fld.value());
                    setColumnToPreparedStatement(record, st, index++, field, handle);
                }
            }

            if (update) {
                var primaryKey = findPrimaryKey(record.getClass());
                setColumnToPreparedStatement(record, st, index, primaryKey.field, primaryKey.handle);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private PreparedStatement getPreparedStatement(Record record, Connection conn, boolean update) throws SQLException {
        String sql = (update) ? getUpdateSQL(record) : getInsertSQL(record);
        PreparedStatement st = conn.prepareStatement(sql);
        setData(record, st, update);
        return st;
    }

    private PreparedStatement getDeleteStatement(Record record, Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement(getDeleteSQL(record));

        var primaryKey = findPrimaryKey(record.getClass());
        setColumnToPreparedStatement(record, st, 1, primaryKey.field, primaryKey.handle);
        return st;
    }

    private <K> PreparedStatement getDeleteStatement(K id, Class<? extends Record<K>> clazz, Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement(getDeleteSQL(clazz));
        var primaryKey = findPrimaryKey(clazz);
        setColumnToPreparedStatement(st, 1, primaryKey.field, id);
        return st;
    }

    /**
     * Pre-loads necessary information from the just opened database. This method must be called prior to any other
     * database operations. Otherwise primary keys may be generated incorrectly.
     *
     * @param tables list of {@link Record} types
     */
    public void preload(Collection<Class<? extends Record>> tables) {
        for (var clazz : tables) {
            var table = clazz.getAnnotation(Table.class);
            if (table == null) {
                throw new IllegalStateException(CLASS_NOT_ANNOTATED + clazz.getTypeName());
            }

            var primaryKey = findPrimaryKey(clazz);
            if (!primaryKey.isAutoIncrement()) {
                continue;
            }

            var fieldTypeName = primaryKey.field.getType().getTypeName();
            if (!AUTO_INCREMENT_TYPES.contains(fieldTypeName)) {
                continue;
            }

            var pattern = proxy.getSelectColumnString(primaryKey.field);

            Number maxValue = 0;

            try (var conn = getDataSource().getConnection()) {
                var st = conn.prepareStatement("SELECT MAX(" + pattern + ") FROM " + table.value());
                try (var rs = st.executeQuery()) {
                    if (rs.next()) {
                        switch (fieldTypeName) {
                            case TYPE_INT:
                            case TYPE_INTEGER:
                                maxValue = rs.getInt(1);
                                break;
                            case TYPE_LONG:
                            case TYPE_LONG_PRIM:
                                maxValue = rs.getLong(1);
                                break;
                        }
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            primaryKeys.put(clazz, maxValue);
        }
    }

    /**
     * Returns next available primary key value. This method is thread safe.
     * Only numeric types (int, long, {@link Integer}, {@link Long}) are currently supported.
     *
     * @param <K>   primary key type
     * @param clazz record class
     * @return primary key value
     */
    public <K extends Number> K generatePrimaryKey(Class<? extends Record<K>> clazz) {
        var primaryKey = findPrimaryKey(clazz);
        if (!primaryKey.isAutoIncrement()) {
            throw new IllegalStateException("Primary key for class " + clazz + " is not set to auto increment");
        }

        return (K) primaryKeys.compute(clazz, (k, v) -> {
            if (v instanceof Integer) {
                return (Integer) v + 1;
            } else if (v instanceof Long) {
                return (Long) v + 1;
            } else {
                return 1;
            }
        });
    }

    /**
     * This method inserts new record with predefined id into the database. No attempt to generate
     * new id is made. Calling code must ensure that predefined id is unique.
     *
     * @param record record
     * @throws IllegalArgumentException if id of the record is 0
     */
    public void insert(Record record) {
        try (var conn = getDataSource().getConnection()) {
            insert(conn, record);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method inserts new record with predefined id into the database. No attempt to generate
     * new id is made. Calling code must ensure that predefined id is unique.
     *
     * @param conn   SQL connection
     * @param record record
     * @throws IllegalArgumentException if id of the record is 0
     */
    public void insert(Connection conn, Record record) {
        try (var st = getPreparedStatement(record, conn, false)) {
            st.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * <p>This method inserts multiple records with predefined id using batch insert. No attempt to generate
     * new id is made. Calling code must ensure that predefined id is unique for all records.</p>
     * <p>Supplied records are divided to batches of the specified size. To avoid memory issues size of the batch
     * must be tuned appropriately.</p>
     *
     * @param size    size of the batch
     * @param records list of records
     * @param <T>     type of records
     */
    public <T extends Record> void insert(int size, List<T> records) {
        try (var conn = getConnection()) {
            insert(conn, size, records);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * <p>This method inserts multiple records with predefined id using batch insert. No attempt to generate
     * new id is made. Calling code must ensure that predefined id is unique for all records.</p>
     * <p>Supplied records are divided to batches of the specified size. To avoid memory issues size of the batch
     * must be tuned appropriately.</p>
     *
     * @param conn    SQL connection
     * @param size    size of the batch
     * @param records list of records
     * @param <T>     type of records
     */
    public <T extends Record> void insert(Connection conn, int size, List<T> records) {
        if (size < 1) {
            throw new IllegalArgumentException("Batch size must be >= 1");
        }

        if (!records.isEmpty()) {
            var sql = getInsertSQL(records.get(0));

            try (var st = conn.prepareStatement(sql)) {
                int count = 0;

                for (T r : records) {
                    setData(r, st, false);
                    st.addBatch();

                    if (++count % size == 0) {
                        st.executeBatch();
                    }
                }

                st.executeBatch();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Updates record in the database. This method returns instance of the {@link Record}, i.e. supplied object is
     * not changed.
     *
     * @param record record
     */
    public void update(Record record) {
        try (var conn = getDataSource().getConnection()) {
            update(conn, record);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Updates record in the database. This method returns instance of the {@link Record}, i.e. supplied object is
     * not changed.
     *
     * @param conn   SQL connection
     * @param record record
     */
    public void update(Connection conn, Record record) {
        try (var ps = getPreparedStatement(record, conn, true)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * Deleted record from the database.
     *
     * @param record record to delete
     */
    public void delete(Record record) {
        try (var conn = getDataSource().getConnection(); var ps = getDeleteStatement(record, conn)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Deletes record from the database.
     *
     * @param <K>   primary key type
     * @param id    id of the record
     * @param clazz record type
     */
    public <K> void delete(K id, Class<? extends Record<K>> clazz) {
        try (var conn = getDataSource().getConnection(); var ps = getDeleteStatement(id, clazz, conn)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Deletes all records from table.
     *
     * @param table table
     */
    public void deleteAll(Class<? extends Record> table) {
        try (var connection = getDataSource().getConnection()) {
            deleteAll(connection, table);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Deletes all records from table using provided connection.
     *
     * @param connection SQL connection
     * @param table      table class
     */
    public void deleteAll(Connection connection, Class<? extends Record> table) {
        proxy.deleteAll(connection, table);
    }

    /**
     * Truncates tables removing all records. Primary key generation starts from 1 again. For MySQL this operation
     * uses <code>TRUNCATE TABLE table_name</code> command. As SQLite does not support this command <code>DELETE FROM
     * table_name</code> is used instead.
     *
     * @param tables tables to truncate
     */
    public void truncate(List<Class<? extends Record>> tables) {
        try (var connection = getDataSource().getConnection()) {
            truncate(connection, tables);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Truncates tables removing all records. Primary key generation starts from 1 again. For MySQL this operation
     * uses <code>TRUNCATE TABLE table_name</code> command. As SQLite does not support this command <code>DELETE FROM
     * table_name</code> is used instead.
     *
     * @param conn   connection
     * @param tables tables to truncate
     */
    public void truncate(Connection conn, List<Class<? extends Record>> tables) {
        proxy.truncate(conn, tables);
        for (Class<? extends Record> t : tables) {
            primaryKeys.put(t, 0);
        }
    }

    /**
     * Resets primary key generation for the given table. Next call to {@link MySqlClient#generatePrimaryKey(Class)}
     * will return 1. This method should only be used in case of manual table truncate.
     *
     * @param table table class
     */
    protected void resetPrimaryKey(Class<? extends Record> table) {
        primaryKeys.put(table, 0);
    }

    /**
     * Drops specified tables according to their annotations.
     *
     * @param tables table classes
     */
    public void dropTables(List<Class<? extends Record>> tables) {
        try (var conn = getDataSource().getConnection()) {
            dropTables(conn, tables);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Drops specified tables according to their annotations.
     *
     * @param conn   connection
     * @param tables table classes
     */
    public void dropTables(Connection conn, List<Class<? extends Record>> tables) {
        try (var st = conn.createStatement()) {
            for (Class<? extends Record> t : tables) {
                st.execute("DROP TABLE " + Record.getTableName(t));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    static ConstructorHandle cacheConstructorHandle(Class<?> clazz) {
        Constructor<?> constructor = null;

        for (var c : clazz.getConstructors()) {
            if (c.isAnnotationPresent(RecordBuilder.class)) {
                constructor = c;
                break;
            }
        }

        if (constructor == null) {
            return null;
        }

        var paramAnnotations = constructor.getParameterAnnotations();
        var paramTypes = constructor.getParameterTypes();

        var parameterHandles = new ArrayList<ParameterHandle>();

        for (int i = 0; i < constructor.getParameterCount(); i++) {
            var fieldName = Arrays.stream(paramAnnotations[i])
                .filter(a -> a instanceof Column)
                .findAny()
                .map(a -> ((Column) a).value())
                .orElseThrow(RuntimeException::new);
            parameterHandles.add(new ParameterHandle(fieldName, paramTypes[i]));
        }

        if (parameterHandles.isEmpty()) {
            throw new IllegalArgumentException("Constructor builder must have parameters");
        }

        var lookup = MethodHandles.publicLookup();
        try {
            var handle = lookup.unreflectConstructor(constructor);
            return new ConstructorHandle(handle, parameterHandles);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method returns field that represents primary key.
     *
     * @param recordClass table class
     * @return primary key field
     * @throws IllegalStateException if there is no primary key
     */
    static PrimaryKeyHandle findPrimaryKey(Class<? extends Record> recordClass) {
        return PRIMARY_KEY_HANDLE_MAP.computeIfAbsent(recordClass, clazz -> {
            for (var field : clazz.getDeclaredFields()) {
                var column = field.getAnnotation(Column.class);
                if (column == null) {
                    continue;
                }
                var primaryKey = field.getAnnotation(PrimaryKey.class);
                if (primaryKey == null) {
                    continue;
                }

                var columns = COLUMN_MAP.computeIfAbsent(clazz, MySqlClient::computeColumns);
                return new PrimaryKeyHandle(field, columns.get(column.value()), primaryKey.isAutoIncrement());
            }

            throw new IllegalStateException("No primary key defined for " + clazz.getTypeName());
        });
    }

    static <K> K getPrimaryKey(Record<K> record) {
        var primaryKey = findPrimaryKey(record.getClass());
        //noinspection unchecked
        return (K) primaryKey.handle.get(record);
    }

    /**
     * This method updates module <code>org.panteleyev.mysqlapi</code> to read given modules.
     * <p>
     * Calling this method replaces command line option <code>--add-reads
     * org.panteleyev.mysqlapi=&lt;module&gt;</code>.
     *
     * @param modules collection of client modules
     */
    public static void addReads(Collection<Module> modules) {
        var thisModule = MySqlClient.class.getModule();
        modules.forEach(thisModule::addReads);
    }
}
