package org.panteleyev.mysqlapi.answers;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.time.LocalDate;
import java.util.Date;

public class ResultSetLongAnswer extends ResultSetAnswer implements Answer<Long> {
    public ResultSetLongAnswer(Object object) {
        super(object);
    }

    @Override
    public Long answer(InvocationOnMock inv) {
        String fieldName = (String) inv.getArguments()[0];
        Object result = getValue(fieldName);

        if (result instanceof LocalDate) {
            return ((LocalDate) result).toEpochDay();
        } else if (result instanceof Date) {
            return ((Date) result).getTime();
        } else {
            return result == null? 0 : (Long) result;
        }
    }
}
