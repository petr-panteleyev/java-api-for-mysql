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
 * Defines foreign key.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForeignKey {
    /**
     * Referenced table class. This must be a class annotated by {@link Table}.
     *
     * @return table class
     */
    Class table();

    /**
     * Referenced column.
     *
     * @return column name
     */
    String column() default Column.ID;

    /**
     * ON DELETE reference option.
     *
     * @return reference option
     */
    ReferenceOption onDelete() default ReferenceOption.NONE;

    /**
     * ON UPDATE reference option.
     *
     * @return reference option
     */
    ReferenceOption onUpdate() default ReferenceOption.NONE;
}
