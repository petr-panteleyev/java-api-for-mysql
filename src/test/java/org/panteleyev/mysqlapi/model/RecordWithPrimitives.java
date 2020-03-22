package org.panteleyev.mysqlapi.model;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.Record;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.panteleyev.mysqlapi.annotations.Table;
import java.util.Objects;
import java.util.Random;

@Table("primitives_table")
public class RecordWithPrimitives implements Record<Integer> {
    @PrimaryKey
    @Column(Column.ID)
    private Integer id;

    @Column("a")
    private int a;
    @Column("b")
    private boolean b;
    @Column("c")
    private long c;

    public RecordWithPrimitives() {
        this(0, 0, false, 0l);
    }

    public RecordWithPrimitives(Integer id, int a, boolean b, long c) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public boolean getB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public long getC() {
        return c;
    }

    public void setC(long c) {
        this.c = c;
    }

    public static RecordWithPrimitives newRecord(Integer id, Random random) {
        return new RecordWithPrimitives(
                id,
                random.nextInt(),
                random.nextBoolean(),
                random.nextLong()
        );
    }

    public static RecordWithPrimitives newNullRecord(Integer id) {
        return new RecordWithPrimitives(
                id,
                0,
                false,
                0l
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof RecordWithPrimitives that) {
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
