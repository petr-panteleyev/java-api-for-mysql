package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.model.RecordWithPrimitives;
import org.panteleyev.mysqlapi.model.StringPrimaryKeyRecord;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test
public class TestBatchInsert extends Base {
    @DataProvider
    public Object[][] testBatchInsertDataProvider() {
        return new Object[][]{
            {100, 7},
            {100, 10}
        };
    }

    @Test(dataProvider = "testBatchInsertDataProvider")
    public void testBatchInsertInteger(int count, int batchSize) {
        var clazz = RecordWithPrimitives.class;

        getDao().createTables(Collections.singletonList(clazz));
        getDao().preload(Collections.singletonList(clazz));

        // Create records
        List<RecordWithPrimitives> records = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            records.add(RecordWithPrimitives.newRecord(getDao().generatePrimaryKey(clazz), RANDOM));
        }

        // Insert records
        getDao().insert(batchSize, records);

        // Retrieve records
        List<RecordWithPrimitives> retrieved = getDao().getAll(clazz);
        var map = new HashMap<Integer, RecordWithPrimitives>();
        getDao().getAll(clazz, map);

        // Size must be the same
        assertEquals(retrieved.size(), records.size());
        assertEquals(map.size(), records.size());

        assertEquals(getDao().getTableSize(clazz), records.size());

        assertRecords(records, retrieved, map);
    }

    @Test(dataProvider = "testBatchInsertDataProvider")
    public void testBatchInsertString(int count, int batchSize) {
        var clazz = StringPrimaryKeyRecord.class;

        getDao().createTables(Collections.singletonList(clazz));
        getDao().preload(Collections.singletonList(clazz));

        // Create records
        List<StringPrimaryKeyRecord> records = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            records.add(new StringPrimaryKeyRecord(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
        }

        // Insert records
        getDao().insert(batchSize, records);

        // Retrieve records
        List<StringPrimaryKeyRecord> retrieved = getDao().getAll(clazz);
        var map = new HashMap<String, StringPrimaryKeyRecord>();
        getDao().getAll(clazz, map);

        // Size must be the same
        assertEquals(retrieved.size(), records.size());
        assertEquals(map.size(), records.size());

        assertEquals(getDao().getTableSize(clazz), records.size());

        assertRecords(records, retrieved, map);
    }

    private void assertRecords(Collection<? extends TableRecord> original,
                               Collection<? extends TableRecord> retrieved,
                               Map<?, ? extends TableRecord> map)
    {
        for (var r : original) {
            assertTrue(retrieved.contains(r));
            assertTrue(map.containsValue(r));
            assertEquals(map.get(r.getPrimaryKey()), r);
        }
    }
}
