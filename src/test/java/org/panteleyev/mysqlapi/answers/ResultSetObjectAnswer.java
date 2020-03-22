package org.panteleyev.mysqlapi.answers;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.time.LocalDate;

public class ResultSetObjectAnswer extends ResultSetAnswer implements Answer<Object> {
    public ResultSetObjectAnswer(Object object) {
        super(object);
    }

    @Override
    public Object answer(InvocationOnMock inv) {
        String fieldName = (String) inv.getArguments()[0];
        Object result = getValue(fieldName);

        if (result instanceof LocalDate date) {
            return date.toEpochDay();
        } else if (result instanceof Enum<?> enm) {
            return enm.name();
        } else {
            return result;
        }
    }
}
