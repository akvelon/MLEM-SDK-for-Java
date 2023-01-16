package com.akvelon.client.util;

import com.akvelon.client.exception.*;
import com.akvelon.client.model.request.ArraySet;
import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;
import com.akvelon.client.model.response.Value;
import com.akvelon.client.model.validation.*;
import com.akvelon.client.resources.EM;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

/**
 * A validator that provides functionality to validate the request by given schema.
 */
public final class ApiValidator {
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
            String exceptionMessage = String.format(EM.NoMethodInApi, path);
            Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
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
        Map<String, Value> parameters = requestBody.getParameters();
        Map<String, RecordSetSchema> parameterDescMap = requestBodySchema.getParameterDescMap();
        if (parameters.size() != parameterDescMap.size()) {
            String exceptionMessage = String.format(EM.InvalidParametersCount, parameters.size(), parameterDescMap.size());
            Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
            throw new IllegalParameterNumberException(exceptionMessage);
        }

        // validate parameters by descriptions.
        for (Map.Entry<String, RecordSetSchema> entryDesc : parameterDescMap.entrySet()) {
            if (!parameters.containsKey(entryDesc.getKey())) {
                String exceptionMessage = "Actual parameters: " + parameters.keySet().toString().replaceAll("[\\[\\],]", "")
                        + ", expected: " + entryDesc.getKey();
                Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
                throw new InvalidParameterNameException(exceptionMessage);
            }

            validateValue(parameters.get(entryDesc.getKey()), entryDesc.getValue());
        }
    }

    /**
     * Validate Value object by given description.
     *
     * @param value           the object to validate.
     * @param recordSetSchema the record set description provided by schema.
     */
    private void validateValue(Value value, RecordSetSchema recordSetSchema) {
        if (value instanceof RecordSet) {
            List<Record> recordList = ((RecordSet) value).getRecords();
            for (Record record : recordList) {
                validateRecord(record, recordSetSchema);
            }
        } else if (value instanceof ArraySet) {
            JsonNode jsonNode = JsonMapper.createObjectNodeWith2DArray(((ArraySet) value).getArrays());
            validateArrayNesting(jsonNode, recordSetSchema.getColumns().get(0).getShape(), 1, recordSetSchema.getColumns().get(0).getType().type);
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
        if (recordSetSchema.getType().equals("ndarray")) {
            return;
        }

        Map<String, Number> columns = record.getColumns();
        if (columns.isEmpty()) {
            String exceptionMessage = String.format(EM.MapModelColumnsIsEmpty);
            Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
            throw new IllegalColumnsNumberException(exceptionMessage);
        }

        List<RecordSetColumnSchema> columnsDesc = recordSetSchema.getColumns();
        if (columns.size() != columnsDesc.size()) {
            String exceptionMessage = "Actual columns number: " + columns.size()
                    + ", expected: " + columnsDesc.size();
            Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
            throw new IllegalColumnsNumberException(exceptionMessage);
        }

        for (RecordSetColumnSchema recordSetColumnSchema : columnsDesc) {
            if (!columns.containsKey(recordSetColumnSchema.getName())) {
                String exceptionMessage = "Can't find: '" + recordSetColumnSchema.getName() +
                        "' property in request object, although is exists in schema: "
                        + "'" + recordSetSchema + "'";
                Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
                throw new KeyNotFoundException(exceptionMessage);
            }

            String argProperty = recordSetColumnSchema.getName();
            if (argProperty == null || argProperty.isEmpty()) {
                String exceptionMessage = EM.EmptyArgument;
                Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
                throw new EmptyArgumentException(exceptionMessage);
            }
            validateNumberType(columns.get(recordSetColumnSchema.getName()), recordSetColumnSchema.getType(), recordSetColumnSchema.getName());
        }
    }

    /**
     * Validate actual number data type by given expected type.
     * Throw exception if the specified number is not equal to type description.
     *
     * @param actualNumber the number to validate.
     * @param property     the property name.
     * @param expectedType the type description provided by schema.
     */
    private void validateNumberType(Number actualNumber, DataType expectedType, String property) {
        for (DataType dataType : DataType.values()) {
            if (!actualNumber.equals(0) && expectedType.equals(dataType)) {
                determineType(actualNumber, property, dataType);
            }
        }
    }

    /**
     * Determines if the specified actual number is assignment-compatible with the actual type
     * represented by DataType.
     *
     * @param actualNumber the number to determine.
     * @param property     the property name.
     * @param actualType   the type description provided by schema.
     */
    private void determineType(Number actualNumber, String property,
                               DataType actualType) {
        if (!(actualType.getClazz().isInstance(actualNumber))) {
            String exceptionMessage = String.format(EM.InvalidType, actualNumber, actualType)
                    + " for property '" + property + "'";

            Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
            throw new InvalidTypeException(exceptionMessage);
        }
    }

    /**
     * Validate Response object by given schema represented in ApiSchema.
     *
     * @param path      the method name for the request.
     * @param response  the Response object to validate.
     * @param apiSchema the schema represented in ApiSchema object.
     */
    public void validateResponse(String path, JsonNode response, ApiSchema apiSchema) {
        RequestBodySchema requestBodySchema = validatePathAndGetRequestBodySchema(path, apiSchema);

        validateSingleResponse(path, response, requestBodySchema.getReturnsSchema());
    }

    /**
     * Validate Response object by given response description.
     *
     * @param path       the method name for the request.
     * @param response   the Response object to validate.
     * @param returnType the response description provided by schema.
     */
    private void validateSingleResponse(String path, JsonNode response, ReturnType returnType) {
        if (response == null) {
            String exceptionMessage = String.format(EM.ReturnObjectTypeForMethodIsEmpty, path);
            Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
            throw new InvalidResponseTypeException(exceptionMessage);
        }

        String ndarray = returnType.getType();
        if (!response.isArray() && ndarray.equals("ndarray")) {
            String exceptionMessage = String.format(EM.InvalidJsonResponseFromModel, response);
            Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
            throw new InvalidResponseTypeException(exceptionMessage);
        }

        validateArrayNesting(response, returnType.getShape(), 1, returnType.getDtype());
    }

    /**
     * Validate Response array object by given shapes, nesting level and type.
     *
     * @param array        the array object to validate.
     * @param shapes       the list contains an array sizes.
     * @param nestingLevel an array dimension.
     * @param dtype        the type of element
     */
    private void validateArrayNesting(JsonNode array, List<Integer> shapes, int nestingLevel, String dtype) {
        // shapes contains one null element
        // so nothing to validate.
        if (shapes == null) {
            return;
        }

        if (shapes.size() == 1 && shapes.get(0) == null) {
            return;
        }

        for (JsonNode item : array) {
            if (item.isArray()) {
                validateArrayNesting(item, shapes, nestingLevel + 1, dtype);
                break;
            }

            if (nestingLevel != shapes.size()) {
                String exceptionMessage = String.format(EM.UnexpectedLevelOfNestingResponseData, nestingLevel, shapes.size());
                Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
                throw new IllegalArrayNestingLevel(exceptionMessage);
            }

            int shapesLastIndex = shapes.size() - 1;
            Integer lastShape = shapes.get(shapesLastIndex);
            if (lastShape != null && array.size() != lastShape) {
                String exceptionMessage = String.format(EM.PrimitiveValueUnexpectedLevel, array.size(), lastShape);
                Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
                throw new IllegalArrayLength(exceptionMessage);
            }

            validateNumberType(item.numberValue(), DataType.fromString(dtype), array.asText());
        }
    }
}