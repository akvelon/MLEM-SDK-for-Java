package com.akvelon.client;

import com.akvelon.client.model.request.RequestBody;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MlemJClientWithoutValidationTest extends MlemJClientTest {

    public MlemJClientWithoutValidationTest() {
        super.jClient = MlemJClientFactory.createMlemJClient(HOST_URL, LOGGER, false);
    }

    @Test
    @DisplayName("Test post /predict empty request")
    public void testPredictEmptyRequest() {
        RequestBody requestBody = new RequestBody();
        ExecutionException thrown = Assertions.assertThrows(ExecutionException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong parameter name")
    public void testPredictRequestBadParameterName() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data1", TestDataFactory.buildRecordSetWrongCount());
        ExecutionException thrown = Assertions.assertThrows(ExecutionException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column name")
    public void testPredictRequestBadColumnName() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongName());
        ExecutionException thrown = Assertions.assertThrows(ExecutionException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column value type")
    public void testPredictRequestBadColumnType() throws IOException, ExecutionException, InterruptedException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongValue());
        JsonNode jsonNode = jClient.predict(requestBody).get();
        Assertions.assertNotNull(jsonNode);
    }

    @Test
    @DisplayName("Test post /call method with wrong parameter name")
    public void testPredictRequestBadRequestName() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongCount());
        ExecutionException thrown = Assertions.assertThrows(ExecutionException.class, () -> jClient.call("illegalmethodname", requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column name")
    public void testPredictRequestBadColumnsCount() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongCount());
        ExecutionException thrown = Assertions.assertThrows(ExecutionException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }
}
