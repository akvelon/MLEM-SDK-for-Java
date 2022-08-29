package com.akvelon.client.util;

import com.akvelon.client.exception.IllegalColumnSizeException;
import com.akvelon.client.exception.IllegalMethodException;
import com.akvelon.client.exception.IllegalParameterException;
import com.akvelon.client.exception.IllegalRecordException;
import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.model.validation.*;

import java.util.List;
import java.util.Map;

/**
 * Provides functionality for validation the request by given schema.
 */
public class RequestValidator {
    /**
     * Validate Request object by given schema represented in InterfaceDesc.
     * Throw exception if the specified method name is not exist.
     *
     * @param method        the method name for the request.
     * @param request       the Request object for validation.
     * @param interfaceDesc the schema represented in InterfaceDesc object.
     */
    public static void validateRequest(String method, Request request, InterfaceDesc interfaceDesc) {
        assert method != null && !method.isEmpty() : "the method is null or empty";
        assert request != null : "the request is null";
        assert interfaceDesc != null : "the interfaceDesc is null";

        // get request descriptions.
        List<RequestDesc> requestDescs = interfaceDesc.getRequestDescs();
        boolean isMethodNameExist = false;
        // find given request in schema by name and validate it.
        for (RequestDesc requestDesc : requestDescs) {
            // get method name in the description.
            String name = requestDesc.getName();
            // if request found in schema, validate it.
            if (method.equals(name)) {
                isMethodNameExist = true;
                validateSingleRequest(request, requestDesc);
            }
        }

        // throw exception if the request is not exist in schema.
        if (!isMethodNameExist) {
            throw new IllegalMethodException("the request " + method + " is not found in schema");
        }
    }

    /**
     * Validate Request object by given request description.
     * Throw exception if the specified parameters is not equal to the schema.
     *
     * @param request     the Request object for validation.
     * @param requestDesc the request description provided by schema.
     */
    private static void validateSingleRequest(Request request, RequestDesc requestDesc) {
        // get descriptions.
        List<ParameterDesc> parameterDescList = requestDesc.getParameterDescList();
        // validate parameters by descriptions.
        for (ParameterDesc parameterDesc : parameterDescList) {
            // get record sets for validation.
            Map<String, RecordSet> parameters = request.getParameters();
            // throw exception, if parameter name is not exist in description
            if (!parameters.containsKey(parameterDesc.getName())) {
                throw new IllegalParameterException("the parameter " + parameterDesc.getName() + " is not found in the request");
            }

            // validate parameter by description.
            validateParameter(parameters.get(parameterDesc.getName()), parameterDesc);
        }
    }

    /**
     * Validate RecordSet object by given parameter description.
     *
     * @param recordSet     the RecordSet object for validation.
     * @param parameterDesc the parameter description provided by schema.
     */
    private static void validateParameter(RecordSet recordSet, ParameterDesc parameterDesc) {
        validateRecordSet(recordSet, parameterDesc.getType());
    }

    /**
     * Validate RecordSet object by given description.
     *
     * @param recordSet the RecordSet object for validation.
     * @param typeDesc  the record set description provided by schema.
     */
    private static void validateRecordSet(RecordSet recordSet, RecordSetDesc typeDesc) {
        // get records list.
        List<Record> recordList = recordSet.getRecords();
        // validate the records.
        for (Record record : recordList) {
            validateRecord(record, typeDesc);
        }
    }

    /**
     * Validate Record object by given schema.
     * Throw exception if the specified parameters is not equal to record schema.
     *
     * @param record              the Record object for validation.
     * @param recordSetColumnDesc the record description provided by schema.
     */
    private static void validateRecord(Record record, RecordSetDesc recordSetColumnDesc) {
        // get record columns.
        Map<String, Number> columns = record.getColumns();
        // get record columns description.
        List<RecordSetColumn> columnsDesc = recordSetColumnDesc.getColumns();
        // check the column count.
        if (columns.size() != columnsDesc.size()) {
            throw new IllegalColumnSizeException("Columns count " + columns.size()
                    + " is not equal to columns count in schema " + columnsDesc.size());
        }

        // check every column in the loop.
        for (RecordSetColumn recordSetColumn : columnsDesc) {
            // throw exception if given column name is not exist in schema.
            if (!columns.containsKey(recordSetColumn.getName())) {
                throw new IllegalRecordException("the column name " + recordSetColumn.getName() + " is not found in the request");
            }

            // validate record by schema
            validateType(columns.get(recordSetColumn.getName()), recordSetColumn.getName(), recordSetColumn.getType());
        }
    }

    /**
     * Validate data type by given schema.
     * Throw exception if the specified number is not equal to type description.
     *
     * @param number   the number for validation.
     * @param typeDesc the type description provided by schema.
     */
    private static void validateType(Number number, String name, DataType typeDesc) {
        // for type description Float64 the number must be Double.
        if (typeDesc.equals(DataType.Float64)) {
            // throw exception if number for Float64 is not Double
            if (!(number instanceof Double)) {
                throw new IllegalRecordException("the column value " + number
                        + " for name " + name + " must be " + DataType.Float64);
            }
            // for type description Int64 the number must be Integer.
        } else if (typeDesc.equals(DataType.Int64)) {
            // throw exception if number for Int64 is not Integer
            if (!(number instanceof Long)) {
                throw new IllegalRecordException("the column value " + number
                        + " for name " + name + " must be " + DataType.Int64);
            }
        }
    }
}