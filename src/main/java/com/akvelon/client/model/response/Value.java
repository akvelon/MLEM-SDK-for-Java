package com.akvelon.client.model.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A class that provides a value abstraction.
 */
public abstract class Value {
    public abstract JsonNode toJson();
}
