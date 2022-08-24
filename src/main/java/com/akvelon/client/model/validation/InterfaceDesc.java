package com.akvelon.client.model.validation;

import java.util.List;

/**
 * Class represents the schema for validation the requests.
 */
public class InterfaceDesc {
    /**
     * The list of request descriptions.
     */
    private final List<RequestDesc> requestDescs;

    /**
     * Constructs a new InterfaceDesc
     *
     * @param requestDescs the list of request descriptions.
     */
    public InterfaceDesc(List<RequestDesc> requestDescs) {
        this.requestDescs = requestDescs;
    }

    /**
     * @return the list of request descriptions.
     */
    public List<RequestDesc> getRequestDescs() {
        return requestDescs;
    }
}
