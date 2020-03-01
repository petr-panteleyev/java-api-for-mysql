package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

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
