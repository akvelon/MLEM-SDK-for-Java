package com.akvelon.client.model.validation;

import java.util.List;

public class ReturnsSchema {
    private final List<Integer> shape;
    private final String dtype;
    private final String type;

    public ReturnsSchema(List<Integer> shape, String dtype, String type) {
        this.shape = shape;
        this.dtype = dtype;
        this.type = type;
    }

    public List<Integer> getShape() {
        return shape;
    }

    public String getDtype() {
        return dtype;
    }

    public String getType() {
        return type;
    }
}
