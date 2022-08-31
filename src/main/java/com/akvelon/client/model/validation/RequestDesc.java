package com.akvelon.client.model.validation;

import java.util.Map;

/**
 * Class represents the request description for validation the requests objects.
 */
public class RequestDesc {
    /**
     * The request name.
     */
/*
    private final String name;
*/
    /**
     * The request parameters description.
     */
    private final Map<String, RecordSetDesc> parameterDescMap;
    /**
     * The request return type.
     */
    private final DataType returnType;

    /**
     * Construct a new RequestDesc.
     *
     * @param //name           the request name.
     * @param parameterDescMap the request parameters description.
     * @param returnType       the request return type.
     */
    public RequestDesc(/*String name, */Map<String, RecordSetDesc> parameterDescMap, DataType returnType) {
/*
        this.name = name;
*/
        this.parameterDescMap = parameterDescMap;
        this.returnType = returnType;
    }
/*
    public String getName() {
        return name;
    }*/

    public Map<String, RecordSetDesc> getParameterDescMap() {
        return parameterDescMap;
    }

    public DataType getReturnType() {
        return returnType;
    }
}
