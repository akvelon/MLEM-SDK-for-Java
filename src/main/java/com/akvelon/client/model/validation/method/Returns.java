package com.akvelon.client.model.validation.method;

import java.util.ArrayList;

public class Returns {
    private ArrayList<String> shape;
    private String dtype;
    private String type;

    public Returns() {
    }

    public ArrayList<String> getShape() {
        return shape;
    }

    public void setShape(ArrayList<String> shape) {
        this.shape = shape;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
