/*
 Copyright (c) Petr Panteleyev. All rights reserved.
 Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.mysqlapi;

import com.google.gson.JsonParser;
import org.panteleyev.mysqlapi.model.RecordWithJson;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Collections;
import static org.testng.Assert.assertEquals;

@Test
public class TestJson extends Base {
    private static final String EMPTY_JSON_STRING = "{}";

    private static final String PLAIN_JSON_STRING = """
        {
            "a" : "a-value",
            "b" : "b-value"
        }
        """;

    private static final String COMPLEX_JSON_STRING = """
        {
          "a": "a-value",
          "b": "b-value",
          "c": {
            "d": "d-value"
          }
        }
        """;

    @DataProvider(name = "testJsonTableProvider")
    public Object[][] testJsonTableProvider() {
        return new Object[][]{
            {EMPTY_JSON_STRING},
            {PLAIN_JSON_STRING},
            {COMPLEX_JSON_STRING}
        };
    }

    @Test(dataProvider = "testJsonTableProvider")
    public void testJsonTable(String jsonString) {
        getDao().createTables(Collections.singletonList(RecordWithJson.class));
        getDao().preload(Collections.singletonList(RecordWithJson.class));

        int id = getDao().generatePrimaryKey(RecordWithJson.class);

        var record = new RecordWithJson(id, jsonString);

        getDao().insert(record);

        getDao().get(id, RecordWithJson.class).ifPresentOrElse(retrieved -> {
            assertEquals(retrieved.getId(), record.getId());
            assertJsonEquals(retrieved.getJson(), record.getJson());
        }, Assert::fail);
    }

    private static void assertJsonEquals(String actual, String expected) {
        assertEquals(JsonParser.parseString(actual), JsonParser.parseString(expected));
    }
}
