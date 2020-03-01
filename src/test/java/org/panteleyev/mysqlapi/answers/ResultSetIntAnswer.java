package org.panteleyev.mysqlapi.answers;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ResultSetIntAnswer extends ResultSetAnswer implements Answer<Integer> {
    public ResultSetIntAnswer(Object object) {
        super(object);
    }

    @Override
    public Integer answer(InvocationOnMock inv) {
        String fieldName = (String) inv.getArguments()[0];
        Object result = getValue(fieldName);
        return result == null ? 0 : (Integer) result;
    }
}
