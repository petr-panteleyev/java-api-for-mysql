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
import java.util.UUID;

@Table("table_with_uuid")
public class RecordWithUuid implements TableRecord<Integer> {
    @PrimaryKey
    @Column(Column.ID)
    private int id;

    @Column(value = "uuid")
    private UUID uuid;
    @Column(value = "uuidBinary", storeUuidAsBinary = true)
    private UUID uuidBin;

    public RecordWithUuid() {
    }

    public RecordWithUuid(int id, UUID uuid, UUID uuidBin) {
        this.id = id;
        this.uuid = uuid;
        this.uuidBin = uuidBin;
    }

    public int getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getUuidBin() {
        return uuidBin;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof RecordWithUuid that)) {
            return false;
        }

        return this.id == that.id
            && Objects.equals(this.uuid, that.uuid)
            && Objects.equals(this.uuidBin, that.uuidBin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, uuidBin);
    }
}
