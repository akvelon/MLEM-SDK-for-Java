package com.akvelon.client;

import com.akvelon.client.exception.IllegalArrayLength;
import com.akvelon.client.exception.IllegalArrayNestingLevel;
import com.akvelon.client.exception.InvalidHttpStatusCodeException;
import com.akvelon.client.exception.InvalidTypeException;
import com.akvelon.client.model.validation.ApiSchema;
import com.akvelon.client.model.validation.RequestBodySchema;
import com.akvelon.client.model.validation.ReturnType;
import com.akvelon.client.util.ApiValidator;
import com.akvelon.client.util.JsonParser;
import com.akvelon.client.util.TestDataBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.akvelon.client.util.AssertionsUtil.assertResponseException;
import static com.akvelon.client.util.AssertionsUtil.assertResponseJsonOrHandleException;
import static com.akvelon.client.MlemJClientTest.*;

public class SchemaTest {
    private static final JsonParser jsonParser = new JsonParser();
    private final MlemJClientImpl jClient = new MlemJClientImpl(HOST_URL, LOGGER, true);

    @Test
    @DisplayName("Test get /interface.json method with json response")
    public void testGetInterfaceJson() throws ExecutionException, InterruptedException, IOException {
        CompletableFuture<JsonNode> completableFuture = jClient.interfaceJsonAsync();
        assertResponseJsonOrHandleException(completableFuture);
        ApiSchema methodDesc = jsonParser.parseApiSchema(completableFuture.get());
        Assertions.assertNotNull(methodDesc);
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
        new JsonParser().parseApiSchema(apiSchema);
        new ApiValidator().validateResponse(method, TestDataBuilder.buildResponse1(), new JsonParser().parseApiSchema(apiSchema));
    }

    @Test
    public void testSklearnResponseValidationWithException() throws ExecutionException, InterruptedException, IOException {
        CompletableFuture<JsonNode> future = jClient.interfaceJsonAsync();
        assertResponseJsonOrHandleException(future);
        JsonNode apiSchema = future.get();
        new JsonParser().parseApiSchema(apiSchema);
        IllegalArrayNestingLevel thrown = Assertions.assertThrows(
                IllegalArrayNestingLevel.class,
                () -> new ApiValidator().validateResponse(POST_SKLEARN_PREDICT_PROBA, TestDataBuilder.buildResponse1(), new JsonParser().parseApiSchema(apiSchema))
        );
        Assertions.assertNotNull(thrown);
    }

    @Test
    public void testSklearnResponseValidationWithException1() throws ExecutionException, InterruptedException, IOException {
        CompletableFuture<JsonNode> future = jClient.interfaceJsonAsync();
        assertResponseJsonOrHandleException(future);
        JsonNode apiSchema = future.get();
        new JsonParser().parseApiSchema(apiSchema);
        IllegalArrayLength thrown = Assertions.assertThrows(
                IllegalArrayLength.class,
                () -> new ApiValidator().validateResponse(POST_SKLEARN_PREDICT_PROBA, TestDataBuilder.buildResponse3(), new JsonParser().parseApiSchema(apiSchema))
        );
        Assertions.assertNotNull(thrown);
    }

    @Test
    public void testSklearnResponseValidationWithNumberFormatException() throws ExecutionException, InterruptedException, IOException {
        CompletableFuture<JsonNode> future = jClient.interfaceJsonAsync();
        assertResponseJsonOrHandleException(future);
        JsonNode apiSchema = future.get();
        new JsonParser().parseApiSchema(apiSchema);
        InvalidTypeException thrown = Assertions.assertThrows(
                InvalidTypeException.class,
                () -> new ApiValidator().validateResponse(POST_SKLEARN_PREDICT_PROBA, TestDataBuilder.buildResponse2(), new JsonParser().parseApiSchema(apiSchema))
        );
        Assertions.assertNotNull(thrown);
    }
}
