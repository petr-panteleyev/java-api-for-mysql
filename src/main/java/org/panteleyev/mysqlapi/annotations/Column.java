/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.mysqlapi.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Defines database column.
 */
@Retention(RUNTIME)
@Target({FIELD, PARAMETER, RECORD_COMPONENT})
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
     *
     * @return name of the column
     */
    String value();

    /**
     * Defines if the column can be NULL.
     *
     * @return <code>true</code> if the column can take NULL values
     */
    boolean nullable() default true;

    /**
     * Defines length of the column.
     *
     * @return length of the column
     */
    int length() default LENGTH;

    /**
     * Defines PRECISION. Applicable to numeric data types.
     *
     * @return PRECISION
     */
    int precision() default PRECISION;

    /**
     * Defines SCALE. Applicable to numeric data types.
     *
     * @return SCALE
     */
    int scale() default SCALE;

    /**
     * Defines JSON column. Applicable to String data type only, ignored for other types.
     *
     * @return if column contains json
     */
    boolean isJson() default false;

    /**
     * Defines if UUID column should be stored as binary column. For MySQL only.
     *
     * @return if store UUID as binary
     */
    boolean storeUuidAsBinary() default false;

    /**
     * Defines if column should have unique constraint.
     *
     * @return if column is unique
     */
    boolean unique() default false;
}
