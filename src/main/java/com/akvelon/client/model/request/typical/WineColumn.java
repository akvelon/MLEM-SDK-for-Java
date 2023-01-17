package com.akvelon.client.model.request.typical;

/**
 * A class that provides the pair property-value for Wine model.
 */
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