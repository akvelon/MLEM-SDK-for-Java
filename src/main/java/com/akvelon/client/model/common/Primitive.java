package com.akvelon.client.model.common;

import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class Primitive extends Value {
    private final String property;
    private final String data;

    public Primitive(String property, String data) {
        this.property = property;
        this.data = data;
    }

    @Override
    public JsonNode toJson() {
        return JsonMapper.createPrimitive(property, data);
    }
}