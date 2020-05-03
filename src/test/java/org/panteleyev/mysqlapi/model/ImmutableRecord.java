package org.panteleyev.mysqlapi.model;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.TableRecord;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.panteleyev.mysqlapi.annotations.RecordBuilder;
import org.panteleyev.mysqlapi.annotations.Table;
import org.panteleyev.mysqlapi.Base;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Table("immutable_table")
public class ImmutableRecord implements TableRecord<Integer> {
    @PrimaryKey
    @Column(Column.ID)
    private final Integer id;

    // fields
    // Tests may assume annotations use the same names
    @Column("a")
    private final String a;
    @Column("b")
    private final Integer b;
    @Column("c")
    private final Boolean c;
    @Column("d")
    private final Date d;
    @Column("e")
    private final Long e;
    @Column("f")
    private final BigDecimal f;
    @Column("g")
    private final EnumType g;
    @Column("h")
    private final LocalDate h;

    private final Integer b2;

    @RecordBuilder
    public ImmutableRecord(@Column("id") Integer id,
                           @Column("a") String a,
                           @Column("b") Integer b,
                           @Column("c") Boolean c,
                           @Column("d") Date d,
                           @Column("e") Long e,
                           @Column("f") BigDecimal f,
                           @Column("g") EnumType g,
                           @Column("h") LocalDate h) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;

        b2 = b == null ? null : b + b;
    }

    public int getId() {
        return id;
    }

    public String getA() {
        return a;
    }

    public Integer getB() {
        return b;
    }

    public Boolean getC() {
        return c;
    }

    public Date getD() {
        return d;
    }

    public Long getE() {
        return e;
    }

    public BigDecimal getF() {
        return f;
    }

    public EnumType getG() {
        return g;
    }

    public LocalDate getH() {
        return h;
    }

    public Integer getB2() {
        return b2;
    }

    public static ImmutableRecord newRecord(Integer id, Random random) {
        return new ImmutableRecord(
                id,
                UUID.randomUUID().toString(),
                random.nextInt(),
                random.nextBoolean(),
                new Date(),
                random.nextLong(),
                new BigDecimal(random.nextDouble()).setScale(6, RoundingMode.HALF_UP),
                EnumType.F1,
                LocalDate.now()
        );
    }

    public static ImmutableRecord newNullRecord(Integer id) {
        return new ImmutableRecord(
                id,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ImmutableRecord that) {
            return Objects.equals(this.id, that.id)
                    && Objects.equals(this.a, that.a)
                    && Objects.equals(this.b, that.b)
                    && Objects.equals(this.c, that.c)
                    && Objects.equals(this.d, that.d)
                    && Objects.equals(this.e, that.e)
                    && Base.compareBigDecimals(this.f, that.f)
                    && Objects.equals(this.g, that.g)
                    && Objects.equals(this.h, that.h)
                    && Objects.equals(this.b2, that.b2);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c, d, e, f, g, h, b2);
    }

    @Override
    public String toString() {
        return "[ImmutableRecord: id=" + id
                + " a=" + a + " b=" + b + " c=" + c + " d=" + d
                + " e=" + e + " f=" + f + " g=" + g + " h=" + h
                + " b2=" + b2
                + "]";
    }
}
