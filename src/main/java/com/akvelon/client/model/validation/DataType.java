package com.akvelon.client.model.validation;

public enum DataType {
    Int64("int64"),
    Float64("float64");

    public final String type;

    DataType(String type) {
        this.type = type;
    }

    public static DataType fromString(String type) {
        for (DataType dataType : values()) {
            if (dataType.type.equals(type)) {
                return dataType;
            }
        }
        throw new IllegalArgumentException();
    }
}