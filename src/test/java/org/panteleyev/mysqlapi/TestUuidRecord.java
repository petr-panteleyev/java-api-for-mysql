/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.mysqlapi;

import org.panteleyev.mysqlapi.model.RecordWithUuid;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Collections;
import java.util.UUID;
import static org.testng.Assert.assertEquals;

@Test
public class TestUuidRecord extends Base {
    @Test
    public void testUuidTable() {
        getDao().createTables(Collections.singletonList(RecordWithUuid.class));
        getDao().preload(Collections.singletonList(RecordWithUuid.class));

        int id = getDao().generatePrimaryKey(RecordWithUuid.class);
        var uuid = UUID.randomUUID();
        var uuidBin = UUID.randomUUID();

        var record = new RecordWithUuid(id, uuid, uuidBin);

        getDao().insert(record);

        getDao().get(id, RecordWithUuid.class).ifPresentOrElse(inserted -> {
            assertEquals(inserted.getId(), record.getId());
            assertEquals(inserted.getUuid(), record.getUuid());
            assertEquals(inserted.getUuidBin(), record.getUuidBin());
        }, Assert::fail);

        // Update
        var newUuid = UUID.randomUUID();
        var newUuidBin = UUID.randomUUID();
        var update = new RecordWithUuid(id, newUuid, newUuidBin);
        getDao().update(update);

        getDao().get(id, RecordWithUuid.class).ifPresentOrElse(updated -> {
            assertEquals(updated.getId(), record.getId());
            assertEquals(updated.getUuid(), newUuid);
            assertEquals(updated.getUuidBin(), newUuidBin);
        }, Assert::fail);
    }
}
