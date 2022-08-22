package com.akvelon.client.model.validation;

import java.util.List;

public class RequestDesc {
    private String name;
    private List<ParameterDesc> parameterDescList;
    private DataType returnType;

    public RequestDesc(String name, List<ParameterDesc> parameterDescList, DataType returnType) {
        this.name = name;
        this.parameterDescList = parameterDescList;
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public List<ParameterDesc> getParameterDescList() {
        return parameterDescList;
    }

    public DataType getReturnType() {
        return returnType;
    }
}
