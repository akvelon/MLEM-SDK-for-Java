package com.akvelon.client.model.request;

import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

public class Request {
    private final HashMap<String, RecordSet> request = new HashMap<>();

    public void addRecordSet(String propertyName, RecordSet recordSet) {
        request.put(propertyName, recordSet);
    }

    public JsonNode toJson() {
        return JsonMapper.stringToObject(toString(), JsonNode.class);
    }

    public String toString() {
        return JsonMapper.writeValueAsString(request);
    }
}