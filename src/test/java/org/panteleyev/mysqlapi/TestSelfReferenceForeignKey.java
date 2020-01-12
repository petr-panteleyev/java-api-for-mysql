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

import org.panteleyev.mysqlapi.model.SelfReferencingTable;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;
import java.util.UUID;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

@Test
public class TestSelfReferenceForeignKey extends Base {

    private void deleteForbidden(Record record) {
        var exception = false;
        try {
            getDao().delete(record);
        } catch (Exception ex) {
            exception = true;
        }
        assertTrue(exception);
    }

    private void updateForbidden(SelfReferencingTable record) {
        var exception = false;
        try {
            record.setValue(UUID.randomUUID().toString());
            getDao().update(record);
        } catch (Exception ex) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void testForeignKeyOnDelete() {
        List<Class<? extends Record>> classes = List.of(SelfReferencingTable.class);

        getDao().createTables(classes);
        getDao().preload(classes);

        var cascade = new SelfReferencingTable(getDao().generatePrimaryKey(SelfReferencingTable.class),
            UUID.randomUUID().toString());
        getDao().insert(cascade);

        var restrict = new SelfReferencingTable(getDao().generatePrimaryKey(SelfReferencingTable.class),
            UUID.randomUUID().toString());
        getDao().insert(restrict);

        var setNull = new SelfReferencingTable(getDao().generatePrimaryKey(SelfReferencingTable.class),
            UUID.randomUUID().toString());
        getDao().insert(setNull);

        var noAction = new SelfReferencingTable(getDao().generatePrimaryKey(SelfReferencingTable.class),
            UUID.randomUUID().toString());
        getDao().insert(noAction);

        var none = new SelfReferencingTable(getDao().generatePrimaryKey(SelfReferencingTable.class),
            UUID.randomUUID().toString());
        getDao().insert(none);

        var table = new SelfReferencingTable(
            getDao().generatePrimaryKey(SelfReferencingTable.class),
            UUID.randomUUID().toString(),
            setNull.getValue(),
            cascade.getValue(),
            restrict.getValue(),
            noAction.getValue(),
            none.getValue()
        );
        getDao().insert(table);

        deleteForbidden(none);
        deleteForbidden(noAction);
        deleteForbidden(restrict);

        // Set null
        getDao().delete(setNull);
        getDao().get(table.getId(), SelfReferencingTable.class)
            .ifPresentOrElse(setNullCheck -> assertNull(setNullCheck.getNullValue()), Assert::fail);

        // Cascade
        getDao().delete(cascade);
        var cascadeCheck = getDao().get(table.getId(), SelfReferencingTable.class);
        assertTrue(cascadeCheck.isEmpty());
    }

    @Test
    public void testForeignKeyOnUpdate() {
        List<Class<? extends Record>> classes = List.of(SelfReferencingTable.class);

        getDao().createTables(classes);
        getDao().preload(classes);

        var cascade = new SelfReferencingTable(getDao().generatePrimaryKey(SelfReferencingTable.class),
            UUID.randomUUID().toString());
        getDao().insert(cascade);

        var restrict = new SelfReferencingTable(getDao().generatePrimaryKey(SelfReferencingTable.class),
            UUID.randomUUID().toString());
        getDao().insert(restrict);

        var setNull = new SelfReferencingTable(getDao().generatePrimaryKey(SelfReferencingTable.class),
            UUID.randomUUID().toString());
        getDao().insert(setNull);

        var noAction = new SelfReferencingTable(getDao().generatePrimaryKey(SelfReferencingTable.class),
            UUID.randomUUID().toString());
        getDao().insert(noAction);

        var none = new SelfReferencingTable(getDao().generatePrimaryKey(SelfReferencingTable.class),
            UUID.randomUUID().toString());
        getDao().insert(none);

        var table = new SelfReferencingTable(
            getDao().generatePrimaryKey(SelfReferencingTable.class),
            UUID.randomUUID().toString(),
            setNull.getValue(),
            cascade.getValue(),
            restrict.getValue(),
            noAction.getValue(),
            none.getValue()
        );
        getDao().insert(table);

        updateForbidden(none);
        updateForbidden(noAction);
        updateForbidden(restrict);

        // Mysql does not support this for INNODB
        updateForbidden(setNull);
        updateForbidden(cascade);
    }
}
