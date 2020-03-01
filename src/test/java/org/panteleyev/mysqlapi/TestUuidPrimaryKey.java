package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.model.UuidPrimaryKeyRecord;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Collections;
import java.util.UUID;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test
public class TestUuidPrimaryKey extends Base {

    @Test
    public void testUuidPrimaryKey() {
        getDao().createTables(Collections.singletonList(UuidPrimaryKeyRecord.class));
        getDao().preload(Collections.singletonList(UuidPrimaryKeyRecord.class));

        var id = UUID.randomUUID();
        var record = new UuidPrimaryKeyRecord(id, UUID.randomUUID().toString());

        getDao().insert(record);

        getDao().get(id, UuidPrimaryKeyRecord.class)
            .ifPresentOrElse(retrieved -> assertEquals(retrieved, record), Assert::fail);

        var newValue = UUID.randomUUID().toString();
        getDao().update(new UuidPrimaryKeyRecord(record.getPrimKey(), newValue));

        getDao().get(id, UuidPrimaryKeyRecord.class).ifPresentOrElse(updated -> {
            assertEquals(updated.getPrimKey(), record.getPrimKey());
            assertEquals(updated.getValue(), newValue);
        }, Assert::fail);
    }

    @Test
    public void testUuidDeleteByRecord() {
        getDao().createTables(Collections.singletonList(UuidPrimaryKeyRecord.class));
        getDao().preload(Collections.singletonList(UuidPrimaryKeyRecord.class));

        var id = UUID.randomUUID();
        var record = new UuidPrimaryKeyRecord(id, UUID.randomUUID().toString());

        getDao().insert(record);
        getDao().get(id, UuidPrimaryKeyRecord.class)
            .ifPresentOrElse(retrieved -> assertEquals(retrieved, record), Assert::fail);

        getDao().delete(record);
        var deleted = getDao().get(id, UuidPrimaryKeyRecord.class);
        assertTrue(deleted.isEmpty());
    }

    @Test
    public void testUuidDeleteById() {
        getDao().createTables(Collections.singletonList(UuidPrimaryKeyRecord.class));
        getDao().preload(Collections.singletonList(UuidPrimaryKeyRecord.class));

        var id = UUID.randomUUID();
        var record = new UuidPrimaryKeyRecord(id, UUID.randomUUID().toString());

        getDao().insert(record);
        getDao().get(id, UuidPrimaryKeyRecord.class)
            .ifPresentOrElse(retrieved -> assertEquals(retrieved, record), Assert::fail);

        getDao().delete(id, UuidPrimaryKeyRecord.class);
        var deleted = getDao().get(id, UuidPrimaryKeyRecord.class);
        assertTrue(deleted.isEmpty());
    }
}
