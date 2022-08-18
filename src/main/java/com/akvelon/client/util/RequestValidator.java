package com.akvelon.client.util;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.model.validation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestValidator {
    public static void validateRequest(String method, Request requestBody, InterfaceDesc interfaceDesc) {
        ArrayList<RequestDesc> requestDescs = interfaceDesc.getMethodDescs();
        for (RequestDesc requestDesc : requestDescs) {
            ParameterDesc parameterDesc = requestDesc.getParameter();
            String name = parameterDesc.getName();
            if (method.equals(name)) {
                validateRequestSingle(requestBody, parameterDesc);
            }
        }
    }

    private static void validateRequestSingle(Request requestBody, ParameterDesc parameterDesc) {
        RecordSetDesc type = parameterDesc.getType();
        if (!type.getType().equals("dataframe")) {
            throw new IllegalArgumentException();
        }

        HashMap<String, RecordSet> parameters = requestBody.getParameters();
        if (!parameters.containsKey(type.getName())) {
            throw new IllegalArgumentException();
        }

        validateParameter(parameters.get(type.getName()), type);
    }

    private static void validateParameter(RecordSet recordSet, RecordSetDesc typeDesc) {
        validateRecordSet(recordSet, typeDesc);
    }

    private static void validateRecordSet(RecordSet recordSet, RecordSetDesc typeDesc) {
        List<Record> recordList = recordSet.getRecords();
        for (Record record : recordList) {
            for (RecordSetColumn recordSetColumn : typeDesc.getColumns()) {
                validateRecord(record, recordSetColumn);
            }
        }
    }

    private static void validateRecord(Record record, RecordSetColumn recordSetColumnDesc) {
        HashMap<String, Number> columns = record.getColumns();
        if (!columns.containsKey(recordSetColumnDesc.getName())) {
            throw new IllegalArgumentException();
        }

        validateType(columns.get(recordSetColumnDesc.getName()), recordSetColumnDesc.getType());
    }

    private static void validateType(Number number, DataType typeDesc) {
        if (typeDesc.equals(DataType.Float64)) {
            if (!(number instanceof Double)) {
                throw new IllegalArgumentException();
            }
        } else if (typeDesc.equals(DataType.Int64)) {
            if (!(number instanceof Integer)) {
                throw new IllegalArgumentException();
            }
        }
    }
}