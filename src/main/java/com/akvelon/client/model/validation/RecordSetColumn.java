package com.akvelon.client.model.validation;

/**
 * A class that provides the record set column description.
 */
public final class RecordSetColumn {
    /**
     * The column name.
     */
    private final String name;
    /**
     * The column type.
     */
    private final DataType type;

    /**
     * Construct the new RecordSetColumn.
     *
     * @param name the column name.
     * @param type the column type.
     */
    public RecordSetColumn(String name, DataType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }
}
