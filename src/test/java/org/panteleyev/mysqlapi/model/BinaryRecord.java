package org.panteleyev.mysqlapi.model;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.Record;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.panteleyev.mysqlapi.annotations.Table;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

@Table("binary_table")
public class BinaryRecord implements Record<Integer> {
    @PrimaryKey
    @Column("primary_key")
    private Integer primKey;

    @Column(value = "a_field", length = 3000)
    private byte[] aField;

    public BinaryRecord() {
    }

    private BinaryRecord(int primKey, byte[] aField) {
        this.primKey = primKey;
        this.aField = aField;
    }

    public int getPrimKey() {
        return primKey;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof BinaryRecord that) {
            return Objects.equals(this.primKey, that.primKey)
                    && Arrays.equals(this.aField, that.aField);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(primKey, aField);
    }

    public static BinaryRecord newRecord(Integer id, Random random) {
        byte[] a = new byte[1000];
        random.nextBytes(a);

        return new BinaryRecord(id, a);
    }

    public static BinaryRecord newNullRecord(Integer id) {
        return new BinaryRecord(id, null);
    }
}
