/*
 * Copyright (c) 2017, Petr Panteleyev <petr@panteleyev.org>
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
package org.panteleyev.mysqlapi;

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
