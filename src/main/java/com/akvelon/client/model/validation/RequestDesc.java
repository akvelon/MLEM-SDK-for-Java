package com.akvelon.client.model.validation;

import java.util.Map;

/**
 * A class that provides the request description.
 */
public class RequestDesc {
    /**
     * The request parameters description.
     */
    private final Map<String, RecordSetDesc> parameterDescMap;
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
    public RequestDesc(Map<String, RecordSetDesc> parameterDescMap, DataType returnType) {
        this.parameterDescMap = parameterDescMap;
        this.returnType = returnType;
    }

    public Map<String, RecordSetDesc> getParameterDescMap() {
        return parameterDescMap;
    }

    public DataType getReturnType() {
        return returnType;
    }
}
