package com.akvelon.client.util;

import com.akvelon.client.model.request.ArraySet;
import com.akvelon.client.model.request.RecordType;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;

public class TestDataBuilder {
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

    private static final String strRequestBody = "{\n" +
            "  \"data\": [\n" +
            "    \"1abcde\"\n" +
            "  ]\n" +
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

    public static JsonNode buildStrRequestBody() throws JsonProcessingException {
        return JsonMapper.readValue(strRequestBody, JsonNode.class);
    }

    public static JsonNode buildResponse1() throws JsonProcessingException {
        return JsonMapper.readValue("[1, 2]", JsonNode.class);
    }

    public static JsonNode buildResponse2() throws JsonProcessingException {
        return JsonMapper.readValue("[[1.7, 2, 17]]", JsonNode.class);
    }

    public static JsonNode buildResponse3() throws JsonProcessingException {
        return JsonMapper.readValue("[[1.7, 2.1]]", JsonNode.class);
    }

    public static RecordSet buildIrisRecordSet() throws JsonProcessingException {
        RecordType recordType = new RecordType();
        recordType.addColumn("sepal length (cm)", 1.2);
        recordType.addColumn("sepal width (cm)", 2.4);
        recordType.addColumn("petal length (cm)", 3.3);
        recordType.addColumn("petal width (cm)", 4.1);
        JsonNode jsonNode = recordType.toJsonNode();
        Assertions.assertNotNull(jsonNode);

        RecordSet recordSet = new RecordSet("values");
        recordSet.addRecord(recordType);

        return recordSet;
    }

    public static RecordSet buildRecordSetWrongValue() throws JsonProcessingException {
        RecordType recordType = new RecordType();
        recordType.addColumn("sepal length (cm)", 1);
        recordType.addColumn("sepal width (cm)", 2.4);
        recordType.addColumn("petal length (cm)", 3.3);
        recordType.addColumn("petal width (cm)", 4.1);
        JsonNode jsonNode = recordType.toJsonNode();
        Assertions.assertNotNull(jsonNode);

        RecordSet recordSet = new RecordSet("values");
        recordSet.addRecord(recordType);

        return recordSet;
    }

    public static RecordSet buildRecordSetWrongName() throws JsonProcessingException {
        RecordType recordType = new RecordType();
        recordType.addColumn("length (cm)", 1);
        recordType.addColumn("sepal width (cm)", 2.4);
        recordType.addColumn("petal length (cm)", 3.3);
        recordType.addColumn("petal width (cm)", 4.1);
        JsonNode jsonNode = recordType.toJsonNode();
        Assertions.assertNotNull(jsonNode);

        RecordSet recordSet = new RecordSet("values");
        recordSet.addRecord(recordType);

        return recordSet;
    }

    public static RecordSet buildRecordSetWrongCount() throws JsonProcessingException {
        RecordType recordType = new RecordType();
        recordType.addColumn("length (cm)", 1);
        JsonNode jsonNode = recordType.toJsonNode();
        Assertions.assertNotNull(jsonNode);

        RecordSet recordSet = new RecordSet("values");
        recordSet.addRecord(recordType);

        return recordSet;
    }

    public static RequestBody buildRequest(String property, RecordSet recordSet) {
        RequestBody requestBody = new RequestBody();
        requestBody.setParameter(property, recordSet);

        return requestBody;
    }

    public static <T extends Number> RequestBody buildRequest(String property, ArraySet<T> recordSet) {
        RequestBody requestBody = new RequestBody();
        requestBody.setParameter(property, recordSet);

        return requestBody;
    }

    public static RecordSet buildSeriesTypeRecordSet() throws JsonProcessingException {
        RecordType recordType = new RecordType();
        recordType.addColumn("Pclass", 1L);
        recordType.addColumn("Parch", 2L);
        JsonNode jsonNode = recordType.toJsonNode();
        Assertions.assertNotNull(jsonNode);

        RecordSet recordSet = new RecordSet("values");
        recordSet.addRecord(recordType);

        return recordSet;
    }
}