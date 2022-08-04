package com.akvelon.client.model.method.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class PredictRequest {
    @JsonProperty("data")
    private ArrayList<Value> values;

    public PredictRequest(ArrayList<Value> values) {
        this.values = values;
    }

    public PredictRequest() {
    }

    public ArrayList<Value> getValues() {
        return values;
    }

    public void setValues(ArrayList<Value> values) {
        this.values = values;
    }
}
