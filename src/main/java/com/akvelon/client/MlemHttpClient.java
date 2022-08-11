package com.akvelon.client;

import com.akvelon.client.model.interface_.InterfaceModel;
import com.akvelon.client.model.request.Request;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.CompletableFuture;

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
     * The method sends the /inteface.json get request. The method can catch the exception via exceptionaly method.
     *
     * @return a InterfaceModel response wrapped in the CompletableFuture object.
     */
    CompletableFuture<InterfaceModel> interfaceModelAsync();

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
    CompletableFuture<JsonNode> predict(Request requestBody);

    /**
     * The method sends the /predictProba post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    CompletableFuture<JsonNode> call(String methodName, JsonNode requestBody);
}