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

    public void setName(String name) {
        this.name = name;
    }

    public List<ParameterDesc> getParameterDescList() {
        return parameterDescList;
    }

    public void setParameterDescList(List<ParameterDesc> parameterDescList) {
        this.parameterDescList = parameterDescList;
    }

    public DataType getReturnType() {
        return returnType;
    }

    public void setReturnType(DataType returnType) {
        this.returnType = returnType;
    }
}
