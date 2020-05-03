package org.panteleyev.mysqlapi.model;

/*
 * Copyright (c) Petr Panteleyev. All rights reserved.
 * Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */

import org.panteleyev.mysqlapi.TableRecord;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.panteleyev.mysqlapi.annotations.Table;
import java.util.Objects;

@Table("table_with_json")
public class RecordWithJson implements TableRecord<Integer> {
    @PrimaryKey
    @Column(Column.ID)
    private int id;

    @Column(value = "json", isJson = true)
    private String json;

    public RecordWithJson() {
    }

    public RecordWithJson(int id, String json) {
        this.id = id;
        this.json = json;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof RecordWithJson that)) {
            return false;
        }

        return this.id == that.id
            && Objects.equals(this.json, that.json);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, json);
    }
}
