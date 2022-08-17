package com.akvelon.client.util;

import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.model.validation.ParameterDesc;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RequestValidator {
    public static void validateRequest(String method, Request requestBody, JsonNode schema) throws IOException {
        ArrayList<ParameterDesc> parameters = RequestParser.toRequestDesc(schema).getParameters();
        HashMap<String, RecordSet> request = requestBody.getRequest();
        for (ParameterDesc parameterDesc : parameters) {
            String name = parameterDesc.getName();
            if (method.equals(name)) {
                RecordSet recordSet = request.get(name);
                break;
            }
        }
    }
}