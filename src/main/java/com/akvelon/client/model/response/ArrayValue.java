package com.akvelon.client.model.response;

import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * A class that provides an array type of response body.
 */
public class ArrayValue<T extends Number> extends Value {
    private final List<T> list;

    public ArrayValue(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public JsonNode toJson() {
        return JsonMapper.createObjectNodeWithList(list);
    }
}
