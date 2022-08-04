package com.akvelon.client;

import com.akvelon.client.exception.RestException;
import com.akvelon.client.model.interface_.InterfaceModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * Provides the functionality for Mlem API
 */
class MlemHttpClientImpl implements MlemHttpClient {
    /**
     * /interface.json get-method
     */
    private final String GET_INTERFACE = "interface.json";
    /**
     * /predict post-method
     */
    private final String POST_PREDICT = "predict";
    /**
     * /predict post-method
     */
    private final String POST_PREDICT_PROBA = "predict_proba";
    private final String POST_SKLEARN_PREDICT = "sklearn_predict";
    private final String POST_SKLEARN_PREDICT_PROBA = "sklearn_predict_proba";

    private final String host;

    private final HttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructor for creating the implementation of Mlem HttpClient
     *
     * @param host is the host URL
     */
    public MlemHttpClientImpl(String host) {
        this(null, host);
    }

    /**
     * Constructor for creating the implementation of Mlem HttpClient
     *
     * @param executorService provides a pool of threads and an API for assigning tasks to it
     * @param host            is the host URL
     */
    public MlemHttpClientImpl(ExecutorService executorService, String host) {
        this.host = host;
        //create a builder for a httpClient
        HttpClient.Builder builder = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1);

        //check the executorService for null and set it to the httpClient
        if (executorService != null) {
            builder.executor(executorService);
        }

        //build the httpClient
        this.httpClient = builder.build();
    }

    /**
     * The method sends the /inteface.json get request. The method can catch the exception via exceptionaly method.
     *
     * @return a String response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<String> interfaceStringAsync() {
        return sendAsyncGetString(GET_INTERFACE);
    }

    /**
     * The method sends the /inteface.json get request. The method can catch the exception via exceptionaly method.
     *
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<JsonNode> interfaceJsonAsync() {
        return sendAsyncGetJson(GET_INTERFACE);
    }

    /**
     * The method sends the /inteface.json get request. The method can catch the exception via exceptionaly method.
     *
     * @return a InterfaceModel response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<InterfaceModel> interfaceModelAsync() {
        return sendAsyncGetModel(GET_INTERFACE);
    }

    /**
     * The method sends the /predict post request.  The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the String representation of the request.
     * @return a String response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<String> predictAsync(String requestBody) {
        return sendAsyncPostString(POST_PREDICT, requestBody);
    }

    /**
     * The method sends the /predict post request.  The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<JsonNode> predictAsync(JsonNode requestBody) {
        return sendAsyncPostJson(POST_PREDICT, requestBody);
    }

    /**
     * The method sends the /predictProba post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the String representation of the request.
     * @return a String response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<String> predictProbaAsync(String requestBody) {
        return sendAsyncPostString(POST_PREDICT_PROBA, requestBody);
    }

    /**
     * The method sends the /predictProba post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<JsonNode> predictProbaAsync(JsonNode requestBody) {
        return sendAsyncPostJson(POST_PREDICT_PROBA, requestBody);
    }

    /**
     * The method sends the /sklearnPredict post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the String representation of the request.
     * @return a String response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<String> sklearnPredictAsync(String requestBody) {
        return sendAsyncPostString(POST_SKLEARN_PREDICT, requestBody);
    }

    /**
     * The method sends the /sklearnPredict post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<JsonNode> sklearnPredictAsync(JsonNode requestBody) {
        return sendAsyncPostJson(POST_SKLEARN_PREDICT, requestBody);
    }

    /**
     * The method sends the /sklearnPredictProba post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the String representation of the request.
     * @return a String response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<String> sklearnPredictProbaAsync(String requestBody) {
        return sendAsyncPostString(POST_SKLEARN_PREDICT_PROBA, requestBody);
    }

    /**
     * The method sends the /sklearnPredictProba post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<JsonNode> sklearnPredictProbaAsync(JsonNode requestBody) {
        return sendAsyncPostJson(POST_SKLEARN_PREDICT_PROBA, requestBody);
    }

    /**
     * The method send the async get request for given method name.
     *
     * @param method is the name of request params
     * @return a String response wrapped in the CompletableFuture object.
     */
    private CompletableFuture<String> sendAsyncGetString(String method) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(host + method)).build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body);
    }

    /**
     * The method send the async get request for given method name.
     *
     * @param method is the name of request params
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    private CompletableFuture<JsonNode> sendAsyncGetJson(String method) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(host + method)).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(stringHttpResponse -> stringToJsonNode(stringHttpResponse.body(), JsonNode.class));
    }

    /**
     * The method send the async get request for given method name.
     *
     * @param method is the name of request params
     * @return a InterfaceModel response wrapped in the CompletableFuture object.
     */
    private CompletableFuture<InterfaceModel> sendAsyncGetModel(String method) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(host + method)).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(stringHttpResponse -> stringToJsonNode(stringHttpResponse.body(), InterfaceModel.class));
    }

    /**
     * The method send the async post request for given method name and request body
     *
     * @param method      is the name of request params
     * @param requestBody is the request body
     * @return a String response wrapped in the CompletableFuture object.
     */
    private CompletableFuture<String> sendAsyncPostString(String method, String requestBody) {
        // Build a new post request
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(requestBody)).uri(URI.create(host + method)).header("Content-Type", "application/json").build();

        return httpClient
                // Sends the given request asynchronously using this client with the given response body handler
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                // Returns a new CompletionStage that, when this stage completes normally,
                // is executed with this stage's result as the argument to the supplied function.
                .thenApply(httpResponse -> {
                    // check response for exception and if true throw the exception
                    checkResponseAndThrowException(httpResponse);
                    // if no exception caused, return the response body
                    return httpResponse.body();
                });
    }

    /**
     * The method send the async post request for given method name and request body
     *
     * @param method      is the name of request params
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    private CompletableFuture<JsonNode> sendAsyncPostJson(String method, JsonNode requestBody) {
        // Build a new post request
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(requestBody.toString())).uri(URI.create(host + method)).header("Content-Type", "application/json").build();

        return httpClient
                // Sends the given request asynchronously using this client with the given response body handler
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                // Returns a new CompletionStage that, when this stage completes normally,
                // is executed with this stage's result as the argument to the supplied function.
                .thenApply(httpResponse -> {
                    // check response for exception and if true throw the exception
                    checkResponseAndThrowException(httpResponse);
                    // if no exception caused, return the response body converted to Json
                    return stringToJsonNode(httpResponse.body(), JsonNode.class);
                });
    }

    /**
     * The method check the status code of the response and throw exception if the code is not starts with digit 2
     *
     * @param httpResponse is the http response to check
     */
    private void checkResponseAndThrowException(HttpResponse<String> httpResponse) {
        // check the status code for 2**
        if (httpResponse.statusCode() / 100 != 2) {
            //if the code is not start with the digit 2, throw the RestException
            throw new RestException(httpResponse.body(), httpResponse.statusCode());
        }
    }

    /**
     * The method convert the string representation to given class
     *
     * @param json   is the string representation of Json
     * @param tClass is the class for conversion result
     * @param <T>    is the generic for setting the class
     * @return the result object of the conversion
     */
    private <T> T stringToJsonNode(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}