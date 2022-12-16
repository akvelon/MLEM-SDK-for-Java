package com.akvelon.client.util;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonParserTest {
    private static final System.Logger LOGGER = System.getLogger(JsonParserTest.class.getName());
    private static final JsonParser JSON_PARSER = new JsonParser();

    @Test
    public void testRequest() throws IOException {
        RequestBody requestBody = JSON_PARSER.parseRequestBody(TestDataBuilder.buildDataRequestBody());
        Assertions.assertNotNull(requestBody);

        Map<String, RecordSet> parameters = requestBody.getParameters();
        Assertions.assertNotNull(parameters);
        Assertions.assertFalse(parameters.isEmpty());
        for (Map.Entry<String, RecordSet> entry : parameters.entrySet()) {
            RecordSet recordSet = entry.getValue();
            Assertions.assertNotNull(recordSet);

            List<Record> records = recordSet.getRecords();
            Assertions.assertNotNull(records);
            for (Record record : records) {
                Assertions.assertNotNull(record);
            }
        }
    }
}