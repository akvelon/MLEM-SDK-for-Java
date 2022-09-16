package com.akvelon.client.model.validation;

import java.util.Map;

/**
 * A class that provides the request body description.
 */
public final class RequestBodySchema {
    /**
     * The request parameters description.
     */
    private final Map<String, RecordSetSchema> parameterDescMap;
    /**
     * The request return type.
     */
    private final DataType returnType;

    /**
     * Construct a new RequestDesc.
     *
     * @param parameterDescMap the request parameters description.
     * @param returnType       the request return type.
     */
    public RequestBodySchema(Map<String, RecordSetSchema> parameterDescMap, DataType returnType) {
        this.parameterDescMap = parameterDescMap;
        this.returnType = returnType;
    }

    public Map<String, RecordSetSchema> getParameterDescMap() {
        return parameterDescMap;
    }

    public DataType getReturnType() {
        return returnType;
    }
}
