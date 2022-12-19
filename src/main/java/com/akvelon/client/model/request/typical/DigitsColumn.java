package com.akvelon.client.model.request.typical;

/**
 * A class that provides the pair property-value for Digits model.
 */
public class DigitsColumn {
    private final DigitsProperty property;
    private final Double value;

    public DigitsColumn(DigitsProperty property, Double value) {
        this.property = property;
        this.value = value;
    }

    public DigitsProperty getProperty() {
        return property;
    }

    public Double getValue() {
        return value;
    }
}


