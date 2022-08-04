package com.akvelon.client;

import com.akvelon.client.model.interface_.InterfaceModel;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.CompletableFuture;

/**
 * Interface provides Mlem API
 */
public interface MlemHttpClient {
    /**
     * The method sends the /inteface.json get request. The method can catch the exception via exceptionaly method.
     *
     * @return a String response wrapped in the CompletableFuture object.
     */
    CompletableFuture<String> interfaceStringAsync();

    /**
     * The method sends the /inteface.json get request. The method can catch the exception via exceptionaly method.
     *
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    CompletableFuture<JsonNode> interfaceJsonAsync();

    /**
     * The method sends the /inteface.json get request. The method can catch the exception via exceptionaly method.
     *
     * @return a InterfaceModel response wrapped in the CompletableFuture object.
     */
    CompletableFuture<InterfaceModel> interfaceModelAsync();

    /**
     * The method sends the /predict post request.  The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the String representation of the request.
     * @return a String response wrapped in the CompletableFuture object.
     */
    CompletableFuture<String> predictAsync(String requestBody);

    /**
     * The method sends the /predict post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    CompletableFuture<JsonNode> predictAsync(JsonNode requestBody);

    /**
     * The method sends the /predictProba post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the String representation of the request.
     * @return a String response wrapped in the CompletableFuture object.
     */
    CompletableFuture<String> predictProbaAsync(String requestBody);

    /**
     * The method sends the /predictProba post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    CompletableFuture<JsonNode> predictProbaAsync(JsonNode requestBody);

    /**
     * The method sends the /sklearnPredict post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the String representation of the request.
     * @return a String response wrapped in the CompletableFuture object.
     */
    CompletableFuture<String> sklearnPredictAsync(String requestBody);

    /**
     * The method sends the /sklearnPredict post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    CompletableFuture<JsonNode> sklearnPredictAsync(JsonNode requestBody);

    /**
     * The method sends the /sklearnPredictProba post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the String representation of the request.
     * @return a String response wrapped in the CompletableFuture object.
     */
    CompletableFuture<String> sklearnPredictProbaAsync(String requestBody);

    /**
     * The method sends the /sklearnPredictProba post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    CompletableFuture<JsonNode> sklearnPredictProbaAsync(JsonNode requestBody);
}