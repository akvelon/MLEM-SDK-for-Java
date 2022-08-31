package com.akvelon.client.util;

import com.akvelon.client.exception.*;
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
     * System.Logger instances log messages that will be routed to the underlying.
     * logging framework the LoggerFinder uses.
     */
    private final System.Logger logger;

    public RequestValidator(System.Logger logger) {
        this.logger = logger;
    }

    /**
     * Validate Request object by given schema represented in InterfaceDesc.
     * Throw exception if the specified method name is not exist.
     *
     * @param method        the method name for the request.
     * @param request       the Request object for validation.
     * @param interfaceDesc the schema represented in InterfaceDesc object.
     */
    public void validateRequest(String method, Request request, InterfaceDesc interfaceDesc) {
        assert method != null && !method.isEmpty() : "the method is null or empty";
        assert request != null : "the request is null";
        assert interfaceDesc != null : "the interfaceDesc is null";

        // get request descriptions.
        Map<String, RequestDesc> requestDescs = interfaceDesc.getRequestDescs();
        // find given request in schema
        if (!requestDescs.containsKey(method)) {
            // throw exception if the request is not exist in schema.
            String exceptionMessage = "the request " + method + " is not found in schema";
            logger.log(System.Logger.Level.ERROR, exceptionMessage);
            throw new IllegalMethodException(exceptionMessage);
        }

        // if request found in schema, validate it.
        validateSingleRequest(request, requestDescs.get(method));
    }

    /**
     * Validate Request object by given request description.
     * Throw exception if the specified parameters is not equal to the schema.
     *
     * @param request     the Request object for validation.
     * @param requestDesc the request description provided by schema.
     */
    private void validateSingleRequest(Request request, RequestDesc requestDesc) {
        // get record sets for validation.
        Map<String, RecordSet> parameters = request.getParameters();
        // get descriptions.
        Map<String, RecordSetDesc> parameterDescMap = requestDesc.getParameterDescMap();
        // check the column count.
        if (parameters.size() != parameterDescMap.size()) {
            String exceptionMessage = "Parameters number " + parameters.size()
                    + " is not equal to Parameters number in schema " + parameterDescMap.size();
            logger.log(System.Logger.Level.ERROR, exceptionMessage);
            throw new IllegalParametersNumberException(exceptionMessage);
        }

        // validate parameters by descriptions.
        for (Map.Entry<String, RecordSetDesc> entryDesc : parameterDescMap.entrySet()) {
            // throw exception, if parameter name is not exist in description
            if (!parameters.containsKey(entryDesc.getKey())) {
                String exceptionMessage = "the parameter " + entryDesc.getKey() + " is not found in the request";
                logger.log(System.Logger.Level.ERROR, exceptionMessage);
                throw new IllegalParameterException(exceptionMessage);
            }

            // validate parameter by description.
            validateRecordSet(parameters.get(entryDesc.getKey()), entryDesc.getValue());
        }
    }

    /**
     * Validate RecordSet object by given description.
     *
     * @param recordSet the RecordSet object for validation.
     * @param typeDesc  the record set description provided by schema.
     */
    private void validateRecordSet(RecordSet recordSet, RecordSetDesc typeDesc) {
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
    private void validateRecord(Record record, RecordSetDesc recordSetColumnDesc) {
        // get record columns.
        Map<String, Number> columns = record.getColumns();
        // get record columns description.
        List<RecordSetColumn> columnsDesc = recordSetColumnDesc.getColumns();
        // check the column count.
        if (columns.size() != columnsDesc.size()) {
            String exceptionMessage = "Columns count " + columns.size()
                    + " is not equal to columns count in schema " + columnsDesc.size();
            logger.log(System.Logger.Level.ERROR, exceptionMessage);
            throw new IllegalColumnsNumberException(exceptionMessage);
        }

        // check every column in the loop.
        for (RecordSetColumn recordSetColumn : columnsDesc) {
            // throw exception if given column name is not exist in schema.
            if (!columns.containsKey(recordSetColumn.getName())) {
                String exceptionMessage = "the column name " + recordSetColumn.getName() + " is not found in the request";
                logger.log(System.Logger.Level.ERROR, exceptionMessage);
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
    private void validateType(Number number, String name, DataType typeDesc) {
        // for type description Float64 the number must be Double.
        if (typeDesc.equals(DataType.Float64)) {
            // throw exception if number for Float64 is not Double
            if (!(number instanceof Double)) {
                String exceptionMessage = "the column value " + number
                        + " for name " + name + " must be " + DataType.Float64;
                logger.log(System.Logger.Level.ERROR, exceptionMessage);
                throw new IllegalRecordException(exceptionMessage);
            }
            // for type description Int64 the number must be Integer.
        } else if (typeDesc.equals(DataType.Int64)) {
            // throw exception if number for Int64 is not Integer
            if (!(number instanceof Long)) {
                String exceptionMessage = "the column value " + number
                        + " for name " + name + " must be " + DataType.Int64;
                logger.log(System.Logger.Level.ERROR, exceptionMessage);
                throw new IllegalRecordException("the column value " + number
                        + " for name " + name + " must be " + DataType.Int64);
            }
        }
    }
}