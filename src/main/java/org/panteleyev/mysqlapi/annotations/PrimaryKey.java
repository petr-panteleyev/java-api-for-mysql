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
 * Defines if annotated column serves as a primary key.
 */
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
public @interface PrimaryKey {
    /**
     * Defines if primary key is auto-incremented integer value. Works only for fields of type int and Integer. For
     * other types attribute is ignored.
     *
     * @return if primary key is auto-incremented
     */
    boolean isAutoIncrement() default true;
}
