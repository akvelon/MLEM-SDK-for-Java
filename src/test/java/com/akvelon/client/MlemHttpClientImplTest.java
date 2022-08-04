package com.akvelon.client;

import com.akvelon.client.exception.RestException;
import com.akvelon.client.model.error.ValidationError;
import com.akvelon.client.model.interface_.InterfaceModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class MlemHttpClientImplTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final static String HOST_URL = "http://localhost:8080/";

    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final MlemHttpClientImpl clientWithExecutor = new MlemHttpClientImpl(executorService, HOST_URL);
    private final MlemHttpClientImpl clientWithOutExecutor = new MlemHttpClientImpl(HOST_URL);

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

    private final String xRequestBody = "{\n" +
            "  \"X\": {\n" +
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
    @DisplayName("Test get /interface.json method with string response")
    public void testGetInterfaceString() throws ExecutionException, InterruptedException {
        assertResponseStringOrHandleException(clientWithExecutor.interfaceStringAsync());
    }

    @Test
    @DisplayName("Test get /interface.json method without executor")
    public void testGetInterfaceWithoutExecutor() throws ExecutionException, InterruptedException {
        assertResponseStringOrHandleException(clientWithOutExecutor.interfaceStringAsync());
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
    @DisplayName("Test post /predict method with string response")
    public void testPredictString() throws ExecutionException, InterruptedException {
        assertResponseStringOrHandleException(clientWithExecutor.predictAsync(dataRequestBody));
    }

    @Test
    @DisplayName("Test post /predict method with JSON response")
    public void testPredictJson() throws ExecutionException, InterruptedException {
        assertResponseJsonOrHandleException(clientWithExecutor.predictAsync(stringToJsonNode(dataRequestBody)));
    }

    @Test
    @DisplayName("Test post /predictProba method with STRING request and response")
    public void testPredictProbaString() throws ExecutionException, InterruptedException {
        assertResponseStringOrHandleException(clientWithExecutor.predictProbaAsync(dataRequestBody));
    }

    @Test
    @DisplayName("Test post /predictProba method with JSON request and response")
    public void testPredictProbaJson() throws ExecutionException, InterruptedException {
        assertResponseJsonOrHandleException(clientWithExecutor.predictProbaAsync(stringToJsonNode(dataRequestBody)));
    }

    @Test
    @DisplayName("Test post /predictProba method with empty request body")
    public void testPredictProbaEmptyString() throws InterruptedException, ExecutionException {
        String result = clientWithExecutor.predictProbaAsync("").exceptionally(throwable -> {
            RestException restException = (RestException) throwable.getCause();
            assertResponseException(restException);
            return null;
        }).get();
        Assertions.assertNull(result);
    }

    @Test()
    @DisplayName("Test post /sklearnPredict method with STRING request and response")
    public void testSklearnPredictString() throws InterruptedException, ExecutionException {
        assertResponseStringOrHandleException(clientWithExecutor.predictProbaAsync(xRequestBody));
    }

    @Test
    @DisplayName("Test post /sklearnPredictProba method with STRING request and response")
    public void testSklearnPredictProbaString() throws InterruptedException, ExecutionException {
        assertResponseStringOrHandleException(clientWithExecutor.sklearnPredictProbaAsync(xRequestBody));
    }

    @Test()
    @DisplayName("Test post /sklearnPredict method with JSON request and response")
    public void testSklearnPredictJson() throws InterruptedException, ExecutionException {
        assertResponseJsonOrHandleException(clientWithExecutor.sklearnPredictAsync(stringToJsonNode(xRequestBody)));
    }

    @Test
    @DisplayName("Test post /sklearnPredictProba method with JSON request and response")
    public void testSklearnPredictProbaJson() throws InterruptedException, ExecutionException {
        assertResponseJsonOrHandleException(clientWithExecutor.sklearnPredictProbaAsync(stringToJsonNode(xRequestBody)));
    }

    @Test()
    @DisplayName("Test post /sklearnPredict method with WRONG JSON request and response. Handle the HTTP 500.")
    public void testSklearnPredict500Error() throws InterruptedException, ExecutionException {
        String result = clientWithExecutor
                .sklearnPredictAsync(dataRequestBody)
                .exceptionally(throwable -> {
                    RestException restException = (RestException) throwable.getCause();
                    assertResponseException(restException);
                    return null;
                })
                .get();
        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Test post /sklearnPredict method with WRONG JSON request and response")
    public void testSklearnPredictProba500error() throws InterruptedException, ExecutionException {
        String result = clientWithExecutor
                .sklearnPredictProbaAsync(dataRequestBody)
                .exceptionally(throwable -> {
                    RestException restException = (RestException) throwable.getCause();
                    assertResponseException(restException);
                    return null;
                })
                .get();
        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Test post /predict method with executorService = null")
    public void testGetInterfaceExecutorNull() throws ExecutionException, InterruptedException {
        MlemHttpClientImpl mlemHttpClientImpl = new MlemHttpClientImpl(null, HOST_URL);

        CompletableFuture<String> future = mlemHttpClientImpl.predictAsync(dataRequestBody);
        assertResponseStringOrHandleException(future);
    }

    @Test
    @DisplayName("Test a list of the requests")
    public void testListRequests() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        MlemHttpClientImpl mlemHttpClientImpl = new MlemHttpClientImpl(executorService, HOST_URL);
        List<String> dataRequestList = Arrays.asList(dataRequestBody, dataRequestBody, dataRequestBody);
        List<CompletableFuture<String>> completableFutures = dataRequestList.stream()
                .map(mlemHttpClientImpl::predictAsync)
                .collect(Collectors.toList());

        for (CompletableFuture<String> future : completableFutures) {
            assertResponseStringOrHandleException(future);
        }

        executorService.shutdown();
    }

    private void assertResponseStringOrHandleException(CompletableFuture<String> future) throws ExecutionException, InterruptedException {
        Assertions.assertNotNull(future);
        String response = future.exceptionally(throwable -> {
            RestException restException = (RestException) throwable.getCause();
            assertResponseException(restException);
            return null;
        }).get();
        if (response == null) {
            return;
        }

        assertResponseString(response);
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
        Assertions.assertNotNull(restException.getMessage());
        assertResponseString(restException.getMessage());

        if (restException.getStatusCode() == 422) {
            ValidationError validationError = stringToObject(restException.getMessage(), ValidationError.class);
            Assertions.assertNotNull(validationError);
            Assertions.assertNotNull(validationError.getDetail());
        }
    }

    private <T> T stringToObject(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode stringToJsonNode(String json) {
        return stringToObject(json, JsonNode.class);
    }
}