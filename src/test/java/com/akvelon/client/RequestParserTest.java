package com.akvelon.client;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.util.RequestParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RequestParserTest {
    private static final System.Logger LOGGER = System.getLogger(MlemJClientImplTest.class.getName());
    private static final RequestParser requestParser = new RequestParser(LOGGER);

    @Test
    public void testRequest() throws IOException {
        Request request = requestParser.parseRequest(TestDataFactory.buildDataRequestBody());
        Assertions.assertNotNull(request);

        Map<String, RecordSet> parameters = request.getParameters();
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