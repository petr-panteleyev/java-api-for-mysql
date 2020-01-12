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

        if (o instanceof RecordWithPrimitives) {
            RecordWithPrimitives that = (RecordWithPrimitives) o;

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
