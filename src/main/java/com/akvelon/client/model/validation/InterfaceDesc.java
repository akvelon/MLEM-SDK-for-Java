package com.akvelon.client.model.validation;

import java.util.Map;

/**
 * Container for the application rules(schema).
 * Schema data is used for the request validation.
 */
public final class InterfaceDesc {
    /**
     * The request descriptions.
     */
    private final Map<String, RequestDesc> requestDescs;

    /**
     * Constructs a new InterfaceDesc.
     *
     * @param requestDescs the request descriptions.
     */
    public InterfaceDesc(Map<String, RequestDesc> requestDescs) {
        this.requestDescs = requestDescs;
    }

    /**
     * @return the request descriptions.
     */
    public Map<String, RequestDesc> getRequestDescs() {
        return requestDescs;
    }
}
