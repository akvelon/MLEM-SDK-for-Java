package com.akvelon.client.model.validation;

import java.util.ArrayList;

public class RequestDesc {
    private ArrayList<RecordSetColumn> parameters;
    private DataType returnType;

    public RequestDesc(ArrayList<RecordSetColumn> parameters, DataType returnType) {
        this.parameters = parameters;
        this.returnType = returnType;
    }

    public ArrayList<RecordSetColumn> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<RecordSetColumn> parameters) {
        this.parameters = parameters;
    }

    public DataType getReturnType() {
        return returnType;
    }

    public void setReturnType(DataType returnType) {
        this.returnType = returnType;
    }
}
