package com.akvelon.client;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;

public class TestDataFactory {
    public static final String dataRequestBody = "{\n" +
            "  \"data\": {\n" +
            "    \"values\": [\n" +
            "      {\n" +
            "        \"sepal length (cm)\": 0,\n" +
            "        \"sepal width (cm)\": 0,\n" +
            "        \"petal length (cm)\": 0,\n" +
            "        \"petal width (cm)\": 0\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    public static final String xRequestBody = "{\n" +
            "  \"X\": {\n" +
            "    \"values\": [\n" +
            "      {\n" +
            "        \"sepal length (cm)\": 0,\n" +
            "        \"sepal width (cm)\": 0,\n" +
            "        \"petal length (cm)\": 0,\n" +
            "        \"petal width (cm)\": 0\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    public static RecordSet buildRecordSet() {
        Record record = new Record();
        record.addColumn("sepal length (cm)", 0);
        record.addColumn("sepal width (cm)", 0);
        record.addColumn("petal length (cm)", 0);
        record.addColumn("petal width (cm)", 0);
        JsonNode jsonNode = record.toJsonNode();
        Assertions.assertNotNull(jsonNode);

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        return recordSet;
    }

    public static Request buildRequest(String propertyName, RecordSet recordSet) {
        Request request = new Request();
        request.addRecordSet(propertyName, recordSet);

        return request;
    }
}
