package org.panteleyev.mysqlapi.model;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.TableRecord;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.ForeignKey;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.panteleyev.mysqlapi.annotations.RecordBuilder;
import org.panteleyev.mysqlapi.annotations.ReferenceOption;
import org.panteleyev.mysqlapi.annotations.Table;

@Table("self_referencing_table")
public class SelfReferencingTable implements TableRecord<Integer> {
    @PrimaryKey
    @Column(Column.ID)
    private int id;

    @Column(value = "value", unique = true)
    private String value;

    @Column(value = "null_value")
    @ForeignKey(table = SelfReferencingTable.class, column = "value",
            onDelete = ReferenceOption.SET_NULL, onUpdate = ReferenceOption.SET_NULL)
    private final String nullValue;

    @Column(value = "cascade_value")
    @ForeignKey(table = SelfReferencingTable.class, column = "value",
            onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    private final String cascadeValue;

    @Column("restrict_value")
    @ForeignKey(table = SelfReferencingTable.class, column = "value",
            onDelete = ReferenceOption.RESTRICT, onUpdate = ReferenceOption.RESTRICT)
    private final String restrictValue;

    @Column("no_action_value")
    @ForeignKey(table = SelfReferencingTable.class, column = "value",
            onDelete = ReferenceOption.NO_ACTION, onUpdate = ReferenceOption.NO_ACTION)
    private final String noActionValue;

    @Column("none_value")
    @ForeignKey(table = SelfReferencingTable.class, column = "value")
    private final String noneValue;

    public SelfReferencingTable(int id, String value) {
        this.id = id;
        this.value = value;

        this.nullValue = null;
        this.cascadeValue = null;
        this.restrictValue = null;
        this.noActionValue = null;
        this.noneValue = null;
    }

    @RecordBuilder
    public SelfReferencingTable(@Column("id") int id,
                                @Column("value") String value,
                                @Column("null_value") String nullValue,
                                @Column("cascade_value") String cascadeValue,
                                @Column("restrict_value") String restrictValue,
                                @Column("no_action_value") String noActionValue,
                                @Column("none_value") String noneValue) {
        this.id = id;
        this.value = value;
        this.nullValue = nullValue;
        this.cascadeValue = cascadeValue;
        this.restrictValue = restrictValue;
        this.noActionValue = noActionValue;
        this.noneValue = noneValue;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
