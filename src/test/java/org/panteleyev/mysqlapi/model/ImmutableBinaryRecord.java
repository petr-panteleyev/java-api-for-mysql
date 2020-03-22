package org.panteleyev.mysqlapi.model;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.Record;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.panteleyev.mysqlapi.annotations.RecordBuilder;
import org.panteleyev.mysqlapi.annotations.Table;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

@Table("immutable_binary_table")
public class ImmutableBinaryRecord implements Record {
    @PrimaryKey
    @Column(Column.ID)
    private final Integer id;

    @Column(value = "a", length = 3000)
    private final byte[] a;

    @RecordBuilder
    public ImmutableBinaryRecord(@Column(Column.ID) Integer id,
                                 @Column("a") byte[] a) {
        this.id = id;
        this.a = a;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof ImmutableBinaryRecord that) {
            return Objects.equals(this.id, that.id)
                    && Arrays.equals(this.a, that.a);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, a);
    }

    public static ImmutableBinaryRecord newRecord(Integer id, Random random) {
        byte[] a = new byte[1000];
        random.nextBytes(a);

        return new ImmutableBinaryRecord(id, a);
    }

    public static ImmutableBinaryRecord newNullRecord(Integer id) {
        return new ImmutableBinaryRecord(id, null);
    }
}
