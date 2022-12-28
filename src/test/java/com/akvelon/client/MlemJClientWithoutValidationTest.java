package com.akvelon.client;

import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;
import com.akvelon.client.model.response.ResponseBody;
import com.akvelon.client.util.TestDataBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MlemJClientWithoutValidationTest extends MlemJClientTest {

    public MlemJClientWithoutValidationTest() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        super.jClient = MlemJClientFactory.createMlemJClient(executorService, HOST_URL, LOGGER, false);
    }

    @Test
    @DisplayName("Test post /predict empty request")
    public void testPredictEmptyRequest() {
        RequestBody<RecordSet> requestBody = new RequestBody<>();
        ExecutionException thrown = Assertions.assertThrows(ExecutionException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong parameter name")
    public void testPredictRequestBadParameterName() throws IOException {
        RequestBody<RecordSet> requestBody = TestDataBuilder.buildRequest("data1", TestDataBuilder.buildRecordSetWrongCount());
        ExecutionException thrown = Assertions.assertThrows(ExecutionException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column name")
    public void testPredictRequestBadColumnName() throws IOException {
        RequestBody<RecordSet> requestBody = TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSetWrongName());
        ExecutionException thrown = Assertions.assertThrows(ExecutionException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column value type")
    public void testPredictRequestBadColumnType() throws IOException, ExecutionException, InterruptedException {
        RequestBody<RecordSet> requestBody = TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSetWrongValue());
        ResponseBody jsonNode = jClient.predict(requestBody).get();
        Assertions.assertNotNull(jsonNode);
    }

    @Test
    @DisplayName("Test post /call method with wrong parameter name")
    public void testPredictRequestBadRequestName() throws IOException {
        RequestBody<RecordSet> requestBody = TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSetWrongCount());
        ExecutionException thrown = Assertions.assertThrows(ExecutionException.class, () -> jClient.call("illegalmethodname", requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column name")
    public void testPredictRequestBadColumnsCount() throws IOException {
        RequestBody<RecordSet> requestBody = TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSetWrongCount());
        ExecutionException thrown = Assertions.assertThrows(ExecutionException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }
}
