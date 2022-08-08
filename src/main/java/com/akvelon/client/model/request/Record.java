package com.akvelon.client.model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class Record {
    private final HashMap<String, Number> columns = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    public void addColumn(String name, Number value) {
        columns.put(name, value);
    }

    public String toString() {
        try {
            return mapper.writeValueAsString(columns);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode toJsonNode() {
        return stringToJsonNode(toString());
    }

    private JsonNode stringToJsonNode(String json) {
        try {
            return mapper.readValue(json, JsonNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}