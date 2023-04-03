package com.akvelon.client.model.common;

import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.resources.EM;

import java.util.List;

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
    Str("str", String.class),
    Ndarray("ndarray", List.class, "import java.util.List;"),
    Dataframe("dataframe", RecordSet.class, "import com.akvelon.client.model.request.RecordSet;"),
    List("list", ArrayValue.class, "import com.akvelon.client.model.common.ArrayValue;"),
    Primitive("primitive", Primitive.class, "import com.akvelon.client.model.common.Primitive;"),
    Torch("torch", ArrayValue.class, "import com.akvelon.client.model.common.ArrayValue;");

    public final String type;
    private final Class clazz;
    private String importString;

    /**
     * Constructs a new DataType.
     *
     * @param type  the representation of DataType.
     * @param clazz the Java class
     */
    DataType(String type, Class clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    /**
     * Constructs a new DataType.
     *
     * @param type         the representation of DataType.
     * @param clazz        the Java class
     * @param importString import of the Class
     */
    DataType(String type, Class clazz, String importString) {
        this.type = type;
        this.clazz = clazz;
        this.importString = importString;
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
        throw new IllegalArgumentException(String.format(EM.NoValidationLogic, type));
    }

    public Class getClazz() {
        return clazz;
    }

    public String getImportString() {
        return importString;
    }
}