package com.akvelon.client.util;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.model.validation.MethodDesc;
import com.akvelon.client.model.validation.ParameterDesc;
import com.akvelon.client.model.validation.RecordSetColumn;
import com.akvelon.client.model.validation.RecordSetColumnsDesc;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestValidator {
    public static void validateRequest(String method, Request requestBody, JsonNode schema) throws IOException {
        ArrayList<MethodDesc> parameterDescs = RequestParser.toRequestDesc(schema).getMethodDescs();
        ParameterDesc parameterDesc = getParameterDescOrThrowException(method, parameterDescs);

        RecordSetColumnsDesc recordSetColumnsDesc = parameterDesc.getType();
        RecordSet recordSet = getRecordSetOrThrowException(requestBody, recordSetColumnsDesc);
        validateRecords(recordSet.getRecords(), recordSetColumnsDesc.getColumns());
    }

    private static ParameterDesc getParameterDescOrThrowException(String method, ArrayList<MethodDesc> parameters) {
        for (MethodDesc methodDesc : parameters) {
            ParameterDesc parameterDesc = methodDesc.getParameter();
            String name = parameterDesc.getName();
            if (method.equals(name)) {
                return parameterDesc;
            }
        }

        throw new IllegalArgumentException();
    }

    private static RecordSet getRecordSetOrThrowException(Request requestBody, RecordSetColumnsDesc type) {
        HashMap<String, RecordSet> requestMap = requestBody.getRequest();
        if (!requestMap.containsKey(type.getName())) {
            throw new IllegalArgumentException();
        }

        return requestMap.get(type.getName());
    }

    private static void validateRecords(List<Record> recordList, ArrayList<RecordSetColumn> columns) {
        for (Record record : recordList) {
            HashMap<String, Number> columnsMap = record.getColumns();
            for (RecordSetColumn recordSetColumn : columns) {
                if (!columnsMap.containsKey(recordSetColumn.getName())) {
                    throw new IllegalArgumentException();
                }

                // TODO: 8/17/2022 Check type
            }
        }
    }
}