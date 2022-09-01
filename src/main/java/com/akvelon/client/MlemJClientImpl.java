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
 * An implementation of the MlemJClient.
 */
class MlemJClientImpl implements MlemJClient {
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
     * System.Logger instances log messages that will be routed to the underlying.
     * logging framework the LoggerFinder uses.
     */
    private final System.Logger logger;
    private final RequestParser requestParser;

    /**
     * Machine learning requests rules.
     */
    private InterfaceDesc interfaceDesc;

    /**
     * Constructor for creating the implementation of Mlem HttpClient.
     *
     * @param host   the host URL.
     * @param logger the events logger.
     */
    public MlemJClientImpl(String host, System.Logger logger) {
        this(null, host, logger);
    }

    /**
     * Constructor for creating the implementation of Mlem HttpClient.
     *
     * @param executorService provides a pool of threads and an API for assigning tasks to it.
     * @param host            the host URL.
     * @param logger          the events logger.
     */
    public MlemJClientImpl(ExecutorService executorService, String host, System.Logger logger) {
        //init the host
        this.host = host;
        //create a builder for a httpClient.
        HttpClient.Builder builder = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1);

        //check the executorService for null and set it to the httpClient.
        if (executorService != null) {
            builder.executor(executorService);
        }

        //build the httpClient.
        this.httpClient = builder.build();
        //init the logger.
        this.logger = logger;
        this.requestParser = new RequestParser(logger);
    }

    /**
     * Sends the /inteface.json request asynchronously. The method can catch the exception via exceptionally method.
     *
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    public CompletableFuture<JsonNode> interfaceJsonAsync() {
        // send the /inteface.json request.
        return sendGetRequest(GET_INTERFACE);
    }

    /**
     * Validates the requestBody by the given schema and sends the /predict request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param requestBody the Json representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<JsonNode> predict(JsonNode requestBody) throws IOException, ExecutionException, InterruptedException {
        // convert json body to Request representation.
        Request body = requestParser.parseRequest(requestBody);
        // validate and send the /predict request with given body.
        return validateAndSendRequest(POST_PREDICT, body);
    }

    /**
     * Validates the requestBody by the given schema and sends the /predict request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param requestBody the representation of the Request class.
     * @return a String response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<JsonNode> predict(Request requestBody) throws IOException, ExecutionException, InterruptedException {
        // validate and send /predict request with given body.
        return validateAndSendRequest(POST_PREDICT, requestBody);
    }

    /**
     * Validates the requestBody by the given schema and sends the post request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param methodName  the method name for the request.
     * @param requestBody the JsonNode representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<JsonNode> call(String methodName, JsonNode requestBody) throws IOException, ExecutionException, InterruptedException {
        // convert json body to Request representation.
        Request body = requestParser.parseRequest(requestBody);
        // validate and send /call request with given methodName and body.
        return validateAndSendRequest(methodName, body);
    }

    /**
     * Validates the requestBody by the given schema and sends the post request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param methodName  the method name for the request.
     * @param requestBody the Request representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<JsonNode> call(String methodName, Request requestBody) throws IOException, ExecutionException, InterruptedException {
        // validate and send /call request with given methodName and body.
        return validateAndSendRequest(methodName, requestBody);
    }

    /**
     * Download and parse the schema if necessary. Then validate the request by schema and send the request.
     *
     * @param method  the method name for the request.
     * @param request the Request representation of the request.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    private synchronized CompletableFuture<JsonNode> validateAndSendRequest(String method, Request request) throws IOException, ExecutionException, InterruptedException {
        // check schema for null.
        if (interfaceDesc == null) {
            // if true, send /interface.json request to obtain the schema.
            JsonNode schema = interfaceJsonAsync()
                    .exceptionally(
                            // in case of exception.
                            throwable -> {
                                // create the instance of RestException.
                                RestException restException = (RestException) throwable.getCause();
                                // log the exception.
                                logger.log(
                                        System.Logger.Level.ERROR,
                                        "an exception in /interface.json request",
                                        restException
                                );
                                return null;
                            })
                    .get(); // get the result from CompletableFuture.

            // convert json to InterfaceDesc.
            interfaceDesc = requestParser.parseInterfaceSchema(schema);
        }

        // validate request by given schema.
        RequestValidator requestValidator = new RequestValidator(logger);
        requestValidator.validateRequest(method, request, interfaceDesc);

        // send post request with given method and request.
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
        // check request body for null.
        assert requestBody != null : "The body is null for method: " + method;

        // Build a new post request.
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .uri(URI.create(host + method))
                .header("Content-Type", "application/json")
                .build();

        //log the host.
        logger.log(System.Logger.Level.INFO, "host: " + host);
        //log the method.
        logger.log(System.Logger.Level.INFO, "method: " + method);

        // check response for exception and if true throw the exception.
        return httpClient
                // Sends the given request asynchronously using this client with the given response body handler.
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
        // Build a new get request
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(host + method))
                .build();

        //log the host.
        logger.log(System.Logger.Level.INFO, "host: " + host);
        //log the method.
        logger.log(System.Logger.Level.INFO, "method: " + method);

        return httpClient
                // Sends the given request asynchronously.
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                // Returns a new CompletionStage that, when this stage completes normally,
                // is executed with this stage's result as the argument to the supplied function.
                .thenApply(this::checkAndReadResponse);
    }

    /**
     * Checks the response for 200 status code and convert the string body to JsonNode.
     *
     * @param httpResponse contains the response status, headers, and body.
     * @return a response body.
     */
    private JsonNode checkAndReadResponse(HttpResponse<String> httpResponse) {
        // check http response status code.
        checkStatusCode(httpResponse);

        try {
            // convert the http response to json and return.
            return JsonMapper.readValue(httpResponse.body(), JsonNode.class);
        } catch (JsonProcessingException e) {
            // in case of exception log an information.
            logger.log(System.Logger.Level.ERROR, "JsonMapper read body exception", e);
            // and throw the exception.
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks the status code of the response and throw exception if the code is not starts with digit 2.
     *
     * @param httpResponse is the http response to check.
     */
    private void checkStatusCode(HttpResponse<String> httpResponse) {
        // check the status code for 200.
        if (httpResponse.statusCode() != 200) {
            //if true, throw the RestException.
            throw new RestException(httpResponse.body(), httpResponse.statusCode());
        }

        // log the status code.
        logger.log(System.Logger.Level.INFO, httpResponse.statusCode());
    }
}