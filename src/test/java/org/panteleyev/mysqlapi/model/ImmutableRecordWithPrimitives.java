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
import java.util.Objects;
import java.util.Random;

@Table("immutable_primitives_table")
public class ImmutableRecordWithPrimitives implements Record {
    @PrimaryKey
    @Column(Column.ID)
    private Integer id;

    @Column("a")
    private final int a;
    @Column("b")
    private final boolean b;
    @Column("c")
    private final long c;

    @RecordBuilder
    public ImmutableRecordWithPrimitives(@Column("id") Integer id,
                                         @Column("a") int a,
                                         @Column("b") boolean b,
                                         @Column("c") long c) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public int getId() {
        return id;
    }

    public int getA() {
        return a;
    }

    public boolean getB() {
        return b;
    }

    public long getC() {
        return c;
    }

    public static ImmutableRecordWithPrimitives newRecord(Integer id, Random random) {
        return new ImmutableRecordWithPrimitives(
                id,
                random.nextInt(),
                random.nextBoolean(),
                random.nextLong()
        );
    }

    public static ImmutableRecordWithPrimitives newNullRecord(Integer id) {
        return new ImmutableRecordWithPrimitives(
                id,
                0,
                false,
                0L
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof ImmutableRecordWithPrimitives) {
            ImmutableRecordWithPrimitives that = (ImmutableRecordWithPrimitives) o;

            return Objects.equals(this.id, that.id)
                    && Objects.equals(this.a, that.a)
                    && Objects.equals(this.b, that.b)
                    && Objects.equals(this.c, that.c);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }
}
