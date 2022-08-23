package com.akvelon.client.model.request;

import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

public class Request {
    private final HashMap<String, RecordSet> parameters = new HashMap<>();

    public void addParameter(String propertyName, RecordSet recordSet) {
        parameters.put(propertyName, recordSet);
    }

    public JsonNode toJson() throws JsonProcessingException {
        return JsonMapper.readValue(toString(), JsonNode.class);
    }

    public String toString() {
        try {
            return JsonMapper.writeValueAsString(parameters);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, RecordSet> getParameters() {
        return parameters;
    }
}
