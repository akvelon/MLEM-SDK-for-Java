package com.akvelon.client;

import com.akvelon.client.exception.*;
import com.akvelon.client.model.request.RequestBody;
import com.akvelon.client.model.request.typical.IrisBody;
import com.akvelon.client.model.validation.ApiSchema;
import com.akvelon.client.util.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class MlemJClientImplTest {
    private final static String HOST_URL = "http://example-mlem-get-started-app.herokuapp.com/";
    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final System.Logger LOGGER = System.getLogger(MlemJClientImplTest.class.getName());
    private static final JsonParser JSON_PARSER = new JsonParser(LOGGER);

    private final MlemJClient clientWithExecutor = MlemJClientFactory.createMlemJClient(executorService, HOST_URL, LOGGER);
    private final MlemJClient clientWithOutExecutor = MlemJClientFactory.createMlemJClient(HOST_URL, LOGGER);

    /**
     * /predict post-methods
     */
    private final String POST_PREDICT_PROBA = "predict_proba";
    private final String POST_SKLEARN_PREDICT = "sklearn_predict";
    private final String POST_SKLEARN_PREDICT_PROBA = "sklearn_predict_proba";

    @AfterAll
    @DisplayName("ExecutorService stopped")
    public static void cleanUp() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    @Test
    @DisplayName("Test get /interface.json method with json response")
    public void testGetInterfaceJson() throws ExecutionException, InterruptedException {
        assertResponseJsonOrHandleException(clientWithExecutor.interfaceJsonAsync());
    }

    @Test
    @DisplayName("Test get /interface.json method with json response")
    public void testGetInterfaceRequest() throws ExecutionException, InterruptedException, IOException {
        JsonNode response = clientWithExecutor.interfaceJsonAsync().exceptionally(throwable -> {
            InvalidHttpStatusCodeException invalidHttpStatusCodeException = (InvalidHttpStatusCodeException) throwable.getCause();
            assertResponseException(invalidHttpStatusCodeException);
            return null;
        }).get();

        if (response == null) {
            return;
        }

        ApiSchema methodDesc = JSON_PARSER.parseInterfaceSchema(response);
        Assertions.assertNotNull(methodDesc);
    }

    @Test
    @DisplayName("Test post /predict method with JSON response")
    public void testPredictJson() throws ExecutionException, InterruptedException, IOException {
        assertResponseJsonOrHandleException(clientWithExecutor.predict(TestDataFactory.buildDataRequestBody()));
    }

    @Test
    @DisplayName("Test post /predict method with Request request and Json response")
    public void testPredictRequest() throws ExecutionException, InterruptedException, IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet());
        assertResponseJsonOrHandleException(clientWithExecutor.predict(requestBody));
    }

    @Test
    @DisplayName("Test post /predictProba method with JSON request and response")
    public void testPredictProbaJson() throws ExecutionException, InterruptedException, IOException {
        assertResponseJsonOrHandleException(clientWithExecutor.call(POST_PREDICT_PROBA, TestDataFactory.buildDataRequestBody()));
    }

    @Test
    @DisplayName("Test post /predictProba method with Request request and Json response")
    public void testPredictProbaRequest() throws ExecutionException, InterruptedException, IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet());

        assertResponseJsonOrHandleException(clientWithExecutor.call(POST_PREDICT_PROBA, requestBody));
    }

    @Test()
    @DisplayName("Test post /predictProba method with null request body")
    public void testPredictProbaEmptyString() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> clientWithExecutor.call(POST_PREDICT_PROBA, (JsonNode) null).exceptionally(throwable -> {
            InvalidHttpStatusCodeException invalidHttpStatusCodeException = (InvalidHttpStatusCodeException) throwable.getCause();
            assertResponseException(invalidHttpStatusCodeException);
            return null;
        }).get());
        Assertions.assertNotNull(thrown);
    }

    @Test()
    @DisplayName("Test post /sklearnPredict method with JSON request and response")
    public void testSklearnPredictJson() throws InterruptedException, ExecutionException, IOException {
        assertResponseJsonOrHandleException(clientWithExecutor.call(POST_SKLEARN_PREDICT, TestDataFactory.buildXRequestBody()));
    }

    @Test
    @DisplayName("Test post /sklearnPredictProba method with JSON request and response")
    public void testSklearnPredictProbaJson() throws InterruptedException, ExecutionException, IOException {
        assertResponseJsonOrHandleException(clientWithExecutor.call(POST_SKLEARN_PREDICT_PROBA, TestDataFactory.buildXRequestBody()));
    }

    @Test
    @DisplayName("Test post /predict method with executorService = null")
    public void testGetInterfaceExecutorNull() throws ExecutionException, InterruptedException, IOException {
        MlemJClientImpl mlemJClient = new MlemJClientImpl(null, HOST_URL, LOGGER);

        CompletableFuture<JsonNode> future = mlemJClient.predict(TestDataFactory.buildDataRequestBody());
        assertResponseJsonOrHandleException(future);
    }

    @Test
    @DisplayName("Test a list of the requests")
    public void testListRequests() throws ExecutionException, InterruptedException, IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        MlemJClientImpl mlemJClient = new MlemJClientImpl(executorService, HOST_URL, LOGGER);
        List<JsonNode> dataRequestList = Arrays.asList(TestDataFactory.buildDataRequestBody(), TestDataFactory.buildDataRequestBody(), TestDataFactory.buildDataRequestBody());
        List<CompletableFuture<JsonNode>> completableFutures = new ArrayList<>();
        for (JsonNode jsonNode : dataRequestList) {
            CompletableFuture<JsonNode> predict = mlemJClient.predict(jsonNode);
            completableFutures.add(predict);
        }

        for (CompletableFuture<JsonNode> future : completableFutures) {
            assertResponseJsonOrHandleException(future);
        }

        executorService.shutdown();
    }

    @Test
    @DisplayName("Test post /predict method with wrong column value type")
    public void testPredictRequestBadColumnType() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongValue());
        IllegalRecordException thrown = Assertions.assertThrows(IllegalRecordException.class, () -> clientWithExecutor.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column name")
    public void testPredictRequestBadColumnName() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongName());
        IllegalRecordException thrown = Assertions.assertThrows(IllegalRecordException.class, () -> clientWithExecutor.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column name")
    public void testPredictRequestBadColumnsCount() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongCount());
        IllegalColumnsNumberException thrown = Assertions.assertThrows(IllegalColumnsNumberException.class, () -> clientWithExecutor.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong parameter name")
    public void testPredictRequestBadParameterName() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data1", TestDataFactory.buildRecordSetWrongCount());
        InvalidParameterTypeException thrown = Assertions.assertThrows(InvalidParameterTypeException.class, () -> clientWithExecutor.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /call method with wrong parameter name")
    public void testPredictRequestBadRequestName() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongCount());
        IllegalMethodException thrown = Assertions.assertThrows(IllegalMethodException.class, () -> clientWithExecutor.call("illegalmethodname", requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /call empty method")
    public void testCallEmptyMethod() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongCount());
        AssertionError thrown = Assertions.assertThrows(AssertionError.class, () -> clientWithExecutor.call("", requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict empty request")
    public void testPredictEmptyRequest() {
        RequestBody requestBody = new RequestBody();
        IllegalParameterNumberException thrown = Assertions.assertThrows(IllegalParameterNumberException.class, () -> clientWithExecutor.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with Iris request and Json response")
    public void testPredictIris() throws ExecutionException, InterruptedException, IOException {
        IrisBody irisParameters = new IrisBody("data", 0.1d, 1.2d, 3.4d, 5.5d);
        assertResponseListOrHandleException(clientWithExecutor.predict(irisParameters));
    }

    @Test
    @DisplayName("Test post /sklearn_predict method with Iris request and Json response")
    public void testCallIris() throws ExecutionException, InterruptedException, IOException {
        IrisBody irisParameters = new IrisBody("X", 0.1d, 1.2d, 3.4d, 5.5d);
        assertResponseListOrHandleException(clientWithExecutor.call(POST_SKLEARN_PREDICT_PROBA, irisParameters));
    }

    @Test
    @DisplayName("Test post /predict method illegal host")
    public void testIllegalHost() {
        MlemJClientImpl mlemJClientImpl = new MlemJClientImpl(null, HOST_URL + 1, LOGGER);

        ExecutionException thrown = Assertions.assertThrows(ExecutionException.class,
                () -> mlemJClientImpl.predict(TestDataFactory.buildDataRequestBody()).get());

        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with invalid values property")
    public void testPredictJsonInvalidValues() {
        InvalidValuesException thrown = Assertions.assertThrows(InvalidValuesException.class,
                () -> clientWithExecutor.predict(TestDataFactory.buildDataRequestBodyInvalidValues()));

        Assertions.assertNotNull(thrown);
    }

    private void assertResponseString(String response) {
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());
    }

    private void assertResponseJsonOrHandleException(CompletableFuture<JsonNode> future) throws ExecutionException, InterruptedException {
        Assertions.assertNotNull(future);

        JsonNode response = future
                .exceptionally(throwable -> {
                    InvalidHttpStatusCodeException invalidHttpStatusCodeException = (InvalidHttpStatusCodeException) throwable.getCause();
                    assertResponseException(invalidHttpStatusCodeException);
                    return null;
                })
                .get();

        if (response == null) {
            return;
        }

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());
    }

    private void assertResponseListOrHandleException(CompletableFuture<List<Long>> future) throws ExecutionException, InterruptedException {
        Assertions.assertNotNull(future);

        List<Long> response = future
                .exceptionally(throwable -> {
                    InvalidHttpStatusCodeException invalidHttpStatusCodeException = (InvalidHttpStatusCodeException) throwable.getCause();
                    assertResponseException(invalidHttpStatusCodeException);
                    return null;
                })
                .get();

        if (response == null) {
            return;
        }

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());
    }

    private void assertResponseException(InvalidHttpStatusCodeException invalidHttpStatusCodeException) {
        Assertions.assertNotNull(invalidHttpStatusCodeException);
        Assertions.assertNotNull(invalidHttpStatusCodeException.getMessage(), invalidHttpStatusCodeException.getMessage());
        assertResponseString(invalidHttpStatusCodeException.getMessage());
    }
}