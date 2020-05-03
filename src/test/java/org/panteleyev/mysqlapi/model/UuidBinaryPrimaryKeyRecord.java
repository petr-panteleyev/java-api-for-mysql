package org.panteleyev.mysqlapi.model;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.TableRecord;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.panteleyev.mysqlapi.annotations.Table;
import java.util.Objects;
import java.util.UUID;

@Table("uuid_binary_primary_key")
public class UuidBinaryPrimaryKeyRecord implements TableRecord<UUID> {
    @PrimaryKey
    @Column(value = "prim_key", storeUuidAsBinary = true)
    private UUID primKey;

    @Column("value")
    private String value;

    public UuidBinaryPrimaryKeyRecord() {
    }

    public UuidBinaryPrimaryKeyRecord(UUID primKey, String value) {
        this.primKey = primKey;
        this.value = value;
    }

    public UUID getPrimKey() {
        return primKey;
    }

    public void setPrimKey(UUID primKey) {
        this.primKey = primKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var that = (UuidBinaryPrimaryKeyRecord) o;
        return Objects.equals(primKey, that.primKey) &&
            Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primKey, value);
    }
}
