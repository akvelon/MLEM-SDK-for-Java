package com.akvelon.client;

import com.akvelon.client.model.validation.ApiSchema;
import com.akvelon.client.model.validation.RequestBodySchema;
import com.akvelon.client.model.validation.ReturnType;
import com.akvelon.client.util.ApiValidator;
import com.akvelon.client.util.TestDataBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.akvelon.client.MlemJClientTest.HOST_URL;
import static com.akvelon.client.MlemJClientTest.LOGGER;
import static com.akvelon.client.util.AssertionsUtil.assertResponseJsonOrHandleException;

public class SchemaTest {
    private final MlemJClient jClient = new MlemJClientImpl(HOST_URL, LOGGER, true);

    @Test
    @DisplayName("Test get /interface.json method with json response")
    public void testGetInterfaceRequest() throws ExecutionException, InterruptedException, JsonProcessingException {
        ApiSchema apiSchema = jClient.interfaceJsonAsync().get();
        Assertions.assertNotNull(apiSchema);

        String jsonString = apiSchema.toJsonString();
        Assertions.assertNotNull(jsonString);

        JsonNode jsonNode = apiSchema.toJsonNode();
        Assertions.assertNotNull(jsonNode);

        Map<String, RequestBodySchema> requestBodySchemas = apiSchema.getRequestBodySchemas();
        Assertions.assertNotNull(requestBodySchemas);
        for (Map.Entry<String, RequestBodySchema> entry : requestBodySchemas.entrySet()) {
            String method = entry.getKey();
            Assertions.assertNotNull(method);
            RequestBodySchema requestBodySchema = entry.getValue();
            Assertions.assertNotNull(requestBodySchema);
            ReturnType returnType = requestBodySchema.getReturnsSchema();
            Assertions.assertNotNull(returnType);
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

    private void getSchemaAndValidateResponse(String method) throws ExecutionException, InterruptedException, IOException {
        CompletableFuture<ApiSchema> future = jClient.interfaceJsonAsync();
        assertResponseJsonOrHandleException(future);
        ApiSchema apiSchema = future.get();
        new ApiValidator().validateResponse(method, TestDataBuilder.buildResponse1(), apiSchema);
    }
}
