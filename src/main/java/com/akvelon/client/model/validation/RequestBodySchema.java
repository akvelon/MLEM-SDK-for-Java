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
     * The request return schema.
     */
    private final ReturnsSchema returnsSchema;

    /**
     * Construct a new RequestDesc.
     *
     * @param parameterDescMap the request parameters description.
     * @param returnsSchema    the request return schema.
     */
    public RequestBodySchema(Map<String, RecordSetSchema> parameterDescMap, ReturnsSchema returnsSchema) {
        this.parameterDescMap = parameterDescMap;
        this.returnsSchema = returnsSchema;
    }

    public Map<String, RecordSetSchema> getParameterDescMap() {
        return parameterDescMap;
    }

    public ReturnsSchema getReturnsSchema() {
        return returnsSchema;
    }
}
