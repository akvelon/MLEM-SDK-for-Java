package com.akvelon.client;

import com.akvelon.client.exception.*;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.model.validation.InterfaceDesc;
import com.akvelon.client.util.RequestParser;
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
    private static final RequestParser requestParser = new RequestParser(LOGGER);

    private final MlemJClient clientWithExecutor = MlemJClientFactory.createMlemHttpClient(executorService, HOST_URL, LOGGER);
    private final MlemJClient clientWithOutExecutor = MlemJClientFactory.createMlemHttpClient(HOST_URL, LOGGER);

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
            RestException restException = (RestException) throwable.getCause();
            assertResponseException(restException);
            return null;
        }).get();

        if (response == null) {
            return;
        }

        InterfaceDesc methodDesc = requestParser.parseInterfaceSchema(response);
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
        Request request = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet());
        assertResponseJsonOrHandleException(clientWithExecutor.predict(request));
    }

    @Test
    @DisplayName("Test post /predictProba method with JSON request and response")
    public void testPredictProbaJson() throws ExecutionException, InterruptedException, IOException {
        assertResponseJsonOrHandleException(clientWithExecutor.call(POST_PREDICT_PROBA, TestDataFactory.buildDataRequestBody()));
    }

    @Test
    @DisplayName("Test post /predictProba method with Request request and Json response")
    public void testPredictProbaRequest() throws ExecutionException, InterruptedException, IOException {
        Request request = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet());

        assertResponseJsonOrHandleException(clientWithExecutor.call(POST_PREDICT_PROBA, request));
    }

    @Test()
    @DisplayName("Test post /predictProba method with null request body")
    public void testPredictProbaEmptyString() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> clientWithExecutor.call(POST_PREDICT_PROBA, (JsonNode) null).exceptionally(throwable -> {
            RestException restException = (RestException) throwable.getCause();
            assertResponseException(restException);
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
        MlemJClientImpl mlemHttpClientImpl = new MlemJClientImpl(null, HOST_URL, LOGGER);

        CompletableFuture<JsonNode> future = mlemHttpClientImpl.predict(TestDataFactory.buildDataRequestBody());
        assertResponseJsonOrHandleException(future);
    }

    @Test
    @DisplayName("Test a list of the requests")
    public void testListRequests() throws ExecutionException, InterruptedException, IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        MlemJClientImpl mlemHttpClientImpl = new MlemJClientImpl(executorService, HOST_URL, LOGGER);
        List<JsonNode> dataRequestList = Arrays.asList(TestDataFactory.buildDataRequestBody(), TestDataFactory.buildDataRequestBody(), TestDataFactory.buildDataRequestBody());
        List<CompletableFuture<JsonNode>> completableFutures = new ArrayList<>();
        for (JsonNode jsonNode : dataRequestList) {
            CompletableFuture<JsonNode> predict = mlemHttpClientImpl.predict(jsonNode);
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
        Request request = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongValue());
        IllegalRecordException thrown = Assertions.assertThrows(IllegalRecordException.class, () -> clientWithExecutor.predict(request).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column name")
    public void testPredictRequestBadColumnName() throws IOException {
        Request request = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongName());
        IllegalRecordException thrown = Assertions.assertThrows(IllegalRecordException.class, () -> clientWithExecutor.predict(request).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column name")
    public void testPredictRequestBadColumnsCount() throws IOException {
        Request request = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongCount());
        IllegalColumnsNumberException thrown = Assertions.assertThrows(IllegalColumnsNumberException.class, () -> clientWithExecutor.predict(request).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong parameter name")
    public void testPredictRequestBadParameterName() throws IOException {
        Request request = TestDataFactory.buildRequest("data1", TestDataFactory.buildRecordSetWrongCount());
        IllegalParameterException thrown = Assertions.assertThrows(IllegalParameterException.class, () -> clientWithExecutor.predict(request).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /call method with wrong parameter name")
    public void testPredictRequestBadRequestName() throws IOException {
        Request request = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongCount());
        IllegalMethodException thrown = Assertions.assertThrows(IllegalMethodException.class, () -> clientWithExecutor.call("illegalmethodname", request).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /call empty method")
    public void testCallEmptyMethod() throws IOException {
        Request request = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongCount());
        AssertionError thrown = Assertions.assertThrows(AssertionError.class, () -> clientWithExecutor.call("", request).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict empty request")
    public void testPredictEmptyRequest() {
        Request request = new Request();
        IllegalParametersNumberException thrown = Assertions.assertThrows(IllegalParametersNumberException.class, () -> clientWithExecutor.predict(request).get());
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
                    RestException restException = (RestException) throwable.getCause();
                    assertResponseException(restException);
                    return null;
                })
                .get();

        if (response == null) {
            return;
        }

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());
    }

    private void assertResponseException(RestException restException) {
        Assertions.assertNotNull(restException);
        Assertions.assertNotNull(restException.getMessage(), restException.getMessage());
        assertResponseString(restException.getMessage());
    }
}