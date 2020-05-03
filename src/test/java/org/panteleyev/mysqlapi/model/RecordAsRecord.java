package org.panteleyev.mysqlapi.model;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.TableRecord;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.panteleyev.mysqlapi.annotations.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Table("record_as_record_table")
public record RecordAsRecord(
    @PrimaryKey
    @Column(Column.ID)
    Integer id,

    // fields
    // Tests may assume annotations use the same names
    @Column("a")
    String a,
    @Column("b")
    Integer b,
    @Column("c")
    Boolean c,
    @Column("d")
    Date d,
    @Column("e")
    Long e,
    @Column("f")
    BigDecimal f,
    @Column("g")
    EnumType g,
    @Column("h")
    LocalDate h
) implements TableRecord<Integer> {

    public RecordAsRecord {
        this.f = f == null? null : f.setScale(6, RoundingMode.HALF_UP);
    }

    public static RecordAsRecord newRecord(Integer id, Random random) {
        return new RecordAsRecord(
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

    public static RecordAsRecord newNullRecord(Integer id) {
        return new RecordAsRecord(
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
}
