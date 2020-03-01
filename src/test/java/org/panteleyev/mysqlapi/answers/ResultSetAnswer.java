package org.panteleyev.mysqlapi.answers;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

abstract class ResultSetAnswer {
    private final Map<String, Object> valueMap = new HashMap<>();

    ResultSetAnswer(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                valueMap.put(field.getName(), field.get(object));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    Object getValue(String fieldName) {
        return valueMap.get(fieldName);
    }
}
