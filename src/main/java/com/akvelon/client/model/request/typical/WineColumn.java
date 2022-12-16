package com.akvelon.client.model.request.typical;

public class WineColumn {
    private final WineProperty property;
    private final Double value;

    public WineColumn(WineProperty property, Double value) {
        this.property = property;
        this.value = value;
    }

    public WineProperty getProperty() {
        return property;
    }

    public Double getValue() {
        return value;
    }
}