/*
 * Copyright (c) 2017, 2019, Petr Panteleyev <petr@panteleyev.org>
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
import org.panteleyev.mysqlapi.annotations.ForeignKey;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.panteleyev.mysqlapi.annotations.RecordBuilder;
import org.panteleyev.mysqlapi.annotations.ReferenceOption;
import org.panteleyev.mysqlapi.annotations.Table;

@Table("child_table")
public class ChildTable implements Record<Integer> {
    @PrimaryKey
    @Column(Column.ID)
    private int id;

    @Column("null_value")
    @ForeignKey(table = ParentTable.class, column = "value",
            onDelete = ReferenceOption.SET_NULL, onUpdate = ReferenceOption.SET_NULL)
    private final String nullValue;

    @Column("cascade_value")
    @ForeignKey(table = ParentTable.class, column = "value",
            onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    private final String cascadeValue;

    @Column("restrict_value")
    @ForeignKey(table = ParentTable.class, column = "value",
            onDelete = ReferenceOption.RESTRICT, onUpdate = ReferenceOption.RESTRICT)
    private final String restrictValue;

    @Column("no_action_value")
    @ForeignKey(table = ParentTable.class, column = "value",
            onDelete = ReferenceOption.NO_ACTION, onUpdate = ReferenceOption.NO_ACTION)
    private final String noActionValue;

    @Column("none_value")
    @ForeignKey(table = ParentTable.class, column = "value")
    private final String noneValue;

    @RecordBuilder
    public ChildTable(@Column("id") int id,
                      @Column("null_value") String nullValue,
                      @Column("cascade_value") String cascadeValue,
                      @Column("restrict_value") String restrictValue,
                      @Column("no_action_value") String noActionValue,
                      @Column("none_value") String noneValue) {
        this.id = id;
        this.nullValue = nullValue;
        this.cascadeValue = cascadeValue;
        this.restrictValue = restrictValue;
        this.noActionValue = noActionValue;
        this.noneValue = noneValue;
    }

    public int getId() {
        return id;
    }

    public String getNullValue() {
        return nullValue;
    }

    public String getCascadeValue() {
        return cascadeValue;
    }

    public String getRestrictValue() {
        return restrictValue;
    }

    public String getNoActionValue() {
        return noActionValue;
    }

    public String getNoneValue() {
        return noneValue;
    }
}
