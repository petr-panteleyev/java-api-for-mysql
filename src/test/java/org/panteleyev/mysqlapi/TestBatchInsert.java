/*
 * Copyright (c) 2019, 2020, Petr Panteleyev <petr@panteleyev.org>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.panteleyev.mysqlapi;

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

        assertRecords(records, retrieved, map);
    }

    private void assertRecords(Collection<? extends Record> original,
                               Collection<? extends Record> retrieved,
                               Map<?, ? extends Record> map)
    {
        for (var r : original) {
            assertTrue(retrieved.contains(r));
            assertTrue(map.containsValue(r));
            assertEquals(map.get(r.getPrimaryKey()), r);
        }
    }
}
