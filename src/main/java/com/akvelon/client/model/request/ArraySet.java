package com.akvelon.client.model.request;

import com.akvelon.client.model.common.Value;
import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * A class that provides the set of arrays.
 */
public class ArraySet<T extends Number> extends Value {
    private final T[][] arrays;

    public ArraySet(T[][] arrays) {
        this.arrays = arrays;
    }

    public T[][] getArrays() {
        return arrays;
    }

    @Override
    public JsonNode toJson() {
        return JsonMapper.createObjectNodeWith2DArray(arrays);
    }
}
