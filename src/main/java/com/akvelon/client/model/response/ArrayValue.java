package com.akvelon.client.model.response;

import java.util.List;

/**
 * A class that provides an array type of response body.
 */
public class ArrayValue<T> extends Value {
    private final List<T> list;

    public ArrayValue(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }
}
