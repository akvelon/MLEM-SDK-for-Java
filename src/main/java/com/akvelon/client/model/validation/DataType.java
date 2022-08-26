package com.akvelon.client.model.validation;

/**
 * Enumeration provides available data types.
 */
public enum DataType {
    Int64("int64"), //integer type.
    Float64("float64"); //float type.

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
        // find given parameter in the enumeration.
        for (DataType dataType : values()) {
            // if data type found, return it.
            if (dataType.type.equals(type)) {
                return dataType;
            }
        }
        // throw exception, if the parameter is not exists.
        throw new IllegalArgumentException();
    }
}