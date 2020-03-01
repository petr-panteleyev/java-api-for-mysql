package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.model.BinaryRecord;
import org.panteleyev.mysqlapi.model.ImmutableBinaryRecord;
import org.panteleyev.mysqlapi.model.RecordWithAllTypes;
import org.panteleyev.mysqlapi.model.RecordWithOptionals;
import org.panteleyev.mysqlapi.model.RecordWithPrimitives;
import org.panteleyev.mysqlapi.model.UuidPrimaryKeyRecord;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

@Test
public class TestRecords extends Base {
    private static final int RECORD_COUNT_1 = 10;
    private static final int RECORD_COUNT_2 = 10;

    private static final List<Class<? extends Record>> ALL_CLASSES =
        Arrays.asList(RecordWithAllTypes.class, RecordWithOptionals.class,
            ImmutableBinaryRecord.class, BinaryRecord.class);

    @Test(dataProvider = "recordClasses")
    public void testRecordCreation(Class<? extends Record<Integer>> clazz) throws Exception {
        getDao().createTables(Collections.singletonList(clazz));
        getDao().preload(Collections.singletonList(clazz));

        var idMap = new HashMap<Integer, Record<Integer>>();

        // Create all new records
        for (int i = 0; i < RECORD_COUNT_1; i++) {
            var newRecord = givenRandomRecord(clazz);

            getDao().insert(newRecord);
            idMap.put(newRecord.getPrimaryKey(), newRecord);
        }

        checkCreatedRecord(clazz, idMap, RECORD_COUNT_1);
    }

    @Test
    public void testParallelRecordCreation() throws Exception {
        getDao().createTables(ALL_CLASSES);
        getDao().preload(ALL_CLASSES);

        var idMap1 = new HashMap<Integer, Record<Integer>>();
        var idMap2 = new HashMap<Integer, Record<Integer>>();

        var t1 = new Thread(() -> {
            for (int i = 0; i < RECORD_COUNT_1; i++) {
                try {
                    var newRecord = givenRandomRecord(RecordWithAllTypes.class);
                    getDao().insert(newRecord);
                    idMap1.put(newRecord.getId(), newRecord);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        var t2 = new Thread(() -> {
            for (int i = 0; i < RECORD_COUNT_2; i++) {
                try {
                    var newRecord = givenRandomRecord(RecordWithOptionals.class);
                    getDao().insert(newRecord);
                    idMap2.put(newRecord.getId(), newRecord);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        checkCreatedRecord(RecordWithAllTypes.class, idMap1, RECORD_COUNT_1);
        checkCreatedRecord(RecordWithOptionals.class, idMap2, RECORD_COUNT_2);
    }

    @Test(dataProvider = "recordClasses")
    public void testRecordPutGet(Class<? extends Record<Integer>> clazz) throws Exception {
        getDao().createTables(Collections.singletonList(clazz));
        getDao().preload(Collections.singletonList(clazz));

        for (int i = 0; i < RECORD_COUNT_1; i++) {
            var record = givenRandomRecord(clazz);

            getDao().insert(record);
            getDao().get(record.getPrimaryKey(), clazz)
                .ifPresentOrElse(result -> assertEquals(result, record), Assert::fail);
        }
    }

    @Test(dataProvider = "recordClasses")
    public void testRecordPutDelete(Class<? extends Record<Integer>> clazz) throws Exception {
        getDao().createTables(Collections.singletonList(clazz));
        getDao().preload(Collections.singletonList(clazz));

        // Delete by record
        for (int i = 0; i < RECORD_COUNT_1; i++) {
            var record = givenRandomRecord(clazz);
            getDao().insert(record);
            getDao().get(record.getPrimaryKey(), clazz)
                .ifPresentOrElse(result -> assertEquals(result, record), Assert::fail);

            getDao().delete(record);
            assertTrue(getDao().get(record.getPrimaryKey(), clazz).isEmpty());
        }

        // Delete by id
        for (int i = 0; i < RECORD_COUNT_1; i++) {
            var record = givenRandomRecord(clazz);
            getDao().insert(record);
            getDao().get(record.getPrimaryKey(), clazz)
                .ifPresentOrElse(result -> assertEquals(result, record), Assert::fail);

            getDao().delete(record.getPrimaryKey(), clazz);
            assertTrue(getDao().get(record.getPrimaryKey(), clazz).isEmpty());
        }
    }

    @Test(dataProvider = "recordClasses")
    public void testRecordUpdate(Class<? extends Record<Integer>> clazz) throws Exception {
        getDao().createTables(Collections.singletonList(clazz));
        getDao().preload(Collections.singletonList(clazz));

        var original = givenRandomRecord(clazz);

        getDao().insert(original);

        var updated = givenRandomRecordWithId(clazz, original.getPrimaryKey());

        getDao().update(updated);

        var retrievedUpdated = getDao().get(original.getPrimaryKey(), clazz);
        assertEquals(retrievedUpdated.orElseThrow(), updated);
    }

    @Test(dataProvider = "recordClasses")
    public void testNullFields(Class<? extends Record<Integer>> clazz) throws Exception {
        getDao().createTables(Collections.singletonList(clazz));
        getDao().preload(Collections.singletonList(clazz));

        var record = givenNullRecord(clazz);

        getDao().insert(record);

        var retrieved = getDao().get(record.getPrimaryKey(), clazz);
        assertEquals(retrieved.orElseThrow(), record);
    }

    @Test
    public void testExtremeValues() {
        var clazz = RecordWithPrimitives.class;

        getDao().createTables(Collections.singletonList(clazz));
        getDao().preload(Collections.singletonList(clazz));

        // Max values
        var idMax = getDao().generatePrimaryKey(clazz);
        var rMax = new RecordWithPrimitives(idMax,
            Integer.MAX_VALUE,
            RANDOM.nextBoolean(),
            Long.MAX_VALUE);
        getDao().insert(rMax);

        var retrievedMax = getDao().get(rMax.getId(), clazz);
        assertEquals(retrievedMax.orElseThrow(), rMax);

        // Min values
        var idMin = getDao().generatePrimaryKey(clazz);
        var rMin = new RecordWithPrimitives(idMin,
            Integer.MIN_VALUE,
            RANDOM.nextBoolean(),
            Long.MIN_VALUE);
        getDao().insert(rMin);

        var retrievedMin = getDao().get(rMin.getId(), clazz);
        assertEquals(retrievedMin.orElseThrow(), rMin);
    }

    @Test
    public void testTruncate() {
        List<Class<? extends Record>> classes = Arrays.asList(RecordWithAllTypes.class, RecordWithPrimitives.class);

        getDao().createTables(classes);
        getDao().preload(classes);

        List<Record> l1 = Arrays.asList(
            RecordWithAllTypes.newRecord(getDao().generatePrimaryKey(RecordWithAllTypes.class), RANDOM),
            RecordWithAllTypes.newRecord(getDao().generatePrimaryKey(RecordWithAllTypes.class), RANDOM),
            RecordWithAllTypes.newRecord(getDao().generatePrimaryKey(RecordWithAllTypes.class), RANDOM),
            RecordWithAllTypes.newRecord(getDao().generatePrimaryKey(RecordWithAllTypes.class), RANDOM),
            RecordWithAllTypes.newRecord(getDao().generatePrimaryKey(RecordWithAllTypes.class), RANDOM)
        );

        List<Record> l2 = Arrays.asList(
            RecordWithPrimitives.newRecord(getDao().generatePrimaryKey(RecordWithPrimitives.class), RANDOM),
            RecordWithPrimitives.newRecord(getDao().generatePrimaryKey(RecordWithPrimitives.class), RANDOM),
            RecordWithPrimitives.newRecord(getDao().generatePrimaryKey(RecordWithPrimitives.class), RANDOM)
        );

        getDao().insert(l1.size(), l1);
        getDao().insert(l2.size(), l2);

        assertEquals(getDao().getAll(RecordWithAllTypes.class).size(), 5);
        assertEquals(getDao().getAll(RecordWithPrimitives.class).size(), 3);

        assertNotEquals(getDao().generatePrimaryKey(RecordWithAllTypes.class), 1);
        assertNotEquals(getDao().generatePrimaryKey(RecordWithPrimitives.class), 1);

        getDao().truncate(classes);

        assertEquals(getDao().getAll(RecordWithAllTypes.class).size(), 0);
        assertEquals(getDao().getAll(RecordWithPrimitives.class).size(), 0);

        assertEquals((int) getDao().generatePrimaryKey(RecordWithAllTypes.class), 1);
        assertEquals((int) getDao().generatePrimaryKey(RecordWithPrimitives.class), 1);
    }

    private <T extends Record> void checkCreatedRecord(Class<T> clazz, Map<Integer, Record<Integer>> idMap,
                                                       int count)
    {
        // Get all records back in one request
        List<T> result = getDao().getAll(clazz);

        // Check total amount or records returned
        assertEquals(result.size(), count);
        assertEquals(result.size(), idMap.keySet().size());

        // Check uniqueness of all primary keys
        assertEquals(result.stream()
            .map(Record::getPrimaryKey)
            .distinct()
            .count(), count);
    }

    @Test
    public void testWrongId() {
        var record = new UuidPrimaryKeyRecord(UUID.randomUUID(), UUID.randomUUID().toString());

        getDao().createTables(List.of(UuidPrimaryKeyRecord.class));
        getDao().preload(List.of(UuidPrimaryKeyRecord.class));

        getDao().insert(record);
        var retrieved = getDao().get(record.getPrimKey(), UuidPrimaryKeyRecord.class);
        assertEquals(retrieved.orElseThrow(), record);

        var notFound = getDao().get(UUID.randomUUID(), UuidPrimaryKeyRecord.class);
        assertTrue(notFound.isEmpty());

    }
}
