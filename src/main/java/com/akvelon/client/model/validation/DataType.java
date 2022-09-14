package com.akvelon.client.model.validation;

/**
 * An enumeration that provides available data types.
 */
public enum DataType {
    Int64("int64"),
    Float64("float64");

    public final String type;

    /**
     * Constructs a new DataType.
     *
     * @param type the representation of DataType.
     */
    DataType(String type) {
        this.type = type;
    }

    /**
     * Get DataType object by given string.
     *
     * @param type the string representation of DataType.
     * @return DataType object.
     */
    public static DataType fromString(String type) {
        for (DataType dataType : values()) {
            if (dataType.type.equals(type)) {
                return dataType;
            }
        }
        // throw exception, if the parameter is not exists.
        throw new IllegalArgumentException();
    }
}