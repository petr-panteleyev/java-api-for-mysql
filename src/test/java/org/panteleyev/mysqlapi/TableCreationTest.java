package org.panteleyev.mysqlapi;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.model.ImmutableRecord;
import org.panteleyev.mysqlapi.model.ImmutableRecordWithPrimitives;
import org.panteleyev.mysqlapi.model.RecordWithAllTypes;
import org.panteleyev.mysqlapi.model.RecordWithOptionals;
import org.panteleyev.mysqlapi.model.RecordWithPrimitives;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Arrays;

@Test
public class TableCreationTest extends Base {
    @Test
    public void testCreateTables() {
        getDao().createTables(Arrays.asList(
            RecordWithAllTypes.class,
            RecordWithOptionals.class,
            RecordWithPrimitives.class,
            ImmutableRecord.class,
            ImmutableRecordWithPrimitives.class
        ));

        Assert.assertTrue(getDao().getAll(RecordWithAllTypes.class).isEmpty());
        Assert.assertTrue(getDao().getAll(RecordWithOptionals.class).isEmpty());
        Assert.assertTrue(getDao().getAll(RecordWithPrimitives.class).isEmpty());
        Assert.assertTrue(getDao().getAll(ImmutableRecord.class).isEmpty());
        Assert.assertTrue(getDao().getAll(ImmutableRecordWithPrimitives.class).isEmpty());
    }
}
