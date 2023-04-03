package com.akvelon.client.util;

import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RequestBodyMappingTest {

    private final String dataRequestBody = "{\n" +
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

    @Test
    @DisplayName("Test a deserialization JSON content from given RecordSet content.")
    public void testRecordSetToJsonNode() throws JsonProcessingException {
        JsonNode jsonArray = TestDataBuilder.buildIrisRecordSet().toJson();
        Assertions.assertNotNull(jsonArray);
    }

    @Test
    @DisplayName("Test a deserialization JSON content from given Request content.")
    public void testRequestToJsonNode() throws JsonProcessingException {
        RecordSet recordSet = TestDataBuilder.buildIrisRecordSet();
        RequestBody requestBody = TestDataBuilder.buildRequest("data", recordSet);
        JsonNode jsonNode = requestBody.toJson();

        Assertions.assertNotNull(jsonNode);

        JsonNode dataJsonNode = jsonNode.get("data");
        Assertions.assertNotNull(dataJsonNode);
    }
}