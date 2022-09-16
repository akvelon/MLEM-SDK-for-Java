package com.akvelon.client.model.request.typical;

/**
 * Iris property represent Mlem RegModel model property.
 * The example of request with RegModel properties:
 * {
 *   "data": {
 *     "values": [
 *       {
 *         "0": 0
 *       }
 *     ]
 *   }
 * }
 */
public enum RegModelProperty {
    VALUE("0");

    public final String property;

    RegModelProperty(String property) {
        this.property = property;
    }
}
