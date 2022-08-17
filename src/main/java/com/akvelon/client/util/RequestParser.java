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
        ArrayList<MethodDesc> methods = toMethods(jsonNodeMap);

        return new RequestDesc(methods);
    }

    public static ArrayList<MethodDesc> toMethods(Map<String, JsonNode> jsonNodeMap) throws IOException {
        ArrayList<MethodDesc> parameters = new ArrayList<>();
        for (Map.Entry<String, JsonNode> entry : jsonNodeMap.entrySet()) {
            JsonNode argJsonNode = entry.getValue().get("args").get(0);
            JsonNode returnsJsonNode = entry.getValue().get("returns");
            MethodDesc parameterDesc = new MethodDesc(
                    new ParameterDesc(entry.getKey(), toRecordSetColumnsDesc(argJsonNode.get("name").asText(), argJsonNode.get("type_"))),
                    DataType.valueOfType(returnsJsonNode.get("dtype").asText()));
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