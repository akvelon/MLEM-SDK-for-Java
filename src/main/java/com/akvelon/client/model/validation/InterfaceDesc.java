package com.akvelon.client.model.validation;

import java.util.ArrayList;

public class InterfaceDesc {
    private ArrayList<RequestDesc> requestDescs;

    public InterfaceDesc(ArrayList<RequestDesc> requestDescs) {
        this.requestDescs = requestDescs;
    }

    public ArrayList<RequestDesc> getMethodDescs() {
        return requestDescs;
    }

    public void setMethodDescs(ArrayList<RequestDesc> requestDescs) {
        this.requestDescs = requestDescs;
    }
}
