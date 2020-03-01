package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.model.ChildTable;
import org.panteleyev.mysqlapi.model.ParentTable;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Test
public class TestForeignKey extends Base {

    private void deleteForbidden(Record record) {
        var exception = false;
        try {
            getDao().delete(record);
        } catch (Exception ex) {
            exception = true;
        }
        Assert.assertTrue(exception);
    }

    private void updateForbidden(ParentTable record) {
        var exception = false;
        try {
            record.setValue(UUID.randomUUID().toString());
            getDao().update(record);
        } catch (Exception ex) {
            exception = true;
        }
        Assert.assertTrue(exception);
    }

    @Test
    public void testForeignKeyOnDelete() {
        List<Class<? extends Record>> classes = Arrays.asList(ParentTable.class, ChildTable.class);

        getDao().createTables(classes);
        getDao().preload(classes);

        var cascade = new ParentTable(getDao().generatePrimaryKey(ParentTable.class),
            UUID.randomUUID().toString());
        getDao().insert(cascade);

        var restrict = new ParentTable(getDao().generatePrimaryKey(ParentTable.class),
            UUID.randomUUID().toString());
        getDao().insert(restrict);

        var setNull = new ParentTable(getDao().generatePrimaryKey(ParentTable.class),
            UUID.randomUUID().toString());
        getDao().insert(setNull);

        var noAction = new ParentTable(getDao().generatePrimaryKey(ParentTable.class),
            UUID.randomUUID().toString());
        getDao().insert(noAction);

        var none = new ParentTable(getDao().generatePrimaryKey(ParentTable.class),
            UUID.randomUUID().toString());
        getDao().insert(none);

        var table = new ChildTable(
            getDao().generatePrimaryKey(ChildTable.class),
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
        getDao().get(table.getId(), ChildTable.class)
            .ifPresentOrElse(setNullCheck -> assertNull(setNullCheck.getNullValue()), Assert::fail);

        // Cascade
        getDao().delete(cascade);
        var cascadeCheck = getDao().get(table.getId(), ChildTable.class);
        Assert.assertTrue(cascadeCheck.isEmpty());
    }

    @Test
    public void testForeignKeyOnUpdate() {
        List<Class<? extends Record>> classes = Arrays.asList(ParentTable.class, ChildTable.class);

        getDao().createTables(classes);
        getDao().preload(classes);

        var cascade = new ParentTable(getDao().generatePrimaryKey(ParentTable.class), UUID.randomUUID().toString());
        getDao().insert(cascade);

        var restrict = new ParentTable(getDao().generatePrimaryKey(ParentTable.class), UUID.randomUUID().toString());
        getDao().insert(restrict);

        var setNull = new ParentTable(getDao().generatePrimaryKey(ParentTable.class), UUID.randomUUID().toString());
        getDao().insert(setNull);

        var noAction = new ParentTable(getDao().generatePrimaryKey(ParentTable.class), UUID.randomUUID().toString());
        getDao().insert(noAction);

        var none = new ParentTable(getDao().generatePrimaryKey(ParentTable.class),
            UUID.randomUUID().toString());
        getDao().insert(none);

        var table = new ChildTable(
            getDao().generatePrimaryKey(ChildTable.class),
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

        // Set null
        setNull.setValue(UUID.randomUUID().toString());
        getDao().update(setNull);
        getDao().get(table.getId(), ChildTable.class)
            .ifPresentOrElse(setNullCheck -> assertNull(setNullCheck.getNullValue()), Assert::fail);

        // Cascade
        cascade.setValue(UUID.randomUUID().toString());
        getDao().update(cascade);
        getDao().get(table.getId(), ChildTable.class)
            .ifPresentOrElse(cascadeCheck -> assertEquals(cascadeCheck.getCascadeValue(), cascade.getValue()),
                Assert::fail);
    }
}
