package com.akvelon.client.model.validation;

import com.akvelon.client.model.common.DataType;

import java.util.List;

/**
 * A class that provides the record set column description.
 */
public final class RecordSetColumnSchema {
    /**
     * The column type.
     */
    private final DataType type;
    /**
     * The column name.
     */
    private String name;
    /**
     * Shapes list.
     */
    private List<Integer> shape;

    /**
     * Construct the new RecordSetColumn.
     *
     * @param name the column name.
     * @param type the column type.
     */
    public RecordSetColumnSchema(String name, DataType type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Construct the new RecordSetColumn.
     *
     * @param type the column type.
     */
    public RecordSetColumnSchema(DataType type) {
        this.type = type;
    }

    public RecordSetColumnSchema(DataType type, List<Integer> shape) {
        this.type = type;
        this.shape = shape;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }

    public List<Integer> getShape() {
        return shape;
    }
}