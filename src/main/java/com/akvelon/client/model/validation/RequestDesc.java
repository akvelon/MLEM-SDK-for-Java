package com.akvelon.client.model.validation;

import java.util.List;

/**
 * Class represents the request description for validation the requests objects.
 */
public class RequestDesc {
    /**
     * The request name.
     */
    private final String name;
    /**
     * The request parameters description.
     */
    private final List<ParameterDesc> parameterDescList;
    /**
     * The request return type.
     */
    private final DataType returnType;

    /**
     * Construct a new RequestDesc.
     *
     * @param name              the request name.
     * @param parameterDescList the request parameters description.
     * @param returnType        the request return type.
     */
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
