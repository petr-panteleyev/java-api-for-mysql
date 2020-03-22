package org.panteleyev.mysqlapi.annotations;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Defines index for the table column.
 */

@Retention(RUNTIME)
@Target(FIELD)
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
