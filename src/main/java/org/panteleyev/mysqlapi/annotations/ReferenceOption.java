/*
 * Copyright (c) 2015, 2017, Petr Panteleyev <petr@panteleyev.org>
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
package org.panteleyev.mysqlapi.annotations;

/**
 * Foreign key reference option.
 * @see <a href="https://dev.mysql.com/doc/refman/5.7/en/create-table-foreign-keys.html">MySQL Foreign Keys</a>
 */
public enum ReferenceOption {
    /**
     * No action is specified in the ON DELETE or ON UPDATE clause.
     */
    NONE,

    /**
     * Rejects the delete or update operation for the parent table.
     */
    RESTRICT,

    /**
     * Delete or update the row from the parent table, and automatically delete or update the matching rows
     * in the child table.
     */
    CASCADE,

    /**
     * Delete or update the row from the parent table, and set the foreign key column or columns in the
     * child table to NULL.
     */
    SET_NULL,

    /**
     * Same as {@link #RESTRICT}
     */
    NO_ACTION;

    @Override
    public String toString() {
        return name().replaceAll("_", " ");
    }
}
