package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.annotations.Table;

/**
 * Database record.
 * @param <K> type of the primary key
 */
public interface TableRecord<K> {
    /**
     * Returns table name. Class must be annotated by {@link Table}.
     *
     * @return table name
     * @throws IllegalStateException if class is not annotated by {@link Table}.
     */
    default String getTableName() {
        return getTableName(getClass());
    }

    /**
     * Returns table name for table class. Class must be annotated by {@link Table}.
     *
     * @param table table class
     * @return table name
     * @throws IllegalStateException if class is not annotated by {@link Table}.
     */
    static String getTableName(Class<? extends TableRecord> table) {
        var annotation = table.getAnnotation(Table.class);
        if (annotation != null) {
            return annotation.value();
        } else {
            throw new IllegalStateException("Class " + table.getName() + "is not properly annotated");
        }
    }

    /**
     * Returns primary key value.
     * @return primary key value
     */
    default K getPrimaryKey() {
        return MySqlClient.getPrimaryKey(this);
    }
}
