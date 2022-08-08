package com.akvelon.client.model.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.List;

public class RecordSet {
    private final List<Record> records = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();

    public void addRecord(Record record) {
        records.add(record);
    }

    public JsonNode toJson(String propertyName) {
        ArrayNode arrayNode = mapper.createArrayNode();
        for (Record record : records) {
            arrayNode.add(record.toJsonNode());
        }

        return mapper.createObjectNode().set(propertyName, arrayNode);
    }
}