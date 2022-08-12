package com.akvelon.client.model.validation;

public class RecordSetColumn {
    private String name;
    private DataType type;

    public RecordSetColumn() {
    }

    public RecordSetColumn(String name, DataType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }
}
