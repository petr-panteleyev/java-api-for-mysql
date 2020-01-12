/*
 * Copyright (c) 2016, 2019, Petr Panteleyev <petr@panteleyev.org>
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

import org.panteleyev.mysqlapi.annotations.Table;

/**
 * Database record.
 * @param <K> type of the primary key
 */
public interface Record<K> {
    /**
     * Returns table name. Class must be annotated by {@link Table}.
     *
     * @return table name
     * @throws IllegalStateException if class is not annotated by {@link Table}.
     */
    default String getTableName() {
        return getTableName(getClass());
    }

    /**
     * Returns table name for table class. Class must be annotated by {@link Table}.
     *
     * @param table table class
     * @return table name
     * @throws IllegalStateException if class is not annotated by {@link Table}.
     */
    static String getTableName(Class<? extends Record> table) {
        var annotation = table.getAnnotation(Table.class);
        if (annotation != null) {
            return annotation.value();
        } else {
            throw new IllegalStateException("Class " + table.getName() + "is not properly annotated");
        }
    }

    /**
     * Returns primary key value.
     * @return primary key value
     */
    default K getPrimaryKey() {
        return MySqlClient.getPrimaryKey(this);
    }
}
