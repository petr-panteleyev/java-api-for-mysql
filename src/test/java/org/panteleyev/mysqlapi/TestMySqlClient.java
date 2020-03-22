package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.answers.ResultSetBigDecimalAnswer;
import org.panteleyev.mysqlapi.answers.ResultSetBooleanAnswer;
import org.panteleyev.mysqlapi.answers.ResultSetIntAnswer;
import org.panteleyev.mysqlapi.answers.ResultSetLongAnswer;
import org.panteleyev.mysqlapi.answers.ResultSetObjectAnswer;
import org.panteleyev.mysqlapi.model.EnumType;
import org.panteleyev.mysqlapi.model.ImmutableRecord;
import org.panteleyev.mysqlapi.model.RecordWithAllTypes;
import org.panteleyev.mysqlapi.model.RecordWithJson;
import org.panteleyev.mysqlapi.model.RecordWithOptionals;
import org.panteleyev.mysqlapi.model.RecordWithUuid;
import org.panteleyev.mysqlapi.model.UuidBinaryPrimaryKeyRecord;
import org.panteleyev.mysqlapi.model.UuidPrimaryKeyRecord;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

@Test
public class TestMySqlClient {
    private static final int NUMBER_OF_RECORDS = 100;
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private static final List<MySqlClient.ParameterHandle> EXPECTED_PARAMS = List.of(
        new MySqlClient.ParameterHandle("id", Integer.class),
        new MySqlClient.ParameterHandle("a", String.class),
        new MySqlClient.ParameterHandle("b", Integer.class),
        new MySqlClient.ParameterHandle("c", Boolean.class),
        new MySqlClient.ParameterHandle("d", Date.class),
        new MySqlClient.ParameterHandle("e", Long.class),
        new MySqlClient.ParameterHandle("f", BigDecimal.class),
        new MySqlClient.ParameterHandle("g", EnumType.class),
        new MySqlClient.ParameterHandle("h", LocalDate.class)
    );

    @Test
    public void testFromSQLImmutable() throws Exception {
        var records = new ImmutableRecord[NUMBER_OF_RECORDS];
        var resultSets = new ResultSet[NUMBER_OF_RECORDS];

        for (int id = 1; id <= NUMBER_OF_RECORDS; id++) {
            var record = ImmutableRecord.newRecord(id, RANDOM);

            records[id - 1] = record;

            var rs = mock(ResultSet.class);
            when(rs.getObject(anyString())).then(new ResultSetObjectAnswer(record));
            when(rs.getLong(anyString())).then(new ResultSetLongAnswer(record));
            when(rs.getInt(anyString())).then(new ResultSetIntAnswer(record));
            when(rs.getBoolean(anyString())).then(new ResultSetBooleanAnswer(record));
            when(rs.getBigDecimal(anyString())).then(new ResultSetBigDecimalAnswer(record));

            resultSets[id - 1] = rs;
        }

        var loadedRecords = new ImmutableRecord[NUMBER_OF_RECORDS];

        var dao = new MySqlClient();

        for (int i = 0; i < NUMBER_OF_RECORDS; i++) {
            loadedRecords[i] = dao.fromSQL(resultSets[i], ImmutableRecord.class);
        }

        assertEquals(loadedRecords, records);
    }

    @Test
    public void testCacheConstructorHandle() {
        MySqlClient.ConstructorHandle constructorHandle = MySqlClient.cacheConstructorHandle(ImmutableRecord.class);

        Assert.assertNotNull(constructorHandle);
        Assert.assertNotNull(constructorHandle.handle());

        var parameters = constructorHandle.parameters();

        assertEquals(parameters.size(), EXPECTED_PARAMS.size());

        for (int i = 0; i < EXPECTED_PARAMS.size(); i++) {
            var p = parameters.get(i);
            assertEquals(p.name(), EXPECTED_PARAMS.get(i).name());
            assertEquals(p.type(), EXPECTED_PARAMS.get(i).type());
        }
    }

    @Test
    public void testCacheConstructorHandleNegative() {
        Assert.assertNull(MySqlClient.cacheConstructorHandle(RecordWithAllTypes.class));
    }

    @Test
    public void testComputeColumns() {
        var actual = MySqlClient.computeColumns(ImmutableRecord.class);

        for (var key : EXPECTED_PARAMS) {
            var handle = actual.get(key.name());
            Assert.assertNotNull(handle);
        }
    }

    @DataProvider(name = "testGetSelectAllSqlDataProvider")
    public Object[][] testGetSelectAllSqlDataProvider() {
        return new Object[][]{
            // MySQL
            {RecordWithAllTypes.class,
                "SELECT primary_key,a_field,b_field,c_field,d_field,e_field,f_field,g_field,h_field FROM " +
                    "all_types_table"},
            {ImmutableRecord.class,
                "SELECT id,a,b,c,d,e,f,g,h FROM immutable_table"},
            {RecordWithJson.class,
                "SELECT id,json FROM table_with_json"},
            {RecordWithUuid.class,
                "SELECT id,uuid,BIN_TO_UUID(uuidBinary) AS uuidBinary FROM table_with_uuid"},
            {RecordWithOptionals.class,
                "SELECT id,a,b,c,d,e,f,g,h FROM optionals_table"},
        };
    }


    @Test(dataProvider = "testGetSelectAllSqlDataProvider")
    public void testGetSelectAllSql(Class<? extends Record> clazz, String expected) {
        var dao = new MySqlClient();
        var sql = dao.getSelectAllSql(clazz);
        var sql2 = dao.getSelectAllSql(clazz);

        assertSame(sql, sql2);
        assertEquals(sql, expected);
    }

    @DataProvider(name = "testGetSelectByIdSqlDataProvider")
    public Object[][] testGetSelectByIdSqlDataProvider() {
        return new Object[][]{
            {RecordWithAllTypes.class,
                "SELECT primary_key,a_field,b_field,c_field,d_field,e_field,f_field,g_field,h_field FROM " +
                    "all_types_table WHERE primary_key=?"},
            {ImmutableRecord.class,
                "SELECT id,a,b,c,d,e,f,g,h FROM immutable_table WHERE id=?"},
            {RecordWithJson.class,
                "SELECT id,json FROM table_with_json WHERE id=?"},
            {RecordWithUuid.class,
                "SELECT id,uuid,BIN_TO_UUID(uuidBinary) AS uuidBinary FROM table_with_uuid WHERE id=?"},
            {UuidBinaryPrimaryKeyRecord.class,
                "SELECT BIN_TO_UUID(prim_key) AS prim_key,value FROM uuid_binary_primary_key WHERE BIN_TO_UUID" +
                    "(prim_key)=?"},
            {UuidPrimaryKeyRecord.class,
                "SELECT prim_key,value FROM uuid_primary_key WHERE prim_key=?"},
        };
    }

    @Test(dataProvider = "testGetSelectByIdSqlDataProvider")
    public void testGetSelectByIdSql(Class<? extends Record> clazz, String expected) {
        var dao = new MySqlClient();
        var sql = dao.getSelectByIdSql(clazz);
        var sql2 = dao.getSelectByIdSql(clazz);

        assertSame(sql, sql2);
        assertEquals(sql, expected);
    }

    @DataProvider(name = "testGetDeleteSqlDataProvider")
    public Object[][] testGetDeleteSqlDataProvider() {
        return new Object[][]{
            {RecordWithAllTypes.class,
                "DELETE FROM all_types_table WHERE primary_key=?"},
            {ImmutableRecord.class,
                "DELETE FROM immutable_table WHERE id=?"},
            {RecordWithJson.class,
                "DELETE FROM table_with_json WHERE id=?"},
            {RecordWithUuid.class,
                "DELETE FROM table_with_uuid WHERE id=?"},
        };
    }

    @Test(dataProvider = "testGetDeleteSqlDataProvider")
    public void testGetDeleteSql(Class<? extends Record> clazz, String expected) {
        var dao = new MySqlClient();
        var sql = dao.getDeleteSQL(clazz);
        var sql2 = dao.getDeleteSQL(clazz);

        assertSame(sql, sql2);
        assertEquals(sql, expected);
    }
}