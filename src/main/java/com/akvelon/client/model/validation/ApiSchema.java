package com.akvelon.client.model.validation;

import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

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
     * API schema version.
     */
    private final String version;

    /**
     * Constructs a new ApiSchema.
     *
     * @param requestBodySchemas the request body descriptions.
     */
    public ApiSchema(Map<String, RequestBodySchema> requestBodySchemas, String version) {
        this.requestBodySchemas = requestBodySchemas;
        this.version = version;
    }

    /**
     * @return the request body descriptions.
     */
    public Map<String, RequestBodySchema> getRequestBodySchemas() {
        return requestBodySchemas;
    }

    public String toJsonString() throws JsonProcessingException {
        return JsonMapper.writeValueAsString(this);
    }

    public JsonNode toJsonNode() throws JsonProcessingException {
        return JsonMapper.readValue(toJsonString(), JsonNode.class);
    }

    public String getVersion() {
        return version;
    }
}
