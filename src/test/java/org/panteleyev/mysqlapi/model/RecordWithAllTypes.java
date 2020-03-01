package org.panteleyev.mysqlapi.model;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.Record;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.panteleyev.mysqlapi.annotations.Table;
import org.panteleyev.mysqlapi.Base;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Table("all_types_table")
public class RecordWithAllTypes implements Record<Integer> {
    @PrimaryKey
    @Column("primary_key")
    private Integer id;

    // fields
    @Column("a_field")
    private String a;
    @Column("b_field")
    private Integer b;
    @Column("c_field")
    private Boolean c;
    @Column("d_field")
    private Date d;
    @Column("e_field")
    private Long e;
    @Column("f_field")
    private BigDecimal f;
    @Column("g_field")
    private EnumType g;
    @Column("h_field")
    private LocalDate h;

    public RecordWithAllTypes() {
    }

    public RecordWithAllTypes(Integer id, String a, Integer b, Boolean c, Date d, Long e, BigDecimal f, EnumType g,
                              LocalDate h) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }

    public Boolean getC() {
        return c;
    }

    public void setC(Boolean c) {
        this.c = c;
    }

    public Date getD() {
        return d;
    }

    public void setD(Date d) {
        this.d = d;
    }

    public Long getE() {
        return e;
    }

    public void setE(Long e) {
        this.e = e;
    }

    public BigDecimal getF() {
        return f;
    }

    public void setF(BigDecimal f) {
        this.f = f;
    }

    public EnumType getG() {
        return g;
    }

    public void setG(EnumType g) {
        this.g = g;
    }

    public LocalDate getH() {
        return h;
    }

    public void setH(LocalDate h) {
        this.h = h;
    }

    public static RecordWithAllTypes newRecord(Integer id, Random random) {
        return new RecordWithAllTypes(
            id,
            UUID.randomUUID().toString(),
            random.nextInt(),
            random.nextBoolean(),
            new Date(),
            random.nextLong(),
            BigDecimal.TEN,
            EnumType.F2,
            LocalDate.now()
        );
    }

    public static RecordWithAllTypes newNullRecord(Integer id) {
        return new RecordWithAllTypes(
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
        if (o instanceof RecordWithAllTypes) {
            RecordWithAllTypes that = (RecordWithAllTypes) o;

            return Objects.equals(this.id, that.id)
                && Objects.equals(this.a, that.a)
                && Objects.equals(this.b, that.b)
                && Objects.equals(this.c, that.c)
                && Objects.equals(this.d, that.d)
                && Objects.equals(this.e, that.e)
                && Base.compareBigDecimals(this.f, that.f)
                && Objects.equals(this.g, that.g)
                && Objects.equals(this.h, that.h);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c, d, e, f, g, h);
    }

    @Override
    public String toString() {
        return "[ImmutableRecord: id=" + id
            + " a=" + a + " b=" + b + " c=" + c + " d=" + d
            + " e=" + e + " f=" + f + " g=" + g + " h=" + h
            + "]";
    }
}
