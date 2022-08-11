package com.akvelon.client;

import com.akvelon.client.exception.RestException;
import com.akvelon.client.model.error.ValidationError;
import com.akvelon.client.model.interface_.InterfaceModel;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class MlemHttpClientImplTest {
    private final static String HOST_URL = "http://example-mlem-get-started-app.herokuapp.com/";
    /*
        private final static String HOST_URL = "http://localhost:8080/";
    */
    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final MlemHttpClientImpl clientWithExecutor = new MlemHttpClientImpl(executorService, HOST_URL);
    private final MlemHttpClientImpl clientWithOutExecutor = new MlemHttpClientImpl(HOST_URL);

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
    @DisplayName("Test get /interface.json method with InterfaceModel response")
    public void testGetInterfaceModel() throws ExecutionException, InterruptedException {
        CompletableFuture<InterfaceModel> future = clientWithExecutor.interfaceModelAsync();
        Assertions.assertNotNull(future);
        InterfaceModel response = future.get();
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("Test post /predict method with JSON response")
    public void testPredictJson() throws ExecutionException, InterruptedException {
        assertResponseJsonOrHandleException(clientWithExecutor.predictAsync(TestDataFactory.buildDataRequestBody()));
    }

    @Test
    @DisplayName("Test post /predict method with Request response")
    public void testPredictRequest() throws ExecutionException, InterruptedException {
        Request request = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet());

        assertResponseJsonOrHandleException(clientWithExecutor.predictAsync(request.toJson()));
    }

    @Test
    @DisplayName("Test post /predictProba method with JSON request and response")
    public void testPredictProbaJson() throws ExecutionException, InterruptedException {
        assertResponseJsonOrHandleException(clientWithExecutor.predictProbaAsync(TestDataFactory.buildDataRequestBody()));
    }

    @Test()
    @DisplayName("Test post /predictProba method with null request body")
    public void testPredictProbaEmptyString() {
        NullPointerException thrown = Assertions.assertThrows(NullPointerException.class, () -> clientWithExecutor.predictProbaAsync(null).exceptionally(throwable -> {
            RestException restException = (RestException) throwable.getCause();
            assertResponseException(restException);
            return null;
        }).get());
        Assertions.assertNotNull(thrown);
    }

    @Test()
    @DisplayName("Test post /sklearnPredict method with JSON request and response")
    public void testSklearnPredictJson() throws InterruptedException, ExecutionException {
        assertResponseJsonOrHandleException(clientWithExecutor.sklearnPredictAsync(TestDataFactory.buildXRequestBody()));
    }

    @Test
    @DisplayName("Test post /sklearnPredictProba method with JSON request and response")
    public void testSklearnPredictProbaJson() throws InterruptedException, ExecutionException {
        assertResponseJsonOrHandleException(clientWithExecutor.sklearnPredictProbaAsync(TestDataFactory.buildDataRequestBody()));
    }

    @Test()
    @DisplayName("Test post /sklearnPredict method with WRONG JSON request and response. Handle the HTTP 500.")
    public void testSklearnPredict500Error() throws InterruptedException, ExecutionException {
        JsonNode result = clientWithExecutor.sklearnPredictAsync(TestDataFactory.buildDataRequestBody()).exceptionally(throwable -> {
            RestException restException = (RestException) throwable.getCause();
            assertResponseException(restException);
            return null;
        }).get();
        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Test post /sklearnPredict method with WRONG JSON request and response")
    public void testSklearnPredictProba500error() throws InterruptedException, ExecutionException {
        JsonNode result = clientWithExecutor.sklearnPredictProbaAsync(TestDataFactory.buildDataRequestBody()).exceptionally(throwable -> {
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

        CompletableFuture<JsonNode> future = mlemHttpClientImpl.predictAsync(TestDataFactory.buildDataRequestBody());
        assertResponseJsonOrHandleException(future);
    }

    @Test
    @DisplayName("Test a list of the requests")
    public void testListRequests() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        MlemHttpClientImpl mlemHttpClientImpl = new MlemHttpClientImpl(executorService, HOST_URL);
        List<JsonNode> dataRequestList = Arrays.asList(TestDataFactory.buildDataRequestBody(), TestDataFactory.buildDataRequestBody(), TestDataFactory.buildDataRequestBody());
        List<CompletableFuture<JsonNode>> completableFutures = dataRequestList.stream().map(mlemHttpClientImpl::predictAsync).collect(Collectors.toList());

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
            ValidationError validationError = JsonMapper.stringToObject(restException.getMessage(), ValidationError.class);
            Assertions.assertNotNull(validationError);
            Assertions.assertNotNull(validationError.getDetail());
        }
    }
}