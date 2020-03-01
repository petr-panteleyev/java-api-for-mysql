package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import java.util.function.BiFunction;

interface DataReaders {
    BiFunction<ResultSet, String, Object> OBJECT_READER = (ResultSet rs, String name) -> {
        try {
            return rs.getObject(name);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    };

    BiFunction<ResultSet, String, Boolean> BOOL_READER = (ResultSet rs, String name) -> {
        try {
            return rs.getBoolean(name);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    };

    BiFunction<ResultSet, String, BigDecimal> BIG_DECIMAL_READER = (ResultSet rs, String name) -> {
        try {
            return rs.getBigDecimal(name);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    };

    BiFunction<ResultSet, String, Integer> INT_READER = (ResultSet rs, String name) -> {
        try {
            return rs.getInt(name);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    };

    BiFunction<ResultSet, String, Long> LONG_READER = (ResultSet rs, String name) -> {
        try {
            return rs.getLong(name);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    };

    BiFunction<ResultSet, String, Date> DATE_READER = (ResultSet rs, String name) -> {
        try {
            return rs.getObject(name) == null ? null : new Date(rs.getLong(name));
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    };

    BiFunction<ResultSet, String, LocalDate> LOCAL_DATE_READER = (ResultSet rs, String name) -> {
        try {
            return rs.getObject(name) == null ? null : LocalDate.ofEpochDay(rs.getLong(name));
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    };

    BiFunction<ResultSet, String, byte[]> BYTE_ARRAY_READER = (ResultSet rs, String name) -> {
        try {
            return rs.getBytes(name);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    };

    BiFunction<ResultSet, String, UUID> UUID_STRING_READER = (ResultSet rs, String name) -> {
        try {
            String uuid = rs.getString(name);
            return uuid == null ? null : UUID.fromString(uuid);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    };
}
