package org.panteleyev.mysqlapi.annotations;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

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
