package com.akvelon.client.model.validation;

import java.util.Map;

/**
 * Class represents the schema for validation the requests.
 */
public class InterfaceDesc {
    /**
     * The list of request descriptions.
     */
    private final Map<String, RequestDesc> requestDescs;

    /**
     * Constructs a new InterfaceDesc.
     *
     * @param requestDescs the list of request descriptions.
     */
    public InterfaceDesc(Map<String, RequestDesc> requestDescs) {
        this.requestDescs = requestDescs;
    }

    /**
     * @return the list of request descriptions.
     */
    public Map<String, RequestDesc> getRequestDescs() {
        return requestDescs;
    }
}
