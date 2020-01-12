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
