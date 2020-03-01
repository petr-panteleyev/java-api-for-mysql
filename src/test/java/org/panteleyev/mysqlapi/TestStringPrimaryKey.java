package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.model.StringPrimaryKeyRecord;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Collections;
import java.util.UUID;
import static org.testng.Assert.assertEquals;

@Test
public class TestStringPrimaryKey extends Base {

    @Test
    public void testStringPrimaryKey() {
        getDao().createTables(Collections.singletonList(StringPrimaryKeyRecord.class));
        getDao().preload(Collections.singletonList(StringPrimaryKeyRecord.class));

        var id = UUID.randomUUID().toString();
        var record = new StringPrimaryKeyRecord(id, UUID.randomUUID().toString());

        getDao().insert(record);

        getDao().get(id, StringPrimaryKeyRecord.class)
            .ifPresentOrElse(retrieved -> assertEquals(retrieved, record), Assert::fail);

        var newValue = UUID.randomUUID().toString();
        getDao().update(new StringPrimaryKeyRecord(record.getPrimKey(), newValue));

        getDao().get(id, StringPrimaryKeyRecord.class).ifPresentOrElse(updated -> {
            assertEquals(updated.getPrimKey(), record.getPrimKey());
            assertEquals(updated.getValue(), newValue);
        }, Assert::fail);
    }
}
