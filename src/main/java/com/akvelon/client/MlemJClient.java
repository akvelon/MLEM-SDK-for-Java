package com.akvelon.client;

import com.akvelon.client.model.request.RequestBody;
import com.akvelon.client.model.response.ResponseBody;
import com.akvelon.client.model.validation.ApiSchema;
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
     * The method sends the /interface.json get request to obtain a validation rules.
     *
     * @return a schema wrapped in the CompletableFuture object.
     */
    CompletableFuture<ApiSchema> interfaceJsonAsync();

    /**
     * The method sends the /predict post request with given JSON body.
     *
     * @param requestBody the requests data.
     * @return a response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    CompletableFuture<JsonNode> predict(JsonNode requestBody)
            throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the /predict post request with given body.
     *
     * @param requestBody the requests data represented in RequestBody class.
     * @return a response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    <RequestT extends RequestBody, ResponseT extends ResponseBody> CompletableFuture<ResponseT> predict(RequestT requestBody)
            throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the /predict post request with given bodies list.
     *
     * @param requestBody the requests data represented in List<RequestBody>.
     * @return a response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    <RequestT extends RequestBody, ResponseT extends ResponseBody> CompletableFuture<List<ResponseT>> predict(List<RequestT> requestBody)
            throws IOException, ExecutionException, InterruptedException;

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
    CompletableFuture<JsonNode> call(String path, JsonNode requestBody)
            throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the post request with given method and requestBody.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in RequestBody class.
     * @return a ResponseBody response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type
     * @throws ExecutionException   if this future completed exceptionally
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    <RequestT extends RequestBody, ResponseT extends ResponseBody> CompletableFuture<ResponseT> call(String path, RequestT requestBody)
            throws IOException, ExecutionException, InterruptedException;

    /**
     * The method sends the post request with given method and requestBody.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in RequestBody class.
     * @return a list of responses wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    <RequestT extends RequestBody, ResponseT extends ResponseBody> CompletableFuture<List<ResponseT>> call(String path, List<RequestT> requestBody)
            throws IOException, ExecutionException, InterruptedException;
}