package com.akvelon.client.model.validation;

import java.util.ArrayList;

public class InterfaceDesc {
    private final ArrayList<RequestDesc> requestDescs;

    public InterfaceDesc(ArrayList<RequestDesc> requestDescs) {
        this.requestDescs = requestDescs;
    }

    public ArrayList<RequestDesc> getRequestDescs() {
        return requestDescs;
    }
}
