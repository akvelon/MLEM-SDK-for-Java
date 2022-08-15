package com.akvelon.client.model.validation;

import com.akvelon.client.model.validation.method.Method;

import java.util.Map;

public class RequestDesc {
    private Map<String, Method> jsonNodeMap;

    public RequestDesc(Map<String, Method> jsonNodeMap) {
        this.jsonNodeMap = jsonNodeMap;
    }

    public Map<String, Method> getJsonNodeMap() {
        return jsonNodeMap;
    }

    public void setJsonNodeMap(Map<String, Method> jsonNodeMap) {
        this.jsonNodeMap = jsonNodeMap;
    }
}
