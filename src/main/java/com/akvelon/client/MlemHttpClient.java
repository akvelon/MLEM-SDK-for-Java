package com.akvelon.client;

import com.akvelon.client.model.request.Request;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Interface provides Mlem API
 */
public interface MlemHttpClient {

    /**
     * The method sends the /inteface.json get request. The method can catch the exception via exceptionaly method.
     *
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    CompletableFuture<JsonNode> interfaceJsonAsync();

    /**
     * The method sends the /predict post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    CompletableFuture<JsonNode> predict(JsonNode requestBody);

    /**
     * The method sends the /predict post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the representation of the Request class.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    CompletableFuture<JsonNode> predict(Request requestBody) throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the post request. The method can catch the exception via exceptionaly method.
     *
     * @param methodName  is the method name for the request
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    CompletableFuture<JsonNode> call(String methodName, JsonNode requestBody);

    /**
     * The method sends the post request. The method can catch the exception via exceptionaly method.
     *
     * @param methodName  is the method name for the request
     * @param requestBody is the Request representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    CompletableFuture<JsonNode> call(String methodName, Request requestBody) throws IOException, ExecutionException, InterruptedException;
}