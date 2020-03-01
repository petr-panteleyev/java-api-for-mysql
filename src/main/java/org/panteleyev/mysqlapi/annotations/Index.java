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
 * Defines index for the table column.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Index {
    /**
     * Name of the index.
     * @return name of the index
     */
    String value();

    /**
     * Specifies whether this index must be unique.
     * @return unique
     */
    boolean unique() default false;
}
