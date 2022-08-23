package com.akvelon.client;

import com.akvelon.client.exception.RestException;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.model.validation.InterfaceDesc;
import com.akvelon.client.util.JsonMapper;
import com.akvelon.client.util.RequestParser;
import com.akvelon.client.util.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * Provides the functionality for Mlem API.
 */
class MlemHttpClientImpl implements MlemHttpClient {
    /**
     * /interface.json get-method.
     */
    private final String GET_INTERFACE = "interface.json";
    /**
     * /predict post-method.
     */
    private final String POST_PREDICT = "predict";

    /**
     * the server url host.
     */
    private final String host;

    /**
     * An HttpClient can be used to send requests and retrieve their responses.
     */
    private final HttpClient httpClient;
    /**
     * System.Logger instances log messages that will be routed to the underlying
     * logging framework the LoggerFinder uses.
     */
    private final System.Logger logger;

    /**
     * Machine learning requests rules.
     */
    private JsonNode schema;

    /**
     * Constructor for creating the implementation of Mlem HttpClient.
     *
     * @param host   the host URL.
     * @param logger the events logger.
     */
    public MlemHttpClientImpl(String host, System.Logger logger) {
        this(null, host, logger);
    }

    /**
     * Constructor for creating the implementation of Mlem HttpClient.
     *
     * @param executorService provides a pool of threads and an API for assigning tasks to it.
     * @param host            the host URL.
     * @param logger          the events logger.
     */
    public MlemHttpClientImpl(ExecutorService executorService, String host, System.Logger logger) {
        //init the host
        this.host = host;
        //create a builder for a httpClient
        HttpClient.Builder builder = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1);

        //check the executorService for null and set it to the httpClient
        if (executorService != null) {
            builder.executor(executorService);
        }

        //build the httpClient
        this.httpClient = builder.build();
        //init the logger
        this.logger = logger;
    }

    /**
     * Sends the /inteface.json request asynchronously. The method can catch the exception via exceptionally method.
     *
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<JsonNode> interfaceJsonAsync() {
        return sendGetRequest(GET_INTERFACE);
    }

    /**
     * Validates the requestBody by the given schema and sends the /predict request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param requestBody the Json representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type
     * @throws ExecutionException   if this future completed exceptionally
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    @Override
    public CompletableFuture<JsonNode> predict(JsonNode requestBody) throws IOException, ExecutionException, InterruptedException {
        Request body = RequestParser.parseRequest(requestBody);
        return validateAndSendRequest(POST_PREDICT, body);
    }

    /**
     * Validates the requestBody by the given schema and sends the /predict request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param requestBody the representation of the Request class.
     * @return a String response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type
     * @throws ExecutionException   if this future completed exceptionally
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    @Override
    public CompletableFuture<JsonNode> predict(Request requestBody) throws IOException, ExecutionException, InterruptedException {
        return validateAndSendRequest(POST_PREDICT, requestBody);
    }

    /**
     * Validates the requestBody by the given schema and sends the post request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param methodName  the method name for the request.
     * @param requestBody the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type
     * @throws ExecutionException   if this future completed exceptionally
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    @Override
    public CompletableFuture<JsonNode> call(String methodName, JsonNode requestBody) throws IOException, ExecutionException, InterruptedException {
        Request body = RequestParser.parseRequest(requestBody);
        return validateAndSendRequest(methodName, body);
    }

    /**
     * Validates the requestBody by the given schema and sends the post request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param methodName  the method name for the request.
     * @param requestBody the Request representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type
     * @throws ExecutionException   if this future completed exceptionally
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    @Override
    public CompletableFuture<JsonNode> call(String methodName, Request requestBody) throws IOException, ExecutionException, InterruptedException {
        return validateAndSendRequest(methodName, requestBody);
    }

    /**
     * Download and parse the schema if necessary. Then validate the request by schema and send the request.
     *
     * @param method  the method name for the request.
     * @param request the Request representation of the request
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type
     * @throws ExecutionException   if this future completed exceptionally
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    private synchronized CompletableFuture<JsonNode> validateAndSendRequest(String method, Request request) throws IOException, ExecutionException, InterruptedException {
        if (schema == null) {
            schema = interfaceJsonAsync().exceptionally(throwable -> {
                RestException restException = (RestException) throwable.getCause();
                logger.log(System.Logger.Level.ERROR, "the http response status code is " + restException.getStatusCode(), restException);
                return null;
            }).get();
        }

        InterfaceDesc interfaceDesc = RequestParser.parseInterfaceSchema(schema);
        RequestValidator.validateRequest(method, request, interfaceDesc);

        return sendPostRequest(method, request.toJson());
    }

    /**
     * Send the async post request asynchronously for given method name and request body.
     *
     * @param method      the name of request params.
     * @param requestBody the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    private CompletableFuture<JsonNode> sendPostRequest(String method, JsonNode requestBody) {
        if (requestBody == null) {
            logger.log(System.Logger.Level.ERROR, "The body is null for method: " + method);
            throw new NullPointerException();
        }

        // Build a new post request
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(requestBody.toString())).uri(URI.create(host + method)).header("Content-Type", "application/json").build();

        logger.log(System.Logger.Level.INFO, "host: " + host);
        logger.log(System.Logger.Level.INFO, "method: " + method);

        // check response for exception and if true throw the exception
        return httpClient
                // Sends the given request asynchronously using this client with the given response body handler
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                // Returns a new CompletionStage that, when this stage completes normally,
                // is executed with this stage's result as the argument to the supplied function.
                .thenApply(this::checkAndReadResponse);
    }

    /**
     * Send the get request asynchronously for given method name.
     *
     * @param method the name of request params.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    private CompletableFuture<JsonNode> sendGetRequest(String method) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(host + method)).build();

        logger.log(System.Logger.Level.INFO, "host: " + host);
        logger.log(System.Logger.Level.INFO, "method: " + method);

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::checkAndReadResponse);
    }

    /**
     * Checks the response for 2** status code and convert the string body to JsonNode
     *
     * @param httpResponse contains the response status, headers, and body
     * @return a response body
     */
    private JsonNode checkAndReadResponse(HttpResponse<String> httpResponse) {
        checkStatusCode(httpResponse);

        try {
            // if no exception caused, return the response body converted to Json
            return JsonMapper.readValue(httpResponse.body(), JsonNode.class);
        } catch (JsonProcessingException e) {
            // in case of exception log an information
            logger.log(System.Logger.Level.ERROR, "JsonMapper read body exception", e);
            // and throw the exception
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks the status code of the response and throw exception if the code is not starts with digit 2
     *
     * @param httpResponse is the http response to check
     */
    private void checkStatusCode(HttpResponse<String> httpResponse) {
        // check the status code for 2**
        if (httpResponse.statusCode() / 100 != 2) {
            //if the code is not start with the digit 2, throw the RestException
            throw new RestException(httpResponse.body(), httpResponse.statusCode());
        }

        logger.log(System.Logger.Level.INFO, httpResponse.statusCode());
    }
}