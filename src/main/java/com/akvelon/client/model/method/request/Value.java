package com.akvelon.client.model.method.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Value {
    @JsonProperty("sepal length (cm)")
    private int sepalLengthCm;
    @JsonProperty("sepal width (cm)")
    private int sepalWidthCm;
    @JsonProperty("petal length (cm)")
    private int petalLengthCm;
    @JsonProperty("petal width (cm)")
    private int petalWidthCm;

    public Value(int sepalLengthCm, int sepalWidthCm, int petalLengthCm, int petalWidthCm) {
        this.sepalLengthCm = sepalLengthCm;
        this.sepalWidthCm = sepalWidthCm;
        this.petalLengthCm = petalLengthCm;
        this.petalWidthCm = petalWidthCm;
    }

    public Value() {
    }

    public int getSepalLengthCm() {
        return sepalLengthCm;
    }

    public void setSepalLengthCm(int sepalLengthCm) {
        this.sepalLengthCm = sepalLengthCm;
    }

    public int getSepalWidthCm() {
        return sepalWidthCm;
    }

    public void setSepalWidthCm(int sepalWidthCm) {
        this.sepalWidthCm = sepalWidthCm;
    }

    public int getPetalLengthCm() {
        return petalLengthCm;
    }

    public void setPetalLengthCm(int petalLengthCm) {
        this.petalLengthCm = petalLengthCm;
    }

    public int getPetalWidthCm() {
        return petalWidthCm;
    }

    public void setPetalWidthCm(int petalWidthCm) {
        this.petalWidthCm = petalWidthCm;
    }
}
