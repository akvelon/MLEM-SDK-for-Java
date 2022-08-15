package com.akvelon.client.util;

import com.akvelon.client.model.validation.*;
import com.akvelon.client.model.validation.method.Method;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestMapper {
    public static ArrayList<RecordSetColumn> mapToRecordSetColumn(JsonNode columns, JsonNode dtypes) throws IOException {
        ArrayList<RecordSetColumn> recordSetColumns = new ArrayList<>();
        List<String> names = JsonMapper.readValues(columns);
        List<String> dTypes = JsonMapper.readValues(dtypes);
        for (int i = 0; i < names.size(); i++) {
            recordSetColumns.add(new RecordSetColumn(names.get(i), DataType.valueOfType(dTypes.get(i))));
        }

        return recordSetColumns;
    }

    public static RecordSetDesc mapToRecordSetDesc(JsonNode columns, JsonNode dtypes) throws IOException {
        ArrayList<RecordSetColumn> recordSetColumns = mapToRecordSetColumn(columns, dtypes);
        return new RecordSetDesc(recordSetColumns);
    }

    public static ParameterDesc mapToParameterDesc(String name, RecordSetDesc recordSetDesc) {
        return new ParameterDesc(name, recordSetDesc);
    }

    public static RequestDesc mapToRequestDesc(JsonNode jsonNode) throws IOException {
        Map<String, Method> jsonNodeMap = JsonMapper.readMap(jsonNode.get("methods"));

        return new RequestDesc(jsonNodeMap);
    }
}