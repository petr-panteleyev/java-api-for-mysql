/*
 * Copyright (c) 2018, 2019, Petr Panteleyev <petr@panteleyev.org>
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

package org.panteleyev.mysqlapi.model;

import org.panteleyev.mysqlapi.Record;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.panteleyev.mysqlapi.annotations.Table;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

@Table("binary_table")
public class BinaryRecord implements Record<Integer> {
    @PrimaryKey
    @Column("primary_key")
    private Integer primKey;

    @Column(value = "a_field", length = 3000)
    private byte[] aField;

    public BinaryRecord() {
    }

    private BinaryRecord(int primKey, byte[] aField) {
        this.primKey = primKey;
        this.aField = aField;
    }

    public int getPrimKey() {
        return primKey;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof BinaryRecord) {
            var that = (BinaryRecord) object;
            return Objects.equals(this.primKey, that.primKey)
                    && Arrays.equals(this.aField, that.aField);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(primKey, aField);
    }

    public static BinaryRecord newRecord(Integer id, Random random) {
        byte[] a = new byte[1000];
        random.nextBytes(a);

        return new BinaryRecord(id, a);
    }

    public static BinaryRecord newNullRecord(Integer id) {
        return new BinaryRecord(id, null);
    }
}
