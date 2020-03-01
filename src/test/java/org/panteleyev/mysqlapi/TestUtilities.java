package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.model.NotAnnotatedRecord;
import org.panteleyev.mysqlapi.model.RecordWithAllTypes;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class TestUtilities {
    @Test
    public void testGetTableName() {
        Assert.assertEquals(Record.getTableName(RecordWithAllTypes.class), "all_types_table");
        Assert.assertEquals(new RecordWithAllTypes().getTableName(), "all_types_table");
    }

    @Test(expectedExceptions = {IllegalStateException.class})
    public void testGetTableNameStaticNegative() {
        Record.getTableName(NotAnnotatedRecord.class);
    }

    @Test(expectedExceptions = {IllegalStateException.class})
    public void testGetTableNameNegative() {
        new NotAnnotatedRecord().getTableName();
    }
}
