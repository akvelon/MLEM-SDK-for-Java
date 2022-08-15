package com.akvelon.client.model.validation;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public class RequestDesc {
    private Map<String, JsonNode> jsonNodeMap;

    public RequestDesc(Map<String, JsonNode> jsonNodeMap) {
        this.jsonNodeMap = jsonNodeMap;
    }

    public Map<String, JsonNode> getJsonNodeMap() {
        return jsonNodeMap;
    }

    public void setJsonNodeMap(Map<String, JsonNode> jsonNodeMap) {
        this.jsonNodeMap = jsonNodeMap;
    }
}
