package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.model.IntegerPrimaryKeyRecord;
import org.panteleyev.mysqlapi.model.NoPrimaryKeyRecord;
import org.panteleyev.mysqlapi.model.NonAutoIncrementPrimaryKeyRecord;
import org.panteleyev.mysqlapi.model.StringPrimaryKeyRecord;
import org.testng.annotations.Test;
import java.util.List;
import java.util.UUID;
import static org.panteleyev.mysqlapi.Base.RANDOM;
import static org.testng.Assert.assertEquals;

@Test
public class TestPrimaryKey {

    @Test
    public void testGetPrimaryKey() {
        var intKey = RANDOM.nextInt(Integer.MAX_VALUE);
        var record = new IntegerPrimaryKeyRecord(intKey, UUID.randomUUID().toString());
        assertEquals((int) record.getPrimaryKey(), intKey);

        var strKey = UUID.randomUUID().toString();
        var strRecord = new StringPrimaryKeyRecord(strKey, UUID.randomUUID().toString());
        assertEquals(strRecord.getPrimaryKey(), strKey);
    }

    @Test
    public void testAutoIncrement() {
        var dao = new MySqlClient();

        for (int i = 1; i < 100; i++) {
            var key = dao.generatePrimaryKey(IntegerPrimaryKeyRecord.class);
            assertEquals(i, (int) key);
        }

        dao.resetPrimaryKey(IntegerPrimaryKeyRecord.class);

        for (int i = 1; i < 100; i++) {
            var key = dao.generatePrimaryKey(IntegerPrimaryKeyRecord.class);
            assertEquals(i, (int) key);
        }
    }

    @Test(expectedExceptions = {IllegalStateException.class})
    public void testNoPrimaryKey() {
        var record = new NoPrimaryKeyRecord(UUID.randomUUID().toString());
        record.getPrimaryKey();
    }

    @Test(expectedExceptions = {IllegalStateException.class})
    public void testNonAutoIncrement() {
        var dao = new MySqlClient();
        dao.preload(List.of(NonAutoIncrementPrimaryKeyRecord.class));

        var key = dao.generatePrimaryKey(NonAutoIncrementPrimaryKeyRecord.class);
    }
}
