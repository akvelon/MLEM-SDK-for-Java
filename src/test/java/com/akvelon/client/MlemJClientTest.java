package com.akvelon.client;

import com.akvelon.client.exception.*;
import com.akvelon.client.model.request.RequestBody;
import com.akvelon.client.model.request.typical.IrisBody;
import com.akvelon.client.model.response.ResponseBody;
import com.akvelon.client.util.JsonParser;
import com.akvelon.client.util.TestDataBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static com.akvelon.client.util.AssertionsUtil.*;

public class MlemJClientTest {
    protected final static String HOST_URL = "http://example-mlem-get-started-app.herokuapp.com/";
    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);
    protected static final System.Logger LOGGER = System.getLogger(MlemJClientTest.class.getName());

    protected MlemJClient jClient = MlemJClientFactory.createMlemJClient(executorService, HOST_URL, LOGGER, true);

    /**
     * /predict post-methods
     */
    private final String POST_PREDICT_PROBA = "predict_proba";/*
    public static final String POST_SKLEARN_PREDICT = "sklearn_predict";
    public static final String POST_SKLEARN_PREDICT_PROBA = "sklearn_predict_proba";*/

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
    @DisplayName("Test post /predict method with JSON response")
    public void testPredictJson() throws ExecutionException, InterruptedException, IOException {
        assertResponseJsonOrHandleException(jClient.predict(TestDataBuilder.buildDataRequestBody()));
    }

    @Test
    @DisplayName("Test post /predict method with Request request and Json response")
    public void testPredictRequest() throws ExecutionException, InterruptedException, IOException {
        RequestBody requestBody = TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSet());
        assertResponseJsonOrHandleException(jClient.predict(requestBody));
    }

    @Test
    @DisplayName("Test post /predictProba method with JSON request and response")
    public void testPredictProbaJson() throws ExecutionException, InterruptedException, IOException {
        assertResponseJsonOrHandleException(jClient.call(POST_PREDICT_PROBA, TestDataBuilder.buildDataRequestBody()));
    }

    @Test
    @DisplayName("Test post /predictProba method with Request request and Json response")
    public void testPredictProbaRequest() throws ExecutionException, InterruptedException, IOException {
        RequestBody requestBody = TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSet());

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

    @Test
    @DisplayName("Test post /predict method with executorService = null")
    public void testGetInterfaceExecutorNull() throws ExecutionException, InterruptedException, IOException {
        MlemJClientImpl mlemJClient = new MlemJClientImpl(null, HOST_URL, LOGGER, true);

        CompletableFuture<JsonNode> future = mlemJClient.predict(TestDataBuilder.buildDataRequestBody());
        assertResponseJsonOrHandleException(future);
    }

    @Test
    @DisplayName("Test a list of the predict requests")
    public void testListPredictRequests() throws ExecutionException, InterruptedException, IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        MlemJClientImpl mlemJClient = new MlemJClientImpl(executorService, HOST_URL, LOGGER, true);
        List<RequestBody> dataRequestList = Arrays.asList(
                TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSet()),
                TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSet()),
                TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSet()));
        CompletableFuture<List<ResponseBody>> completableFutures = mlemJClient.predict(dataRequestList);

        assertResponseListOrHandleException(completableFutures);

        executorService.shutdown();
    }

    @Test
    @DisplayName("Test a list of the call requests")
    public void testListCallRequests() throws ExecutionException, InterruptedException, IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        MlemJClientImpl mlemJClient = new MlemJClientImpl(executorService, HOST_URL, LOGGER, true);
        List<RequestBody> dataRequestList = Arrays.asList(
                TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSet()),
                TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSet()),
                TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSet()));
        CompletableFuture<List<ResponseBody>> completableFutures = mlemJClient.call(POST_PREDICT_PROBA, dataRequestList);

        assertResponseListOrHandleException(completableFutures);

        executorService.shutdown();
    }

    @Test
    @DisplayName("Test post /predict method with wrong column value type")
    public void testPredictRequestBadColumnType() throws IOException, ExecutionException, InterruptedException {
        RequestBody requestBody = TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSetWrongValue());
        InvalidTypeException thrown = Assertions.assertThrows(InvalidTypeException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column name")
    public void testPredictRequestBadColumnName() throws IOException {
        RequestBody requestBody = TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSetWrongName());
        KeyNotFoundException thrown = Assertions.assertThrows(KeyNotFoundException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong column name")
    public void testPredictRequestBadColumnsCount() throws IOException {
        RequestBody requestBody = TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSetWrongCount());
        IllegalColumnsNumberException thrown = Assertions.assertThrows(IllegalColumnsNumberException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with wrong parameter name")
    public void testPredictRequestBadParameterName() throws IOException {
        RequestBody requestBody = TestDataBuilder.buildRequest("data1", TestDataBuilder.buildRecordSetWrongCount());
        InvalidParameterNameException thrown = Assertions.assertThrows(InvalidParameterNameException.class, () -> jClient.predict(requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /call method with wrong parameter name")
    public void testPredictRequestBadRequestName() throws IOException {
        RequestBody requestBody = TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSetWrongCount());
        IllegalPathException thrown = Assertions.assertThrows(IllegalPathException.class, () -> jClient.call("illegalmethodname", requestBody).get());
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /call empty method")
    public void testCallEmptyMethod() throws IOException {
        RequestBody requestBody = TestDataBuilder.buildRequest("data", TestDataBuilder.buildRecordSetWrongCount());
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
        assertResponseJsonOrHandleException(jClient.predict(irisParameters));
    }

    @Test
    @DisplayName("Test post /predict method illegal host")
    public void testIllegalHost() {
        MlemJClientImpl mlemJClientImpl = new MlemJClientImpl(null, HOST_URL + 1, LOGGER, true);

        ExecutionException thrown = Assertions.assertThrows(ExecutionException.class,
                () -> mlemJClientImpl.predict(TestDataBuilder.buildDataRequestBody()).get());

        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test post /predict method with invalid values property")
    public void testPredictJsonInvalidValues() {
        InvalidValuesException thrown = Assertions.assertThrows(InvalidValuesException.class,
                () -> jClient.predict(TestDataBuilder.buildDataRequestBodyInvalidValues()));

        Assertions.assertNotNull(thrown);
    }
}