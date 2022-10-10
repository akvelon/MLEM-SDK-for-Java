package com.akvelon.client;

import com.akvelon.client.model.request.RequestBody;
import com.akvelon.client.model.request.typical.IrisBody;
import com.akvelon.client.model.request.typical.RegModelBody;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;
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
     * @param requestBody the requests data.
     * @return a response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    CompletableFuture<JsonNode> predict(JsonNode requestBody) throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the /predict post request with given body.
     *
     * @param requestBody the requests data represented in RequestBody class.
     * @return a response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    CompletableFuture<JsonNode> predict(RequestBody requestBody) throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the /predict post request with given body.
     *
     * @param requestBody the requests data represented in RequestBody class.
     * @return a list of responses wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    CompletableFuture<List<JsonNode>> predict(List<RequestBody> requestBody) throws IOException, ExecutionException, InterruptedException;


    /**
     * The method sends the /predict post request with given body.
     *
     * @param requestBody the requests data represented in IrisBody class.
     * @return a List<Long> response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    CompletableFuture<List<Long>> predict(IrisBody requestBody) throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the /predict post request with given RegModel object.
     *
     * @param requestBody the representation of the RegModel class.
     * @return a List<Long> response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    CompletableFuture<List<Long>> predict(RegModelBody requestBody) throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the post request with given method and JsonNode body.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in JsonNode class
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    CompletableFuture<JsonNode> call(String path, JsonNode requestBody) throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the post request with given method and Request body.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in RequestBody class.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type
     * @throws ExecutionException   if this future completed exceptionally
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    CompletableFuture<JsonNode> call(String path, RequestBody requestBody) throws IOException, ExecutionException, InterruptedException;


    /**
     * The method sends the post request with given method and JsonNode body.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in JsonNode class
     * @return a list of responses wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    CompletableFuture<List<JsonNode>> call(String path, List<RequestBody> requestBody) throws IOException, ExecutionException, InterruptedException;


    /**
     * The method sends the post request with given method and Iris body.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in IrisBody class.
     * @return a List<Long> response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    CompletableFuture<List<Long>> call(String path, IrisBody requestBody) throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the post request with given method and RegModel body.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in RegModelBody class.
     * @return a List<Long> response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    CompletableFuture<List<Long>> call(String path, RegModelBody requestBody) throws IOException, ExecutionException, InterruptedException;

}