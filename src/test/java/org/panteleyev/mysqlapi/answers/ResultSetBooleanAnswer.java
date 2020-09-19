/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.mysqlapi.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ResultSetBooleanAnswer extends ResultSetAnswer implements Answer<Boolean> {
    public ResultSetBooleanAnswer(Object object) {
        super(object);
    }

    @Override
    public Boolean answer(InvocationOnMock inv) {
        String fieldName = (String) inv.getArguments()[0];
        Object result = getValue(fieldName);
        return result != null && (Boolean) result;
    }
}
