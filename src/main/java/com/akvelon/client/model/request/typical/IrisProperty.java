package com.akvelon.client.model.request.typical;

/**
 * Iris property represent Mlem Iris model property.
 * The example of request with Iris properties:
 * {
 *   "data": {
 *     "values": [
 *       {
 *         "sepal length (cm)": 0,
 *         "sepal width (cm)": 0,
 *         "petal length (cm)": 0,
 *         "petal width (cm)": 0
 *       }
 *     ]
 *   }
 * }
 */
public enum IrisProperty {
    SEPAL_LENGTH("sepal length (cm)"),
    PETAL_LENGTH("petal length (cm)"),
    SEPAL_WIDTH("sepal width (cm)"),
    PETAL_WIDTH("petal width (cm)");

    public final String property;

    IrisProperty(String property) {
        this.property = property;
    }
}
