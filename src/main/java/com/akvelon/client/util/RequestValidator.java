package com.akvelon.client.util;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.model.validation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestValidator {
    public static void validateRequest(String method, Request request, InterfaceDesc interfaceDesc) {
        ArrayList<RequestDesc> requestDescs = interfaceDesc.getMethodDescs();
        for (RequestDesc requestDesc : requestDescs) {
            validateSingleRequest(method, request, requestDesc);
        }
    }

    private static void validateSingleRequest(String method, Request request, RequestDesc requestDesc) {
        ParameterDesc parameterDesc = requestDesc.getParameter();
        String name = parameterDesc.getName();
        if (!method.equals(name)) {
            return;
        }

        HashMap<String, RecordSet> parameters = request.getParameters();
        if (!parameters.containsKey(parameterDesc.getType().getName())) {
            throw new IllegalArgumentException();
        }

        validateParameter(parameters.get(parameterDesc.getType().getName()), parameterDesc);
    }

    private static void validateParameter(RecordSet recordSet, ParameterDesc parameterDesc) {
        validateRecordSet(recordSet, parameterDesc.getType());
    }

    private static void validateRecordSet(RecordSet recordSet, RecordSetDesc typeDesc) {
        List<Record> recordList = recordSet.getRecords();
        for (Record record : recordList) {
            validateRecord(record, typeDesc);
        }
    }

    private static void validateRecord(Record record, RecordSetDesc recordSetColumnDesc) {
        HashMap<String, Number> columns = record.getColumns();
        ArrayList<RecordSetColumn> columnsDesc = recordSetColumnDesc.getColumns();
        if (columns.size() != columnsDesc.size()) {
            throw new IllegalArgumentException();
        }

        for (RecordSetColumn recordSetColumn : columnsDesc) {
            if (!columns.containsKey(recordSetColumn.getName())) {
                throw new IllegalArgumentException();
            }

            validateType(columns.get(recordSetColumn.getName()), recordSetColumn.getType());
        }
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