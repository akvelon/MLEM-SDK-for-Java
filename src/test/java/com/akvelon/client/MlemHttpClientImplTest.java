package com.akvelon.client;

import com.akvelon.client.exception.RestException;
import com.akvelon.client.model.error.ValidationError;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.model.validation.InterfaceDesc;
import com.akvelon.client.util.JsonMapper;
import com.akvelon.client.util.RequestParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class MlemHttpClientImplTest {
    private final static String HOST_URL = "http://example-mlem-get-started-app.herokuapp.com/";
    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final MlemHttpClient clientWithExecutor = MlemHttpClientFactory.createMlemHttpClient(executorService, HOST_URL);
    private final MlemHttpClient clientWithOutExecutor = MlemHttpClientFactory.createMlemHttpClient(HOST_URL);

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

        InterfaceDesc methodDesc = RequestParser.parseInterfaceSchema(response);
        Assertions.assertNotNull(methodDesc);
    }

    @Test
    @DisplayName("Test post /predict method with JSON response")
    public void testPredictJson() throws ExecutionException, InterruptedException {
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
    public void testPredictProbaJson() throws ExecutionException, InterruptedException {
        assertResponseJsonOrHandleException(clientWithExecutor.call(POST_PREDICT_PROBA, TestDataFactory.buildDataRequestBody()));
    }

    @Test
    @DisplayName("Test post /predictProba method with Request request and Json response")
    public void testPredictProbaRequest() throws ExecutionException, InterruptedException {
        Request request = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet());

        assertResponseJsonOrHandleException(clientWithExecutor.call(POST_PREDICT_PROBA, request));
    }

    @Test()
    @DisplayName("Test post /predictProba method with null request body")
    public void testPredictProbaEmptyString() {
        NullPointerException thrown = Assertions.assertThrows(NullPointerException.class, () -> clientWithExecutor.call(POST_PREDICT_PROBA, (JsonNode) null).exceptionally(throwable -> {
            RestException restException = (RestException) throwable.getCause();
            assertResponseException(restException);
            return null;
        }).get());
        Assertions.assertNotNull(thrown);
    }

    @Test()
    @DisplayName("Test post /sklearnPredict method with JSON request and response")
    public void testSklearnPredictJson() throws InterruptedException, ExecutionException {
        assertResponseJsonOrHandleException(clientWithExecutor.call(POST_SKLEARN_PREDICT, TestDataFactory.buildXRequestBody()));
    }

    @Test
    @DisplayName("Test post /sklearnPredictProba method with JSON request and response")
    public void testSklearnPredictProbaJson() throws InterruptedException, ExecutionException {
        assertResponseJsonOrHandleException(clientWithExecutor.call(POST_SKLEARN_PREDICT_PROBA, TestDataFactory.buildDataRequestBody()));
    }

    @Test()
    @DisplayName("Test post /sklearnPredict method with WRONG JSON request and response. Handle the HTTP 500.")
    public void testSklearnPredict500Error() throws InterruptedException, ExecutionException {
        JsonNode result = clientWithExecutor.call(POST_SKLEARN_PREDICT, TestDataFactory.buildDataRequestBody()).exceptionally(throwable -> {
            RestException restException = (RestException) throwable.getCause();
            assertResponseException(restException);
            return null;
        }).get();
        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Test post /sklearnPredict method with WRONG JSON request and response")
    public void testSklearnPredictProba500error() throws InterruptedException, ExecutionException {
        JsonNode result = clientWithExecutor.call(POST_SKLEARN_PREDICT_PROBA, TestDataFactory.buildDataRequestBody()).exceptionally(throwable -> {
            RestException restException = (RestException) throwable.getCause();
            assertResponseException(restException);
            return null;
        }).get();
        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Test post /predict method with executorService = null")
    public void testGetInterfaceExecutorNull() throws ExecutionException, InterruptedException {
        MlemHttpClientImpl mlemHttpClientImpl = new MlemHttpClientImpl(null, HOST_URL);

        CompletableFuture<JsonNode> future = mlemHttpClientImpl.predict(TestDataFactory.buildDataRequestBody());
        assertResponseJsonOrHandleException(future);
    }

    @Test
    @DisplayName("Test a list of the requests")
    public void testListRequests() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        MlemHttpClientImpl mlemHttpClientImpl = new MlemHttpClientImpl(executorService, HOST_URL);
        List<JsonNode> dataRequestList = Arrays.asList(TestDataFactory.buildDataRequestBody(), TestDataFactory.buildDataRequestBody(), TestDataFactory.buildDataRequestBody());
        List<CompletableFuture<JsonNode>> completableFutures = dataRequestList.stream().map(mlemHttpClientImpl::predict).collect(Collectors.toList());

        for (CompletableFuture<JsonNode> future : completableFutures) {
            assertResponseJsonOrHandleException(future);
        }

        executorService.shutdown();
    }

    private void assertResponseString(String response) {
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());
    }

    private void assertResponseJsonOrHandleException(CompletableFuture<JsonNode> future) throws ExecutionException, InterruptedException {
        Assertions.assertNotNull(future);

        JsonNode response = future.exceptionally(throwable -> {
            RestException restException = (RestException) throwable.getCause();
            assertResponseException(restException);
            return null;
        }).get();

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

        if (restException.getStatusCode() == 422) {
            ValidationError validationError = JsonMapper.readValue(restException.getMessage(), ValidationError.class);
            Assertions.assertNotNull(validationError);
            Assertions.assertNotNull(validationError.getDetail());
        }
    }
}