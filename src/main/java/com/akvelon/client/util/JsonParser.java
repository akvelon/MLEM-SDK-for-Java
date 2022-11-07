package com.akvelon.client.util;

import com.akvelon.client.exception.IllegalColumnsNumberException;
import com.akvelon.client.exception.InvalidArgsTypeException;
import com.akvelon.client.exception.InvalidRecordSetTypeException;
import com.akvelon.client.exception.InvalidValuesException;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;
import com.akvelon.client.model.validation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;

import java.io.IOException;
import java.util.*;

/**
 * A parser that provides functionality for deserialization JSON schema to descriptions objects.
 */
public final class JsonParser {
    /**
     * Method to deserialize a ApiSchema object from given JSON schema.
     *
     * @param schema the JsonNode representation of ApiSchema.
     * @return the ApiSchema object of the conversion.
     * @throws IOException signals that an I/O exception has occurred
     *                     or an illegal recordSetType has occurred or args is not array.
     */
    public ApiSchema parseApiSchema(JsonNode schema) throws IOException {
        Map<String, JsonNode> jsonNodeMap = JsonMapper.readMap(schema.get("methods"));
        Map<String, RequestBodySchema> methods = parseMethods(jsonNodeMap);

        return new ApiSchema(methods);
    }

    /**
     * Method to deserialize a list of RequestDesc object from given JSON schema.
     *
     * @param jsonNodeMap a map with string-json representation of RequestDesc list.
     * @return the mapped objects of the conversion.
     * @throws IOException signals that an illegal recordSetType has occurred or args is not array.
     */
    private Map<String, RequestBodySchema> parseMethods(Map<String, JsonNode> jsonNodeMap) throws IOException {
        Map<String, RequestBodySchema> parameters = new HashMap<>();

        for (Map.Entry<String, JsonNode> entry : jsonNodeMap.entrySet()) {
            RequestBodySchema parameterDesc = parseRequestBodySchema(entry);
            parameters.put(entry.getKey(), parameterDesc);
        }

        return parameters;
    }

    /**
     * Method to deserialize RequestDesc object from given JSON schema.
     *
     * @param entry a collection-view of the map with arguments description.
     * @return the RequestDesc object of the conversion.
     * @throws IOException signals that an illegal recordSetType has occurred or args is not array.
     */
    private RequestBodySchema parseRequestBodySchema(Map.Entry<String, JsonNode> entry) throws IOException {
        JsonNode args = entry.getValue().get("args");

        if (!args.isArray()) {
            String exceptionMessage = "args is not array: " + args;
            Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
            throw new InvalidArgsTypeException(exceptionMessage);
        }

        Map<String, RecordSetSchema> parameterDescMap = new HashMap<>();

        for (JsonNode arg : args) {
            RecordSetSchema recordSetSchema = parseRecordSetSchema(arg.get("type_"));
            parameterDescMap.put(arg.get("name").asText(), recordSetSchema);
        }

        ReturnType returnType = parseReturnsSchema(entry.getValue().get("returns"));

        return new RequestBodySchema(parameterDescMap, returnType);
    }

    /**
     * Method to deserialize RecordSetDesc object from given JSON schema.
     *
     * @param type_ the JsonNode representation of RecordSetDesc.
     * @return the RecordSetDesc object of the conversion.
     * @throws IOException signals that an illegal recordSetType has occurred.
     */
    private RecordSetSchema parseRecordSetSchema(JsonNode type_) throws IOException {
        String recordSetDescType = type_.get("type").asText();

        if (recordSetDescType.equals("dataframe")) {
            JsonNode columns = type_.get("columns");
            JsonNode dtypes = type_.get("dtypes");

            List<String> names = JsonMapper.readList(columns);
            List<String> dTypes = JsonMapper.readList(dtypes);
            if (names.size() != dTypes.size()) {
                String exceptionMessage = "Columns size must be equal to dtypes. " +
                        "Actual columns size: " + names.size() + ", actual dtypes size: " + dTypes.size() + ".";
                Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
                throw new IllegalColumnsNumberException(exceptionMessage);
            }

            ArrayList<RecordSetColumnSchema> recordSetColumnSchemas = new ArrayList<>();

            for (int i = 0; i < names.size(); i++) {
                recordSetColumnSchemas.add(new RecordSetColumnSchema(names.get(i), DataType.fromString(dTypes.get(i))));
            }

            return new RecordSetSchema(recordSetDescType, recordSetColumnSchemas);
        }

        if (recordSetDescType.equals("ndarray")) {
            JsonNode shapes = type_.get("shape");
            if (!shapes.isArray()) {
                String exceptionMessage = "shapes is not array: " + shapes;
                Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
                throw new InvalidRecordSetTypeException(exceptionMessage);
            }

            List<Integer> shapesList = new ArrayList<>();
            for (JsonNode shape : shapes) {
                if (shape instanceof NullNode) {
                    shapesList.add(null);
                    continue;
                }

                shapesList.add(shape.intValue());
            }

            String dtype = type_.get("dtype").asText();

            List<RecordSetColumnSchema> recordSetColumnSchemas
                    = Collections.singletonList(new RecordSetColumnSchema(DataType.fromString(dtype), shapesList));
            return new RecordSetSchema(recordSetDescType, recordSetColumnSchemas);
        }

        if (recordSetDescType.equals("list")) {
            JsonNode items = type_.get("items");
            if (!items.isArray()) {
                String exceptionMessage = "items is not array: " + items;
                Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
                throw new InvalidRecordSetTypeException(exceptionMessage);
            }

            List<RecordSetColumnSchema> recordSetColumnSchemas = new ArrayList<>();
            for (JsonNode item : items) {
                String ptype = item.get("ptype").asText();
                recordSetColumnSchemas.add(new RecordSetColumnSchema(DataType.fromString(ptype)));
            }

            return new RecordSetSchema(recordSetDescType, recordSetColumnSchemas);
        }

        String exceptionMessage = "RecordSet type is not dataframe or list: " + recordSetDescType;
        Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
        throw new InvalidRecordSetTypeException(exceptionMessage);
    }

    /**
     * Method to deserialize ReturnType object from given JSON schema.
     *
     * @param returnsJsonNode the JsonNode representation of ReturnType.
     * @return the ReturnType object of the conversion.
     */
    private ReturnType parseReturnsSchema(JsonNode returnsJsonNode) {
        JsonNode shapes = returnsJsonNode.get("shape");
        if (shapes == null) {
            return new ReturnType(returnsJsonNode.get("ptype").asText(), returnsJsonNode.get("type").asText());
        }

        if (!(shapes instanceof ArrayNode)) {
            String exceptionMessage = "shape is not array: " + returnsJsonNode;
            Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
            throw new InvalidArgsTypeException(exceptionMessage);
        }

        List<Integer> shapesList = new ArrayList<>();
        for (JsonNode shape : shapes) {
            if (shape instanceof NullNode) {
                shapesList.add(null);
                continue;
            }

            shapesList.add(shape.intValue());
        }

        return new ReturnType(shapesList, returnsJsonNode.get("dtype").asText(), returnsJsonNode.get("type").asText());
    }

    /**
     * Method to deserialize Request object from given JSON schema.
     *
     * @param body the JsonNode representation of request body.
     * @return the Request object of the conversion.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public RequestBody parseRequestBody(JsonNode body) throws IOException {
        Map<String, JsonNode> parameters = JsonMapper.readMap(body);
        RequestBody requestBody = new RequestBody();

        for (Map.Entry<String, JsonNode> entry : parameters.entrySet()) {
            requestBody.addParameter(entry.getKey(), parseRecordSet(entry.getValue()));
        }

        return requestBody;
    }

    /**
     * Method to deserialize RecordSet object from given JSON schema.
     *
     * @param recordSetJson the JsonNode representation of RecordSet.
     * @return the RecordSet object of the conversion.
     * @throws JsonProcessingException used to signal fatal problems with mapping of content.
     */
    private RecordSet parseRecordSet(JsonNode recordSetJson) throws IOException {
        JsonNode jsonNode = recordSetJson.findValue("values");

        if (jsonNode == null) {
            String exceptionMessage = "Can not find property values in data: " + recordSetJson;
            Logger.getInstance().log(System.Logger.Level.ERROR, exceptionMessage);
            throw new InvalidValuesException(exceptionMessage);
        }

        return JsonMapper.readValue(recordSetJson.toString(), RecordSet.class);
    }
}