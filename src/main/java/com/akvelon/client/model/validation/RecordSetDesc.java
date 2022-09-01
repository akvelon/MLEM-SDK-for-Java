package com.akvelon.client.model.validation;

import java.util.List;

/**
 * A class that represents the record set description.
 */
public class RecordSetDesc {
    /**
     * The record set description type.
     */
    private final String type;
    /**
     * The record set description columns.
     */
    private final List<RecordSetColumn> columns;

    /**
     * Construct a new RecordSetDesc
     *
     * @param type    the record set description type.
     * @param columns the record set description columns.
     */
    public RecordSetDesc(String type, List<RecordSetColumn> columns) {
        this.type = type;
        this.columns = columns;
    }

    public String getType() {
        return type;
    }

    public List<RecordSetColumn> getColumns() {
        return columns;
    }
}