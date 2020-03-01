package org.panteleyev.mysqlapi.answers;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.math.BigDecimal;

public class ResultSetBigDecimalAnswer extends ResultSetAnswer implements Answer<BigDecimal> {
    public ResultSetBigDecimalAnswer(Object object) {
        super(object);
    }

    @Override
    public BigDecimal answer(InvocationOnMock inv) {
        String fieldName = (String) inv.getArguments()[0];
        Object result = getValue(fieldName);
        return (BigDecimal) result;
    }
}
