/*
 * Copyright (c) 2017, 2019, Petr Panteleyev <petr@panteleyev.org>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.panteleyev.mysqlapi.model;

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
