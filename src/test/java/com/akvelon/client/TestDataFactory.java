package com.akvelon.client;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;

public class TestDataFactory {
    private static final String dataRequestBody = "{\n" +
            "  \"data\": {\n" +
            "    \"values\": [\n" +
            "      {\n" +
            "        \"sepal length (cm)\": 0.0,\n" +
            "        \"sepal width (cm)\": 0.1,\n" +
            "        \"petal length (cm)\": 0.5,\n" +
            "        \"petal width (cm)\": 0.2\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    private static final String dataRequestBodyInvalidValues = "{\n" +
            "  \"data\": {\n" +
            "    \"values123\": [\n" +
            "      {\n" +
            "        \"sepal length (cm)\": 0.0,\n" +
            "        \"sepal width (cm)\": 0.1,\n" +
            "        \"petal length (cm)\": 0.5,\n" +
            "        \"petal width (cm)\": 0.2\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    private static final String xRequestBody = "{\n" +
            "  \"X\": {\n" +
            "    \"values\": [\n" +
            "      {\n" +
            "        \"sepal length (cm)\": 0.1,\n" +
            "        \"sepal width (cm)\": 0.2,\n" +
            "        \"petal length (cm)\": 0.3,\n" +
            "        \"petal width (cm)\": 0.4\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    public static JsonNode buildDataRequestBody() throws JsonProcessingException {
        return JsonMapper.readValue(dataRequestBody, JsonNode.class);
    }

    public static JsonNode buildDataRequestBodyInvalidValues() throws JsonProcessingException {
        return JsonMapper.readValue(dataRequestBodyInvalidValues, JsonNode.class);
    }

    public static JsonNode buildXRequestBody() throws JsonProcessingException {
        return JsonMapper.readValue(xRequestBody, JsonNode.class);
    }

    public static RecordSet buildRecordSet() throws JsonProcessingException {
        Record record = new Record();
        record.addColumn("sepal length (cm)", 1.2);
        record.addColumn("sepal width (cm)", 2.4);
        record.addColumn("petal length (cm)", 3.3);
        record.addColumn("petal width (cm)", 4.1);
        JsonNode jsonNode = record.toJsonNode();
        Assertions.assertNotNull(jsonNode);

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        return recordSet;
    }

    public static RecordSet buildRecordSetWrongValue() throws JsonProcessingException {
        Record record = new Record();
        record.addColumn("sepal length (cm)", 1);
        record.addColumn("sepal width (cm)", 2.4);
        record.addColumn("petal length (cm)", 3.3);
        record.addColumn("petal width (cm)", 4.1);
        JsonNode jsonNode = record.toJsonNode();
        Assertions.assertNotNull(jsonNode);

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        return recordSet;
    }

    public static RecordSet buildRecordSetWrongName() throws JsonProcessingException {
        Record record = new Record();
        record.addColumn("length (cm)", 1);
        record.addColumn("sepal width (cm)", 2.4);
        record.addColumn("petal length (cm)", 3.3);
        record.addColumn("petal width (cm)", 4.1);
        JsonNode jsonNode = record.toJsonNode();
        Assertions.assertNotNull(jsonNode);

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        return recordSet;
    }

    public static RecordSet buildRecordSetWrongCount() throws JsonProcessingException {
        Record record = new Record();
        record.addColumn("length (cm)", 1);
        JsonNode jsonNode = record.toJsonNode();
        Assertions.assertNotNull(jsonNode);

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        return recordSet;
    }

    public static Request buildRequest(String propertyName, RecordSet recordSet) {
        Request request = new Request();
        request.addParameter(propertyName, recordSet);

        return request;
    }
}
