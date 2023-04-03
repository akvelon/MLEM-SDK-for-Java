package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.ArraySet;
import com.akvelon.client.model.request.RequestBody;

/**
 * A class that provides the Digits request parameters  as an example request.
 */
public class DigitsBody<T extends Number> extends RequestBody {
    public DigitsBody(String property, T[][] arrays) {
        ArraySet<T> arraySet = new ArraySet<>(arrays);
        setParameter(property, arraySet);
    }
}