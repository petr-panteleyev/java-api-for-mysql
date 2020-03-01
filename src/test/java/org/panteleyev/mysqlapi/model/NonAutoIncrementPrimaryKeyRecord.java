package org.panteleyev.mysqlapi.model;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.Record;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.panteleyev.mysqlapi.annotations.Table;

@Table("integer_primary_key")
public class NonAutoIncrementPrimaryKeyRecord implements Record<Integer> {
    @PrimaryKey(isAutoIncrement = false)
    @Column("prim_key")
    private int primKey;

    @Column("value")
    private String value;

    public NonAutoIncrementPrimaryKeyRecord() {
    }

    public NonAutoIncrementPrimaryKeyRecord(int primKey, String value) {
        this.primKey = primKey;
        this.value = value;
    }

    public int getPrimKey() {
        return primKey;
    }

    public void setPrimKey(int primKey) {
        this.primKey = primKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
