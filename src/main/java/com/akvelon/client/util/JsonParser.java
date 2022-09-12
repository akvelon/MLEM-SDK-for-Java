package com.akvelon.client.util;

import com.akvelon.client.exception.IllegalColumnsNumberException;
import com.akvelon.client.exception.InvalidArgsTypeException;
import com.akvelon.client.exception.InvalidRecordSetTypeException;
import com.akvelon.client.exception.InvalidValuesException;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.model.validation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A parser that provides functionality for deserialization JSON schema to descriptions objects.
 */
public final class JsonParser {
    /**
     * System.Logger instances log messages that will be routed to the underlying.
     * logging framework the LoggerFinder uses.
     */
    private final System.Logger logger;

    public JsonParser(System.Logger logger) {
        this.logger = logger;
    }

    /**
     * Method to deserialize a InterfaceDesc object from given JSON schema.
     *
     * @param schema the JsonNode representation of InterfaceDesc.
     * @return the InterfaceDesc object of the conversion.
     * @throws IOException signals that an I/O exception has occurred
     *                     or an illegal recordSetType has occurred or args is not array.
     */
    public InterfaceDesc parseInterfaceSchema(JsonNode schema) throws IOException {
        Map<String, JsonNode> jsonNodeMap = JsonMapper.readMap(schema.get("methods"));
        Map<String, RequestDesc> methods = parseMethod(jsonNodeMap);

        return new InterfaceDesc(methods);
    }

    /**
     * Method to deserialize a list of RequestDesc object from given JSON schema.
     *
     * @param jsonNodeMap a map with string-json representation of RequestDesc list.
     * @return the mapped objects of the conversion.
     * @throws IOException signals that an illegal recordSetType has occurred or args is not array.
     */
    private Map<String, RequestDesc> parseMethod(Map<String, JsonNode> jsonNodeMap) throws IOException {
        Map<String, RequestDesc> parameters = new HashMap<>();
        // fill the parameters by created RequestDesc.
        for (Map.Entry<String, JsonNode> entry : jsonNodeMap.entrySet()) {
            RequestDesc parameterDesc = parseParameter(entry);
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
    private RequestDesc parseParameter(Map.Entry<String, JsonNode> entry) throws IOException {
        // access value of the "args" of an object node.
        JsonNode args = entry.getValue().get("args");
        if (!args.isArray()) {
            String exceptionMessage = "args is not array: " + args;
            logger.log(System.Logger.Level.ERROR, exceptionMessage);
            throw new InvalidArgsTypeException(exceptionMessage);
        }

        // access value of the "returns" of an object node.
        JsonNode returnsJsonNode = entry.getValue().get("returns");

        Map<String, RecordSetDesc> parameterDescMap = new HashMap<>();

        // fill the parameterDescList by created items.
        for (JsonNode arg : args) {
            RecordSetDesc recordSetDesc = parseRecordSetDesc(arg.get("type_"));
            parameterDescMap.put(arg.get("name").asText(), recordSetDesc);
        }

        return new RequestDesc(
                parameterDescMap,
                DataType.fromString(returnsJsonNode.get("dtype").asText())
        );
    }

    /**
     * Method to deserialize RecordSetDesc object from given JSON schema.
     *
     * @param jsonNode the JsonNode representation of RecordSetDesc.
     * @return the RecordSetDesc object of the conversion.
     * @throws IOException signals that an illegal recordSetType has occurred.
     */
    private RecordSetDesc parseRecordSetDesc(JsonNode jsonNode) throws IOException {
        // get recordSet type description.
        String recordSetDescType = jsonNode.get("type").asText();
        if (!recordSetDescType.equals("dataframe")) {
            String exceptionMessage = "RecordSet type is not dataframe: " + recordSetDescType;
            logger.log(System.Logger.Level.ERROR, exceptionMessage);
            throw new InvalidRecordSetTypeException(exceptionMessage);
        }

        JsonNode columns = jsonNode.get("columns");
        JsonNode dtypes = jsonNode.get("dtypes");

        List<String> names = JsonMapper.readValues(columns);
        List<String> dTypes = JsonMapper.readValues(dtypes);
        if (names.size() != dTypes.size()) {
            throw new IllegalColumnsNumberException("Columns size must be equal to dtypes. " +
                    "Actual columns size: " + names.size() + ", actual dtypes size: " + dTypes.size() + ".");
        }

        ArrayList<RecordSetColumn> recordSetColumns = new ArrayList<>();
        // fill the recordSetColumns by names and dTypes.
        for (int i = 0; i < names.size(); i++) {
            recordSetColumns.add(new RecordSetColumn(names.get(i), DataType.fromString(dTypes.get(i))));
        }

        return new RecordSetDesc(
                recordSetDescType,
                recordSetColumns
        );
    }

    /**
     * Method to deserialize Request object from given JSON schema.
     *
     * @param body the JsonNode representation of Request parameters.
     * @return the Request object of the conversion.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public Request parseRequest(JsonNode body) throws IOException {
        // Convert results from given JSON tree into Map<String, JsonNode>.
        Map<String, JsonNode> parameters = JsonMapper.readMap(body);
        // create the request and add a parameters.
        Request request = new Request();
        for (Map.Entry<String, JsonNode> entry : parameters.entrySet()) {
            request.addParameter(entry.getKey(), parseRecordSet(entry.getValue()));
        }

        return request;
    }

    /**
     * Method to deserialize RecordSet object from given JSON schema.
     *
     * @param recordSetJson the JsonNode representation of RecordSet.
     * @return the RecordSet object of the conversion.
     * @throws JsonProcessingException used to signal fatal problems with mapping of content.
     */
    private RecordSet parseRecordSet(JsonNode recordSetJson) throws IOException {
        // find values property.
        JsonNode jsonNode = recordSetJson.findValue("values");
        if (jsonNode == null) {
            throw new InvalidValuesException("Can not find property values in data: " + recordSetJson);
        }

        return JsonMapper.readValue(
                recordSetJson.toString(),
                RecordSet.class
        );
    }
}