package com.akvelon.client.util;

import com.akvelon.client.exception.*;
import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;
import com.akvelon.client.model.validation.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

/**
 * A validator that provides functionality to validate the request by given schema.
 */
public final class ApiValidator {
    /**
     * System.Logger instances log messages that will be routed to the underlying.
     * logging framework the LoggerFinder uses.
     */
    private final System.Logger logger;

    public ApiValidator(System.Logger logger) {
        this.logger = logger;
    }

    /**
     * Validate Request object by given schema represented in ApiSchema.
     * Throw exception if the specified path is not exist.
     *
     * @param path        the method name for the request.
     * @param requestBody the Request object to validate.
     * @param apiSchema   the schema represented in ApiSchema object.
     */
    public void validateRequestBody(String path, RequestBody requestBody, ApiSchema apiSchema) {
        RequestBodySchema requestBodySchema = validatePathAndGetRequestBodySchema(path, apiSchema);

        validateSingleRequestBody(requestBody, requestBodySchema);
    }

    private RequestBodySchema validatePathAndGetRequestBodySchema(String path, ApiSchema apiSchema) {
        Map<String, RequestBodySchema> requestDescriptions = apiSchema.getRequestBodySchemas();
        // find given request in schema
        if (!requestDescriptions.containsKey(path)) {
            String exceptionMessage = "The method " + path
                    + " is not found in schema; Available methods list: " + requestDescriptions.keySet() + ".";
            if (logger != null) logger.log(System.Logger.Level.ERROR, exceptionMessage);
            throw new IllegalPathException(exceptionMessage);
        }

        return requestDescriptions.get(path);
    }

    /**
     * Validate Request object by given request description.
     * Throw exception if the specified parameters is not equal to the schema.
     *
     * @param requestBody       the Request object to validate.
     * @param requestBodySchema the request description provided by schema.
     */
    private void validateSingleRequestBody(RequestBody requestBody, RequestBodySchema requestBodySchema) {
        Map<String, RecordSet> parameters = requestBody.getParameters();
        Map<String, RecordSetSchema> parameterDescMap = requestBodySchema.getParameterDescMap();
        if (parameters.size() != parameterDescMap.size()) {
            String exceptionMessage = "Actual parameters number: " + parameters.size()
                    + ", expected: " + parameterDescMap.size();
            if (logger != null) logger.log(System.Logger.Level.ERROR, exceptionMessage);
            throw new IllegalParameterNumberException(exceptionMessage);
        }

        // validate parameters by descriptions.
        for (Map.Entry<String, RecordSetSchema> entryDesc : parameterDescMap.entrySet()) {
            if (!parameters.containsKey(entryDesc.getKey())) {
                String exceptionMessage = "Actual parameters: " + parameters.keySet().toString().replaceAll("[\\[\\],]", "")
                        + ", expected: " + entryDesc.getKey();
                if (logger != null) logger.log(System.Logger.Level.ERROR, exceptionMessage);
                throw new InvalidParameterNameException(exceptionMessage);
            }

            validateRecordSet(parameters.get(entryDesc.getKey()), entryDesc.getValue());
        }
    }

    /**
     * Validate RecordSet object by given description.
     *
     * @param recordSet       the RecordSet object to validate.
     * @param recordSetSchema the record set description provided by schema.
     */
    private void validateRecordSet(RecordSet recordSet, RecordSetSchema recordSetSchema) {
        List<Record> recordList = recordSet.getRecords();
        for (Record record : recordList) {
            validateRecord(record, recordSetSchema);
        }
    }

    /**
     * Validate Record object by given schema.
     * Throw exception if the specified parameters is not equal to record schema.
     *
     * @param record          the Record object to validate.
     * @param recordSetSchema the record description provided by schema.
     */
    private void validateRecord(Record record, RecordSetSchema recordSetSchema) {
        Map<String, Number> columns = record.getColumns();
        List<RecordSetColumnSchema> columnsDesc = recordSetSchema.getColumns();
        if (columns.size() != columnsDesc.size()) {
            String exceptionMessage = "Actual columns number: " + columns.size()
                    + ", expected: " + columnsDesc.size();
            if (logger != null) logger.log(System.Logger.Level.ERROR, exceptionMessage);
            throw new IllegalColumnsNumberException(exceptionMessage);
        }

        for (RecordSetColumnSchema recordSetColumnSchema : columnsDesc) {
            if (!columns.containsKey(recordSetColumnSchema.getName())) {
                String exceptionMessage = "Can't find: '" + recordSetColumnSchema.getName() +
                        "' property in request object, although is exists in schema: "
                        + "'" + recordSetSchema + "'";
                if (logger != null) logger.log(System.Logger.Level.ERROR, exceptionMessage);
                throw new KeyNotFoundException(exceptionMessage);
            }

            validateNumberType(columns.get(recordSetColumnSchema.getName()), recordSetColumnSchema.getType(), recordSetColumnSchema.getName());
        }
    }

    /**
     * Validate data type by given schema.
     * Throw exception if the specified number is not equal to type description.
     *
     * @param actualNumber the number to validate.
     * @param property     the property name.
     * @param expectedType the type description provided by schema.
     */
    private void validateNumberType(Number actualNumber, DataType expectedType, String property) {
        for (DataType dataType : DataType.values()) {
            if (!actualNumber.equals(0) && expectedType.equals(dataType)) {
                validateType(actualNumber, property, dataType);
            }
        }
    }

    private void validateType(Number actualNumber, String property,
                              DataType actualType) {
        if (!(actualType.getClazz().isInstance(actualNumber))) {
            String exceptionMessage = "Value " + actualNumber + " for property '" + property + "' is not " +
                    "compatible with expected type - " + actualType;
            if (logger != null) logger.log(System.Logger.Level.ERROR, exceptionMessage);
            throw new InvalidTypeException(exceptionMessage);
        }
    }

    public void validateResponse(String path, JsonNode response, ApiSchema apiSchema) {
        RequestBodySchema requestBodySchema = validatePathAndGetRequestBodySchema(path, apiSchema);

        validateSingleResponse(response, requestBodySchema.getReturnsSchema());
    }

    private void validateSingleResponse(JsonNode response, ReturnType returnType) {
        if (response == null) {
            throw new InvalidResponseTypeException("There is a null value in response.");
        }

        String ndarray = returnType.getType();
        if (!response.isArray() && ndarray.equals("ndarray")) {
            throw new InvalidResponseTypeException("response is not an array: " + response);
        }

        validateArrayNesting(response, returnType.getShape(), 1, returnType.getDtype());
    }

    private void validateArrayNesting(JsonNode array, List<Integer> shapes, int nestingLevel, String dtype) {
        // shapes contains one null element
        // so nothing to validate.
        if (shapes.size() == 1 && shapes.get(0) == null) {
            return;
        }

        for (JsonNode item : array) {
            if (item.isArray()) {
                validateArrayNesting(item, shapes, nestingLevel + 1, dtype);
                break;
            }

            if (nestingLevel != shapes.size()) {
                throw new IllegalArrayNestingLevel("Unexpected level of nesting in the response data. " +
                        "Actual: " + nestingLevel + ", expected: " + shapes.size());
            }

            int shapesLastIndex = shapes.size() - 1;
            Integer lastShape = shapes.get(shapesLastIndex);
            if (lastShape != null && array.size() != lastShape) {
                throw new IllegalArrayLength("Unexpected length of the data: " + array +
                        ". Actual: " + array.size() + ", expected: " + lastShape);
            }

            validateNumberType(item.numberValue(), DataType.fromString(dtype), array.asText());
        }
    }
}