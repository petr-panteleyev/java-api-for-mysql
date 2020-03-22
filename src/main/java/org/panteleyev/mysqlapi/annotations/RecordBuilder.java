package org.panteleyev.mysqlapi.annotations;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Defines constructor used for record retrieval. All parameters of such constructor must be annotated with
 * {@link Column} annotation.
 */
@Retention(RUNTIME)
@Target(CONSTRUCTOR)
public @interface RecordBuilder {
}
