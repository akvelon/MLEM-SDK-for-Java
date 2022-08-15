package com.akvelon.client.util;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.model.validation.RequestDesc;
import com.akvelon.client.model.validation.method.Arg;
import com.akvelon.client.model.validation.method.Method;
import com.akvelon.client.model.validation.method.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestValidator {
    public static void validateRequest(String method, Request requestBody, RequestDesc requestDesc) {
        Map<String, Method> methodsDesc = requestDesc.getJsonNodeMap();
        validateMethods(method, requestBody, methodsDesc);
    }

    private static void validateMethods(String method, Request requestBody, Map<String, Method> methodsDesc) {
        if (!methodsDesc.containsKey(method)) {
            throw new RuntimeException();
        }

        Method methodDesc = methodsDesc.get(method);
        validateArgs(requestBody.getRequest(), methodDesc.getArgs());
    }

    private static void validateArgs(HashMap<String, RecordSet> request, ArrayList<Arg> argsDesc) {
        Arg argDesc = argsDesc.get(0);
        if (!request.containsKey(argDesc.getName())) {
            throw new RuntimeException();
        }

        validateRecordSet(request.get(argDesc.getName()), argDesc.getType_());
    }

    private static void validateRecordSet(RecordSet recordSet, Type typeDesc) {
        if (recordSet == null) {
            throw new RuntimeException();
        }

        List<Record> recordList = recordSet.getRecords();
        validateRecordList(recordList, typeDesc.getColumns(), typeDesc.getDtypes());
    }

    private static void validateRecordList(List<Record> recordList, ArrayList<String> columns, ArrayList<String> dtypes) {
        for (int i = 0; i < columns.size(); i++) {
            validateRecord(recordList.get(0), columns.get(i), dtypes.get(i));
        }
    }

    private static void validateRecord(Record record, String column, String dtype) {
        HashMap<String, Number> columns = record.getColumns();
        if (!columns.containsKey(column)) {
            throw new RuntimeException();
        }
    }
}
