/*
 * Copyright (c) 2018, 2019, Petr Panteleyev <petr@panteleyev.org>
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines database column.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Column {
    /**
     * Most used value for the primary key column.
     */
    String ID = "id";

    /**
     * Default column length.
     */
    int LENGTH = 255;

    /**
     * Default scale of BigDecimal column.
     */
    int SCALE = 6;

    /**
     * Default precision of BigDecimal column.
     */
    int PRECISION = 15;

    /**
     * SQL name of the column.
     * @return name of the column
     */
    String value();

    /**
     * Defines if the column can be NULL.
     * @return <code>true</code> if the column can take NULL values
     */
    boolean nullable() default true;

    /**
     * Defines length of the column.
     * @return length of the column
     */
    int length() default LENGTH;

    /**
     * Defines PRECISION. Applicable to numeric data types.
     * @return PRECISION
     */
    int precision() default PRECISION;

    /**
     * Defines SCALE. Applicable to numeric data types.
     * @return SCALE
     */
    int scale() default SCALE;

    /**
     * Defines JSON column. Applicable to String data type only, ignored for other types.
     * @return if column contains json
     */
    boolean isJson() default false;

    /**
     * Defines if UUID column should be stored as binary column. For MySQL only.
     * @return if store UUID as binary
     */
    boolean storeUuidAsBinary() default false;

    /**
     * Defines if column should have unique constraint.
     * @return if column is unique
     */
    boolean unique() default false;
}
