package com.akvelon.client.model.validation;

/**
 * An enumeration that provides available data types.
 */
public enum DataType {
    Int64("int64", Long.class),
    Float64("float64", Double.class),
    Float32("float32", Float.class),
    Int8("int8", Byte.class),
    Int16("int16", Short.class),
    Int32("int32", Integer.class),
    Uint8("uint8", Byte.class),
    Uint16("uint16", Short.class),
    Uint32("uint32", Integer.class),
    Uint64("uint64", Long.class),
    Bool("bool", Boolean.class),
    Str("primitive", String.class);

    public final String type;
    private final Class clazz;

    /**
     * Constructs a new DataType.
     *
     * @param type the representation of DataType.
     */
    DataType(String type, Class clazz) {
        this.type = type;
        this.clazz = clazz;
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
        throw new IllegalArgumentException("Unknown value type in response " + type);
    }

    public Class getClazz() {
        return clazz;
    }
}