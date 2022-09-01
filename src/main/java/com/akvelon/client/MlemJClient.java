package com.akvelon.client;

import com.akvelon.client.model.request.Request;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * A MlemJClient that provides API for using MLEM technologies with given rules.
 */
public interface MlemJClient {
    /**
     * The method sends the /inteface.json get request to obtain a validation rules.
     *
     * @return a schema wrapped in the CompletableFuture object.
     */
    CompletableFuture<JsonNode> interfaceJsonAsync();

    /**
     * The method sends the /predict post request with given JSON body.
     *
     * @param requestBody the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    CompletableFuture<JsonNode> predict(JsonNode requestBody) throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the /predict post request with given Request object body.
     *
     * @param requestBody the representation of the Request class.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    CompletableFuture<JsonNode> predict(Request requestBody) throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the post request with given method and JsonNode body.
     *
     * @param methodName  the method name for the request.
     * @param requestBody the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    CompletableFuture<JsonNode> call(String methodName, JsonNode requestBody) throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the post request with given method and Request body.
     *
     * @param methodName  the method name for the request
     * @param requestBody the Request representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type
     * @throws ExecutionException   if this future completed exceptionally
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    CompletableFuture<JsonNode> call(String methodName, Request requestBody) throws IOException, ExecutionException, InterruptedException;
}