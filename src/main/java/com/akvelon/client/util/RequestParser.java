package com.akvelon.client.util;

import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.model.validation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestParser {
    public static InterfaceDesc parseInterfaceSchema(JsonNode schema) throws IOException {
        Map<String, JsonNode> jsonNodeMap = JsonMapper.readMap(schema.get("methods"));
        ArrayList<RequestDesc> methods = parseMethod(jsonNodeMap);

        return new InterfaceDesc(methods);
    }

    private static ArrayList<RequestDesc> parseMethod(Map<String, JsonNode> jsonNodeMap) throws IOException {
        ArrayList<RequestDesc> parameters = new ArrayList<>();
        for (Map.Entry<String, JsonNode> entry : jsonNodeMap.entrySet()) {
            RequestDesc parameterDesc = parseParameter(entry);
            parameters.add(parameterDesc);
        }

        return parameters;
    }

    private static RequestDesc parseParameter(Map.Entry<String, JsonNode> entry) throws IOException {
        JsonNode args = entry.getValue().get("args");
        JsonNode returnsJsonNode = entry.getValue().get("returns");

        if (!args.isArray()) {
            throw new IOException();
        }

        List<ParameterDesc> parameterDescList = new ArrayList<>();
        for (JsonNode arg : args) {
            RecordSetDesc recordSetDesc = parseRecordSetDesc(arg.get("type_"));
            ParameterDesc parameterDesc = new ParameterDesc(arg.get("name").asText(), recordSetDesc);
            parameterDescList.add(parameterDesc);
        }

        return new RequestDesc(entry.getKey(),
                parameterDescList,
                DataType.fromString(returnsJsonNode.get("dtype").asText()));
    }

    private static RecordSetDesc parseRecordSetDesc(JsonNode jsonNode) throws IOException {
        String recordSetDescType = jsonNode.get("type").asText();
        if (!recordSetDescType.equals("dataframe")) {
            throw new IOException();
        }

        JsonNode columns = jsonNode.get("columns");
        JsonNode dtypes = jsonNode.get("dtypes");

        ArrayList<RecordSetColumn> recordSetColumns = new ArrayList<>();
        List<String> names = JsonMapper.readValues(columns);
        List<String> dTypes = JsonMapper.readValues(dtypes);
        for (int i = 0; i < names.size(); i++) {
            recordSetColumns.add(new RecordSetColumn(names.get(i), DataType.fromString(dTypes.get(i))));
        }

        return new RecordSetDesc(recordSetDescType, recordSetColumns);
    }

    public static Request parseRequest(JsonNode body) throws IOException {
        Map<String, JsonNode> parameters = JsonMapper.readMap(body);
        Request request = new Request();
        for (Map.Entry<String, JsonNode> entry : parameters.entrySet()) {
            request.addParameter(entry.getKey(), parseRecordSet(entry.getValue()));
        }

        return request;
    }

    /**
     *
     * @param recordSetJson
     * @return
     * @throws JsonProcessingException
     */
    private static RecordSet parseRecordSet(JsonNode recordSetJson) throws JsonProcessingException {
        return JsonMapper.readValue(recordSetJson.toString(), RecordSet.class);
    }
}