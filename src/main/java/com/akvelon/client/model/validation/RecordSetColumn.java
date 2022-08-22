package com.akvelon.client.model.validation;

public class RecordSetColumn {
    private String name;
    private DataType type;

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
