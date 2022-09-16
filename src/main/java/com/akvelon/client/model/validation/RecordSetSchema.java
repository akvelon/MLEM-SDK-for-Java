package com.akvelon.client.model.validation;

import java.util.List;

/**
 * A class that represents the record set description.
 */
public final class RecordSetSchema {
    /**
     * The record set description type.
     */
    private final String type;
    /**
     * The record set description columns.
     */
    private final List<RecordSetColumnSchema> columns;

    /**
     * Construct a new RecordSetDesc.
     *
     * @param type    the record set description type.
     * @param columns the record set description columns.
     */
    public RecordSetSchema(String type, List<RecordSetColumnSchema> columns) {
        this.type = type;
        this.columns = columns;
    }

    public String getType() {
        return type;
    }

    public List<RecordSetColumnSchema> getColumns() {
        return columns;
    }
}