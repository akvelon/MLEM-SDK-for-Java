package com.akvelon.client.model.validation;

import java.util.ArrayList;

public class RequestDesc {
    private ArrayList<MethodDesc> methodDescs;

    public RequestDesc(ArrayList<MethodDesc> methodDescs) {
        this.methodDescs = methodDescs;
    }

    public ArrayList<MethodDesc> getMethodDescs() {
        return methodDescs;
    }

    public void setMethodDescs(ArrayList<MethodDesc> methodDescs) {
        this.methodDescs = methodDescs;
    }
}
