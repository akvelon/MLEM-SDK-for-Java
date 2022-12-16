package com.akvelon.client;

import com.akvelon.client.exception.IllegalArrayLength;
import com.akvelon.client.exception.IllegalArrayNestingLevel;
import com.akvelon.client.exception.InvalidTypeException;
import com.akvelon.client.model.validation.ApiSchema;
import com.akvelon.client.model.validation.RequestBodySchema;
import com.akvelon.client.model.validation.ReturnType;
import com.akvelon.client.util.ApiValidator;
import com.akvelon.client.util.JsonParser;
import com.akvelon.client.util.TestDataBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.akvelon.client.MlemJClientTest.*;
import static com.akvelon.client.util.AssertionsUtil.assertResponseJsonOrHandleException;

public class SchemaTest {
    private final MlemJClient jClient = new MlemJClientImpl(HOST_URL, LOGGER, true);

    @Test
    @DisplayName("Test get /interface.json method with json response")
    public void testGetInterfaceRequest() throws ExecutionException, InterruptedException {
        ApiSchema methodDesc = jClient.interfaceJsonAsync().get();
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
        CompletableFuture<ApiSchema> future = jClient.interfaceJsonAsync();
        assertResponseJsonOrHandleException(future);
        ApiSchema apiSchema = future.get();
        new ApiValidator().validateResponse(method, TestDataBuilder.buildResponse1(), apiSchema);
    }

    @Test
    public void testSklearnResponseValidationWithException() throws ExecutionException, InterruptedException {
        CompletableFuture<ApiSchema> future = jClient.interfaceJsonAsync();
        assertResponseJsonOrHandleException(future);
        IllegalArrayNestingLevel thrown = Assertions.assertThrows(
                IllegalArrayNestingLevel.class,
                () -> new ApiValidator().validateResponse(POST_SKLEARN_PREDICT_PROBA, TestDataBuilder.buildResponse1(), future.get())
        );
        Assertions.assertNotNull(thrown);
    }

    @Test
    public void testSklearnResponseValidationWithException1() throws ExecutionException, InterruptedException {
        CompletableFuture<ApiSchema> future = jClient.interfaceJsonAsync();
        assertResponseJsonOrHandleException(future);
        ApiSchema apiSchema = future.get();
        IllegalArrayLength thrown = Assertions.assertThrows(
                IllegalArrayLength.class,
                () -> new ApiValidator().validateResponse(POST_SKLEARN_PREDICT_PROBA, TestDataBuilder.buildResponse3(), apiSchema)
        );
        Assertions.assertNotNull(thrown);
    }

    @Test
    public void testSklearnResponseValidationWithNumberFormatException() throws ExecutionException, InterruptedException {
        CompletableFuture<ApiSchema> future = jClient.interfaceJsonAsync();
        assertResponseJsonOrHandleException(future);
        ApiSchema apiSchema = future.get();
        InvalidTypeException thrown = Assertions.assertThrows(
                InvalidTypeException.class,
                () -> new ApiValidator().validateResponse(POST_SKLEARN_PREDICT_PROBA, TestDataBuilder.buildResponse2(), apiSchema)
        );
        Assertions.assertNotNull(thrown);
    }
}
