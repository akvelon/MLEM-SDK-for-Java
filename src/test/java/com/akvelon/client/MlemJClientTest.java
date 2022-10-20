package com.akvelon.client;

import com.akvelon.client.exception.*;
import com.akvelon.client.model.request.RequestBody;
import com.akvelon.client.model.request.typical.IrisBody;
import com.akvelon.client.model.validation.ApiSchema;
import com.akvelon.client.model.validation.RequestBodySchema;
import com.akvelon.client.model.validation.ReturnType;
import com.akvelon.client.util.ApiValidator;
import com.akvelon.client.util.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class MlemJClientTest {
    protected final static String HOST_URL = "http://example-mlem-get-started-app.herokuapp.com/";
    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);
    protected static final System.Logger LOGGER = System.getLogger(MlemJClientTest.class.getName());
    private static final JsonParser jsonParser = new JsonParser(LOGGER);

    protected MlemJClient jClient = MlemJClientFactory.createMlemJClient(executorService, HOST_URL, LOGGER, true);

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
        assertResponseJsonOrHandleException(jClient.interfaceJsonAsync());
    }

    @Test
    @DisplayName("Test get /interface.json method with json response")
    public void testGetInterfaceRequest() throws ExecutionException, InterruptedException, IOException {
        JsonNode response = jClient.interfaceJsonAsync().exceptionally(throwable -> {
            InvalidHttpStatusCodeException invalidHttpStatusCodeException = (InvalidHttpStatusCodeException) throwable.getCause();
            assertResponseException(invalidHttpStatusCodeException);
            return null;
        }).get();

        if (response == null) {
            return;
        }

        ApiSchema methodDesc = jsonParser.parseApiSchema(response);
        Assertions.assertNotNull(methodDesc);

        Map<String, RequestBodySchema> requestBodySchemas = methodDesc.getRequestBodySchemas();
        Assertions.assertNotNull(requestBodySchemas);
        for (Map.Entry<String, RequestBodySchema> entry : requestBodySchemas.entrySet()) {
            String method = entry.getKey();
            Assertions.assertNotNull(method);
            RequestBodySchema requestBodySchema = entry.getValue();
            Assertions.assertNotNull(requestBodySchema);
            ReturnType returnType = requestBodySchema.getReturnsSchema();
            Assertions.assertNotNull(returnType);
            List<Integer> shape = returnType.getShape();
            Assertions.assertNotNull(shape);
            String dtype = returnType.getDtype();
            Assertions.assertNotNull(dtype);
            String ndarray = returnType.getType();
            Assertions.assertNotNull(ndarray);
        }
    }

    @Test
    @DisplayName("Test post /predict method with JSON response")
    public void testPredictJson() throws ExecutionException, InterruptedException, IOException {
        assertResponseJsonOrHandleException(jClient.predict(TestDataFactory.buildDataRequestBody()));
    }

    @Test
    @DisplayName("Test post /predict method with Request request and Json response")
    public void testPredictRequest() throws ExecutionException, InterruptedException, IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet());
        assertResponseJsonOrHandleException(jClient.predict(requestBody));
    }

    @Test
    @DisplayName("Test post /predictProba method with JSON request and response")
    public void testPredictProbaJson() throws ExecutionException, InterruptedException, IOException {
        assertResponseJsonOrHandleException(jClient.call(POST_PREDICT_PROBA, TestDataFactory.buildDataRequestBody()));
    }

    @Test
    @DisplayName("Test post /predictProba method with Request request and Json response")
    public void testPredictProbaRequest() throws ExecutionException, InterruptedException, IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet());

        assertResponseJsonOrHandleException(jClient.call(POST_PREDICT_PROBA, requestBody));
    }

    @Test()
    @DisplayName("Test post /predictProba method with null request body")
    public void testPredictProbaEmptyString() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> jClient.call(POST_PREDICT_PROBA, (JsonNode) null).exceptionally(throwable -> {
            InvalidHttpStatusCodeException invalidHttpStatusCodeException = (InvalidHttpStatusCodeException) throwable.getCause();
            assertResponseException(invalidHttpStatusCodeException);
            return null;
        }).get());
        Assertions.assertNotNull(thrown);
    }

    @Test()
    @DisplayName("Test post /sklearnPredict method with JSON request and response")
    public void testSklearnPredictJson() throws InterruptedException, ExecutionException, IOException {
        assertResponseJsonOrHandleException(jClient.call(POST_SKLEARN_PREDICT, TestDataFactory.buildXRequestBody()));
    }

    @Test
    @DisplayName("Test post /sklearnPredictProba method with JSON request and response")
    public void testSklearnPredictProbaJson() throws InterruptedException, ExecutionException, IOException {
        assertResponseJsonOrHandleException(jClient.call(POST_SKLEARN_PREDICT_PROBA, TestDataFactory.buildXRequestBody()));
    }

    @Test
    @DisplayName("Test post /predict method with executorService = null")
    public void testGetInterfaceExecutorNull() throws ExecutionException, InterruptedException, IOException {
        MlemJClientImpl mlemJClient = new MlemJClientImpl(null, HOST_URL, LOGGER, true);

        CompletableFuture<JsonNode> future = mlemJClient.predict(TestDataFactory.buildDataRequestBody());
        assertResponseJsonOrHandleException(future);
    }

    @Test
    @DisplayName("Test a list of the predict requests")
    public void testListPredictRequests() throws ExecutionException, InterruptedException, IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        MlemJClientImpl mlemJClient = new MlemJClientImpl(executorService, HOST_URL, LOGGER, true);
        List<RequestBody> dataRequestList = Arrays.asList(
                TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet()),
                TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet()),
                TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet()));
        CompletableFuture<List<JsonNode>> completableFutures = mlemJClient.predict(dataRequestList);

        assertResponseListOrHandleException(completableFutures);

        executorService.shutdown();
    }

    @Test
    @DisplayName("Test a list of the call requests")
    public void testListCallRequests() throws ExecutionException, InterruptedException, IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        MlemJClientImpl mlemJClient = new MlemJClientImpl(executorService, HOST_URL, LOGGER, true);
        List<RequestBody> dataRequestList = Arrays.asList(
                TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet()),
                TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet()),
                TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSet()));
        CompletableFuture<List<JsonNode>> completableFutures = mlemJClient.call(POST_PREDICT_PROBA, dataRequestList);

        assertResponseListOrHandleException(completableFutures);

        executorService.shutdown();
    }

    @Test
    @DisplayName("Test post /predict method with wrong column value type")
    public void testPredictRequestBadColumnType() throws IOException, ExecutionException, InterruptedException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongValue());
        IllegalNumberFormatException thrown = Assertions.assertThrows(IllegalNumberFormatException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column name")
    public void testPredictRequestBadColumnName() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongName());
        IllegalRecordTypeException thrown = Assertions.assertThrows(IllegalRecordTypeException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column name")
    public void testPredictRequestBadColumnsCount() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongCount());
        IllegalColumnsNumberException thrown = Assertions.assertThrows(IllegalColumnsNumberException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong parameter name")
    public void testPredictRequestBadParameterName() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data1", TestDataFactory.buildRecordSetWrongCount());
        InvalidParameterNameException thrown = Assertions.assertThrows(InvalidParameterNameException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /call method with wrong parameter name")
    public void testPredictRequestBadRequestName() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongCount());
        IllegalPathException thrown = Assertions.assertThrows(IllegalPathException.class, () -> jClient.call("illegalmethodname", requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /call empty method")
    public void testCallEmptyMethod() throws IOException {
        RequestBody requestBody = TestDataFactory.buildRequest("data", TestDataFactory.buildRecordSetWrongCount());
        AssertionError thrown = Assertions.assertThrows(AssertionError.class, () -> jClient.call("", requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict empty request")
    public void testPredictEmptyRequest() {
        RequestBody requestBody = new RequestBody();
        IllegalParameterNumberException thrown = Assertions.assertThrows(IllegalParameterNumberException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with Iris request and Json response")
    public void testPredictIris() throws ExecutionException, InterruptedException, IOException {
        IrisBody irisParameters = new IrisBody("data", 0.1d, 1.2d, 3.4d, 5.5d);
        assertResponseListOrHandleException(jClient.predict(irisParameters));
    }

    @Test
    @DisplayName("Test post /sklearn_predict method with Iris request and Json response")
    public void testCallIris() throws ExecutionException, InterruptedException, IOException {
        IrisBody irisParameters = new IrisBody("X", 0.1d, 1.2d, 3.4d, 5.5d);
        assertResponseListOrHandleException(jClient.call(POST_SKLEARN_PREDICT_PROBA, irisParameters));
    }

    @Test
    @DisplayName("Test post /predict method illegal host")
    public void testIllegalHost() {
        MlemJClientImpl mlemJClientImpl = new MlemJClientImpl(null, HOST_URL + 1, LOGGER, true);

        ExecutionException thrown = Assertions.assertThrows(ExecutionException.class,
                () -> mlemJClientImpl.predict(TestDataFactory.buildDataRequestBody()).get());

        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with invalid values property")
    public void testPredictJsonInvalidValues() {
        InvalidValuesException thrown = Assertions.assertThrows(InvalidValuesException.class,
                () -> jClient.predict(TestDataFactory.buildDataRequestBodyInvalidValues()));

        Assertions.assertNotNull(thrown);
    }

    @Test
    public void testPredictResponseValidation() throws ExecutionException, InterruptedException, IOException {
        getSchemaAndValidateResponse("predict");
    }

    @Test
    public void testSklearnPredictResponseValidation() throws ExecutionException, InterruptedException, IOException {
        getSchemaAndValidateResponse(POST_SKLEARN_PREDICT);
    }

    private void getSchemaAndValidateResponse(String method) throws ExecutionException, InterruptedException, IOException {
        CompletableFuture<JsonNode> future = jClient.interfaceJsonAsync();
        assertResponseJsonOrHandleException(future);
        JsonNode apiSchema = future.get();
        new JsonParser(LOGGER).parseApiSchema(apiSchema);
        new ApiValidator(LOGGER).validateResponse(method, TestDataFactory.buildResponse1(), new JsonParser(LOGGER).parseApiSchema(apiSchema));
    }

    @Test
    public void testSklearnResponseValidationWithException() throws ExecutionException, InterruptedException, IOException {
        CompletableFuture<JsonNode> future = jClient.interfaceJsonAsync();
        assertResponseJsonOrHandleException(future);
        JsonNode apiSchema = future.get();
        new JsonParser(LOGGER).parseApiSchema(apiSchema);
        InvalidResponseTypeException thrown = Assertions.assertThrows(
                InvalidResponseTypeException.class,
                () -> new ApiValidator(LOGGER).validateResponse(POST_SKLEARN_PREDICT_PROBA, TestDataFactory.buildResponse1(), new JsonParser(LOGGER).parseApiSchema(apiSchema))
        );
        Assertions.assertNotNull(thrown);
    }

    @Test
    public void testSklearnResponseValidationWithNumberFormatException() throws ExecutionException, InterruptedException, IOException {
        CompletableFuture<JsonNode> future = jClient.interfaceJsonAsync();
        assertResponseJsonOrHandleException(future);
        JsonNode apiSchema = future.get();
        new JsonParser(LOGGER).parseApiSchema(apiSchema);
        IllegalNumberFormatException thrown = Assertions.assertThrows(
                IllegalNumberFormatException.class,
                () -> new ApiValidator(LOGGER).validateResponse(POST_SKLEARN_PREDICT_PROBA, TestDataFactory.buildResponse2(), new JsonParser(LOGGER).parseApiSchema(apiSchema))
        );
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

    private <T> void assertResponseListOrHandleException(CompletableFuture<List<T>> future) throws ExecutionException, InterruptedException {
        Assertions.assertNotNull(future);

        List<T> response = future
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