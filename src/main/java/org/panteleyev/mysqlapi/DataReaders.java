/*
 * Copyright (c) 2020, Petr Panteleyev <petr@panteleyev.org>
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

package org.panteleyev.mysqlapi;

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
