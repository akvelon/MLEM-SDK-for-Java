package com.akvelon.client.util;

import com.akvelon.client.model.validation.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestParser {
    public static RequestDesc toRequestDesc(JsonNode jsonNode) throws IOException {
        Map<String, JsonNode> jsonNodeMap = JsonMapper.readMap(jsonNode.get("methods"));
        ArrayList<ParameterDesc> parameters = toParameters(jsonNodeMap);

        // TODO: 8/16/2022
        return new RequestDesc(parameters, DataType.valueOfType(""));
    }

    public static ArrayList<ParameterDesc> toParameters(Map<String, JsonNode> jsonNodeMap) throws IOException {
        ArrayList<ParameterDesc> parameters = new ArrayList<>();
        for (Map.Entry<String, JsonNode> entry : jsonNodeMap.entrySet()) {
            JsonNode argJsonNode = entry.getValue().get("args").get(0);
            ParameterDesc parameterDesc = new ParameterDesc(entry.getKey(), toRecordSetColumnsDesc(argJsonNode.get("name").asText(), argJsonNode.get("type_")));
            parameters.add(parameterDesc);
        }

        return parameters;
    }

    public static RecordSetColumnsDesc toRecordSetColumnsDesc(String name, JsonNode jsonNode) throws IOException {
        return mapToRecordSetDesc(name, jsonNode.get("columns"), jsonNode.get("dtypes"));
    }

    public static RecordSetColumnsDesc mapToRecordSetDesc(String name, JsonNode columns, JsonNode dtypes) throws IOException {
        ArrayList<RecordSetColumn> recordSetColumns = mapToRecordSetColumn(columns, dtypes);
        return new RecordSetColumnsDesc(name, recordSetColumns);
    }

    public static ArrayList<RecordSetColumn> mapToRecordSetColumn(JsonNode columns, JsonNode dtypes) throws IOException {
        ArrayList<RecordSetColumn> recordSetColumns = new ArrayList<>();
        List<String> names = JsonMapper.readValues(columns);
        List<String> dTypes = JsonMapper.readValues(dtypes);
        for (int i = 0; i < names.size(); i++) {
            recordSetColumns.add(new RecordSetColumn(names.get(i), DataType.valueOfType(dTypes.get(i))));
        }

        return recordSetColumns;
    }
}