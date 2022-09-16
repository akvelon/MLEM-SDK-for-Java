package com.akvelon.client.model.validation;

import java.util.Map;

/**
 * Container(schema) for the requests bodies descriptions.
 * Schema data is used for the request validation.
 */
public final class ApiSchema {
    /**
     * The requests descriptions.
     */
    private final Map<String, RequestBodySchema> requestBodySchemas;

    /**
     * Constructs a new ApiSchema.
     *
     * @param requestBodySchemas the request body descriptions.
     */
    public ApiSchema(Map<String, RequestBodySchema> requestBodySchemas) {
        this.requestBodySchemas = requestBodySchemas;
    }

    /**
     * @return the request body descriptions.
     */
    public Map<String, RequestBodySchema> getRequestBodySchemas() {
        return requestBodySchemas;
    }
}
