package org.panteleyev.mysqlapi.annotations;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines if annotated column serves as a primary key.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {
    /**
     * Defines if primary key is auto-incremented integer value. Works only for fields of type int and Integer. For
     * other types attribute is ignored.
     *
     * @return if primary key is auto-incremented
     */
    boolean isAutoIncrement() default true;
}
