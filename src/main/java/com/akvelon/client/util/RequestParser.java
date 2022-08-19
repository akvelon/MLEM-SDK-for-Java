package com.akvelon.client.util;

import com.akvelon.client.model.validation.*;
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

    public static ArrayList<RequestDesc> parseMethod(Map<String, JsonNode> jsonNodeMap) throws IOException {
        ArrayList<RequestDesc> parameters = new ArrayList<>();
        for (Map.Entry<String, JsonNode> entry : jsonNodeMap.entrySet()) {
            RequestDesc parameterDesc = parseParameter(entry);
            parameters.add(parameterDesc);
        }

        return parameters;
    }

    public static RequestDesc parseParameter(Map.Entry<String, JsonNode> entry) throws IOException {
        JsonNode args = entry.getValue().get("args");
        JsonNode returnsJsonNode = entry.getValue().get("returns");

        if (!args.isArray()) {
            throw new IllegalArgumentException();
        }

        List<ParameterDesc> parameterDescList = new ArrayList<>();
        for (JsonNode arg : args) {
            RecordSetDesc recordSetDesc = parseRecordSetDesc(arg.get("name").asText(), arg.get("type_"));
            ParameterDesc parameterDesc = new ParameterDesc(arg.get("name").asText(), recordSetDesc);
            parameterDescList.add(parameterDesc);
        }

        return new RequestDesc(entry.getKey(),
                parameterDescList,
                DataType.fromString(returnsJsonNode.get("dtype").asText()));
    }

    public static RecordSetDesc parseRecordSetDesc(String name, JsonNode jsonNode) throws IOException {
        String recordSetDescType = jsonNode.get("type").asText();
        if (!recordSetDescType.equals("dataframe")) {
            throw new IllegalArgumentException();
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
}