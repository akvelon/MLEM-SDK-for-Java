package com.akvelon.client.model.validation;

import java.util.List;

/**
 * A class that provides the return type description.
 */
public class ReturnType {
    private final String dtype;
    private final String type;
    private List<Integer> shape;

    public ReturnType(List<Integer> shape, String dtype, String type) {
        this.shape = shape;
        this.dtype = dtype;
        this.type = type;
    }

    public ReturnType(String dtype, String type) {
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
