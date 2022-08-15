package com.akvelon.client;

import com.akvelon.client.exception.RestException;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.model.validation.RequestDesc;
import com.akvelon.client.model.validation.method.Method;
import com.akvelon.client.util.JsonMapper;
import com.akvelon.client.util.RequestValidator;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

    private final String host;

    private final HttpClient httpClient;
    private RequestDesc requestDesc;

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
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<JsonNode> interfaceJsonAsync() {
        return sendAsyncGetJson(GET_INTERFACE);
    }

    /**
     * The method sends the /predict post request.  The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    @Override
    public CompletableFuture<JsonNode> predict(JsonNode requestBody) {
        return sendAsyncPostJson(POST_PREDICT, requestBody);
    }

    /**
     * The method sends the /predict post request. The method can catch the exception via exceptionaly method.
     *
     * @param requestBody is the representation of the Request class.
     * @return a String response wrapped in the CompletableFuture object.
     */
    @Override
    public CompletableFuture<JsonNode> predict(Request requestBody) throws IOException, ExecutionException, InterruptedException {
        if (requestDesc == null) {
            return getSchemaAndDoPredict(POST_PREDICT, requestBody);
        }

        return sendAsyncPostJson(POST_PREDICT, requestBody.toJson());
    }

    @Override
    public CompletableFuture<JsonNode> predictWithValidation(Request requestBody) throws IOException, ExecutionException, InterruptedException {
        /*if (requestDesc == null) {
            return getSchemaAndDoPredict(requestBody);
        }*/

        return predict(requestBody);
    }

    /**
     * The method sends the post request. The method can catch the exception via exceptionaly method.
     *
     * @param methodName  is the method name for the request
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    @Override
    public CompletableFuture<JsonNode> call(String methodName, JsonNode requestBody) {
        return sendAsyncPostJson(methodName, requestBody);
    }

    /**
     * The method sends the post request. The method can catch the exception via exceptionaly method.
     *
     * @param methodName  is the method name for the request
     * @param requestBody is the Request representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    @Override
    public CompletableFuture<JsonNode> call(String methodName, Request requestBody) {
        return sendAsyncPostJson(methodName, requestBody.toJson());
    }

    /**
     * The method send the async get request for given method name.
     *
     * @param method is the name of request params
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    private CompletableFuture<JsonNode> sendAsyncGetJson(String method) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(host + method)).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(stringHttpResponse -> JsonMapper.readValue(stringHttpResponse.body(), JsonNode.class));
    }

    /**
     * The method send the async post request for given method name and request body
     *
     * @param method      is the name of request params
     * @param requestBody is the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    private CompletableFuture<JsonNode> sendAsyncPostJson(String method, JsonNode requestBody) {
        if (requestBody == null) {
            throw new NullPointerException();
        }
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
                    return JsonMapper.readValue(httpResponse.body(), JsonNode.class);
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

    private CompletableFuture<JsonNode> getSchemaAndDoPredict(String method, Request requestBody) throws ExecutionException, InterruptedException, IOException {
        JsonNode response = interfaceJsonAsync().exceptionally(throwable -> {
            RestException restException = (RestException) throwable.getCause();
            return null;
        }).get();

        if (response == null) {
            throw new NullPointerException();
        }

        Map<String, Method> jsonNodeMap = JsonMapper.readMap(response.get("methods"));
        if (jsonNodeMap == null || jsonNodeMap.isEmpty()) {
            throw new NullPointerException();
        }
        requestDesc = new RequestDesc(jsonNodeMap);
        RequestValidator.validateRequest(method, requestBody, requestDesc);
        return sendAsyncPostJson(method, requestBody.toJson());
    }
}