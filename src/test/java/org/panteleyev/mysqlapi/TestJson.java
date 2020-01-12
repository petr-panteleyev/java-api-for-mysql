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

    private static final String PLAIN_JSON_STRING = "{\n" +
        "    \"a\" : \"a-value\",\n" +
        "    \"b\" : \"b-value\"\n" +
        "}";

    private static final String COMPLEX_JSON_STRING = "{\n" +
        "  \"a\": \"a-value\",\n" +
        "  \"b\": \"b-value\",\n" +
        "  \"c\": {\n" +
        "    \"d\": \"d-value\"\n" +
        "  }\n" +
        "}";

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
