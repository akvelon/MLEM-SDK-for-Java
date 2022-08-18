package com.akvelon.client.model.validation;

public class RequestDesc {
    private ParameterDesc parameter;
    private DataType returnType;

    public RequestDesc(ParameterDesc parameter, DataType returnType) {
        this.parameter = parameter;
        this.returnType = returnType;
    }

    public ParameterDesc getParameter() {
        return parameter;
    }

    public void setParameters(ParameterDesc parameter) {
        this.parameter = parameter;
    }

    public DataType getReturnType() {
        return returnType;
    }

    public void setReturnType(DataType returnType) {
        this.returnType = returnType;
    }
}
