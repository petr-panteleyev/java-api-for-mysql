package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import java.util.Set;

interface DataTypes {
    String TYPE_BIG_DECIMAL = "java.math.BigDecimal";
    String TYPE_DATE        = "java.util.Date";
    String TYPE_LOCAL_DATE  = "java.time.LocalDate";
    String TYPE_LONG        = "java.lang.Long";
    String TYPE_INTEGER     = "java.lang.Integer";
    String TYPE_BOOLEAN     = "java.lang.Boolean";
    String TYPE_STRING      = "java.lang.String";
    String TYPE_UUID        = "java.util.UUID";
    String TYPE_LONG_PRIM   = "long";
    String TYPE_INT         = "int";
    String TYPE_BOOL        = "boolean";
    String TYPE_BYTE_ARRAY  = "byte[]";

    String TYPE_ENUM        = "*** enum ***";

    // Exception strings
    String CLASS_NOT_ANNOTATED = "Class is not properly annotated: ";
    String FIELD_NOT_ANNOTATED = "Field is not properly annotated: ";
    String BAD_FIELD_TYPE   = "Unsupported field type: ";

    Set<String> AUTO_INCREMENT_TYPES = Set.of(TYPE_INT, TYPE_INTEGER, TYPE_LONG, TYPE_LONG_PRIM);
}
