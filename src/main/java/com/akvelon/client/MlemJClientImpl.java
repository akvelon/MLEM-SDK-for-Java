package com.akvelon.client;

import com.akvelon.client.exception.InvalidHttpStatusCodeException;
import com.akvelon.client.model.request.RequestBody;
import com.akvelon.client.model.request.typical.IrisBody;
import com.akvelon.client.model.request.typical.RegModelBody;
import com.akvelon.client.model.validation.ApiSchema;
import com.akvelon.client.util.ApiValidator;
import com.akvelon.client.util.JsonMapper;
import com.akvelon.client.util.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * An implementation of the MlemJClient.
 */
final class MlemJClientImpl implements MlemJClient {
    /**
     * /interface.json path.
     */
    private final String GET_INTERFACE = "interface.json";
    /**
     * /predict path.
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
    private final JsonParser jsonParser;

    /**
     * Requests bodies schema.
     */
    private ApiSchema apiSchema;
    private final boolean validation;

    /**
     * Constructor for creating the implementation of Mlem HttpClient.
     *
     * @param host the host URL.
     */
    public MlemJClientImpl(String host) {
        this(null, host, null, false);
    }

    /**
     * Constructor for creating the implementation of Mlem HttpClient.
     *
     * @param host       the host URL.
     * @param validation the validation switcher.
     */
    public MlemJClientImpl(String host, boolean validation) {
        this(null, host, null, validation);
    }

    /**
     * Constructor for creating the implementation of Mlem HttpClient.
     *
     * @param host   the host URL.
     * @param logger the events logger.
     */
    public MlemJClientImpl(String host, System.Logger logger) {
        this(null, host, logger, false);
    }

    /**
     * Constructor for creating the implementation of Mlem HttpClient.
     *
     * @param host       the host URL.
     * @param logger     the events logger.
     * @param validation the validation switcher.
     */
    public MlemJClientImpl(String host, System.Logger logger, boolean validation) {
        this(null, host, logger, validation);
    }

    /**
     * Constructor for creating the implementation of Mlem HttpClient.
     *
     * @param executorService provides a pool of threads and an API for assigning tasks to it.
     * @param host            the host URL.
     * @param logger          the events logger.
     */
    public MlemJClientImpl(ExecutorService executorService, String host, System.Logger logger) {
        this(executorService, host, logger, false);
    }

    /**
     * Constructor for creating the implementation of Mlem HttpClient.
     *
     * @param executorService provides a pool of threads and an API for assigning tasks to it.
     * @param host            the host URL.
     * @param logger          the events logger.
     * @param validation      the validation switcher.
     */
    public MlemJClientImpl(ExecutorService executorService, String host, System.Logger logger, boolean validation) {
        this.host = host;
        HttpClient.Builder builder = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1);

        if (executorService != null) {
            builder.executor(executorService);
        }

        this.httpClient = builder.build();
        this.logger = logger;
        this.jsonParser = new JsonParser(logger);
        this.validation = validation;
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
     * @param requestBody the requests data represented in JsonNode class.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<JsonNode> predict(JsonNode requestBody) throws IOException, ExecutionException, InterruptedException {
        RequestBody body = jsonParser.parseRequestBody(requestBody);
        return validateAndSendRequest(POST_PREDICT, body);
    }

    /**
     * Validates the requestBody by the given schema and sends the /predict request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param requestBody the requests data represented in RequestBody class.
     * @return a String response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<JsonNode> predict(RequestBody requestBody) throws IOException, ExecutionException, InterruptedException {
        return validateAndSendRequest(POST_PREDICT, requestBody);
    }

    /**
     * Validates the request body by the given schema and sends the /predict request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param requestBody the requests data represented in IrisBody class.
     * @return a List of numbers wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<List<Long>> predict(IrisBody requestBody) throws IOException, ExecutionException, InterruptedException {
        CompletableFuture<JsonNode> jsonNodeCompletableFuture = validateAndSendRequest(POST_PREDICT, requestBody);
        return jsonNodeCompletableFuture.thenApply(jsonNode -> {
            try {
                return JsonMapper.readList(jsonNode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Validates the request body by the given schema and sends the /predict request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param requestBody the requests data represented in RegModelBody class.
     * @return a List of numbers wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<List<Long>> predict(RegModelBody requestBody) throws IOException, ExecutionException, InterruptedException {
        CompletableFuture<JsonNode> jsonNodeCompletableFuture = validateAndSendRequest(POST_PREDICT, requestBody);
        return jsonNodeCompletableFuture.thenApply(jsonNode -> {
            try {
                return JsonMapper.readList(jsonNode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * The method sends the list of /predict post requests with given JSON body.
     *
     * @param requestBody the requests data.
     * @return a list of responses wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<List<JsonNode>> predict(List<RequestBody> requestBody) throws IOException, ExecutionException, InterruptedException {
        List<CompletableFuture<JsonNode>> completableFutures = new ArrayList<>();
        for (RequestBody jsonNode : requestBody) {
            CompletableFuture<JsonNode> predict = predict(jsonNode);
            completableFutures.add(predict);
        }

        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
    }

    /**
     * Validates the requestBody by the given schema and sends the post request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in JsonNode class.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<JsonNode> call(String path, JsonNode requestBody) throws IOException, ExecutionException, InterruptedException {
        RequestBody body = jsonParser.parseRequestBody(requestBody);
        return validateAndSendRequest(path, body);
    }

    /**
     * Validates the requestBody by the given schema and sends the post request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in RequestBody class.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<JsonNode> call(String path, RequestBody requestBody) throws IOException, ExecutionException, InterruptedException {
        return validateAndSendRequest(path, requestBody);
    }

    /**
     * The method sends the post request with given method and a list of bodies.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in JsonNode class
     * @return a list of responses wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<List<JsonNode>> call(String path, List<RequestBody> requestBody) throws IOException, ExecutionException, InterruptedException {
        List<CompletableFuture<JsonNode>> completableFutures = new ArrayList<>();
        for (RequestBody jsonNode : requestBody) {
            CompletableFuture<JsonNode> call = call(path, jsonNode);
            completableFutures.add(call);
        }

        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
    }

    /**
     * Validates the Iris requestBody by the given schema and sends the post request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in IrisBody class.
     * @return a List of numbers wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<List<Long>> call(String path, IrisBody requestBody) throws IOException, ExecutionException, InterruptedException {
        CompletableFuture<JsonNode> jsonNodeCompletableFuture = validateAndSendRequest(path, requestBody);

        return jsonNodeCompletableFuture.thenApply(jsonNode -> {
            try {
                return JsonMapper.readList(jsonNode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Validates the Iris requestBody by the given schema and sends the post request asynchronously.
     * The method can catch the exception via exceptionally method.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in RegModelBody class.
     * @return a List of numbers wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    @Override
    public CompletableFuture<List<Long>> call(String path, RegModelBody requestBody) throws IOException, ExecutionException, InterruptedException {
        CompletableFuture<JsonNode> jsonNodeCompletableFuture = validateAndSendRequest(path, requestBody);

        return jsonNodeCompletableFuture.thenApply(jsonNode -> {
            try {
                return JsonMapper.readList(jsonNode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Download and parse the schema if necessary. Then validate the request by schema and send the request.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in RequestBody class.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    private synchronized CompletableFuture<JsonNode> validateAndSendRequest(String path, RequestBody requestBody) throws IOException, ExecutionException, InterruptedException {
        assert path != null && !path.isEmpty() : "the path is null or empty";
        assert requestBody != null : "the request body is null";

        if (validation) {
            if (apiSchema == null) {
                // if true, send /interface.json request to obtain the schema.
                JsonNode schema = interfaceJsonAsync()
                        .exceptionally(
                                throwable -> {
                                    InvalidHttpStatusCodeException invalidHttpStatusCodeException = (InvalidHttpStatusCodeException) throwable.getCause();
                                    if (logger != null) {
                                        logger.log(
                                                System.Logger.Level.ERROR,
                                                "an exception in /interface.json request",
                                                invalidHttpStatusCodeException
                                        );
                                    }

                                    throw invalidHttpStatusCodeException;
                                })
                        .get();

                apiSchema = jsonParser.parseApiSchema(schema);
            }

            ApiValidator apiValidator = new ApiValidator(logger);
            apiValidator.validateRequestBody(path, requestBody, apiSchema);
        }

        return sendPostRequest(path, requestBody.toJson());
    }

    /**
     * Send the async post request asynchronously for given method name and request body.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in JsonNode class.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    private CompletableFuture<JsonNode> sendPostRequest(String path, JsonNode requestBody) {
        assert requestBody != null : "The body is null for path: " + path;

        String url = host + path;
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .build();

        if (logger != null) logger.log(System.Logger.Level.INFO, "url: " + url);

        return httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                // Returns a new CompletionStage that, when this stage completes normally,
                // is executed with this stage's result as the argument to the supplied function.
                .thenApply(this::checkAndReadResponse)
                .thenApply(jsonNode -> validateResponse(path, jsonNode));
    }

    /**
     * Send the get request asynchronously for given method name.
     *
     * @param path the specific resource in the host that the client wants to access.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    private CompletableFuture<JsonNode> sendGetRequest(String path) {
        String url = host + path;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        if (logger != null) logger.log(System.Logger.Level.INFO, "url: " + url);

        return httpClient
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
        checkStatusCode(httpResponse);

        try {
            return JsonMapper.readValue(httpResponse.body(), JsonNode.class);
        } catch (JsonProcessingException e) {
            if (logger != null) logger.log(System.Logger.Level.ERROR, "JsonMapper read body exception", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks the status code of the response and throw exception if the code is not starts with digit 2.
     *
     * @param httpResponse is the http response to check.
     */
    private void checkStatusCode(HttpResponse<String> httpResponse) {
        if (httpResponse.statusCode() != 200) {
            throw new InvalidHttpStatusCodeException(httpResponse.body(), httpResponse.statusCode());
        }

        if (logger != null) logger.log(System.Logger.Level.INFO, httpResponse.statusCode());
    }


    private JsonNode validateResponse(String path, JsonNode jsonNode) {
        if (apiSchema != null) {
            ApiValidator responseValidator = new ApiValidator(logger);
            responseValidator.validateResponse(path, jsonNode, apiSchema);
        }

        return jsonNode;
    }
}