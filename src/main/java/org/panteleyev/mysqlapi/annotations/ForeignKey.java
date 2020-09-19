/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.mysqlapi.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Defines foreign key.
 */
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
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
