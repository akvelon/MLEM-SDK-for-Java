package com.akvelon.client.model.validation;

import java.util.ArrayList;

public class RequestDesc {
    private ArrayList<ParameterDesc> parameters;
    private DataType returnType;

    public RequestDesc(ArrayList<ParameterDesc> parameters, DataType returnType) {
        this.parameters = parameters;
        this.returnType = returnType;
    }

    public ArrayList<ParameterDesc> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<ParameterDesc> parameters) {
        this.parameters = parameters;
    }

    public DataType getReturnType() {
        return returnType;
    }

    public void setReturnType(DataType returnType) {
        this.returnType = returnType;
    }
}
