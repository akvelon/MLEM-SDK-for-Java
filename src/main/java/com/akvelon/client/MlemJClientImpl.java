package com.akvelon.client;

import com.akvelon.client.exception.InvalidHttpStatusCodeException;
import com.akvelon.client.model.request.RequestBody;
import com.akvelon.client.model.response.ResponseBody;
import com.akvelon.client.model.validation.ApiSchema;
import com.akvelon.client.resources.EM;
import com.akvelon.client.util.ApiValidator;
import com.akvelon.client.util.JsonMapper;
import com.akvelon.client.util.JsonParser;
import com.akvelon.client.util.Logger;
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

    private final JsonParser jsonParser;

    /**
     * Requests bodies schema.
     */
    private ApiSchema apiSchema;
    private final boolean validationOn;

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
     * @param host         the host URL.
     * @param validationOn the validation switcher.
     */
    public MlemJClientImpl(String host, boolean validationOn) {
        this(null, host, null, validationOn);
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
     * @param host         the host URL.
     * @param logger       the events logger.
     * @param validationOn the validation switcher.
     */
    public MlemJClientImpl(String host, System.Logger logger, boolean validationOn) {
        this(null, host, logger, validationOn);
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
     * @param validationOn    the validation switcher.
     */
    public MlemJClientImpl(ExecutorService executorService, String host, System.Logger logger, boolean validationOn) {
        this.host = host;
        HttpClient.Builder builder = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1);

        if (executorService != null) {
            builder.executor(executorService);
        }

        this.httpClient = builder.build();
        Logger.getInstance().setLogger(logger);
        this.jsonParser = new JsonParser();
        this.validationOn = validationOn;
    }

    public CompletableFuture<ApiSchema> interfaceJsonAsync() {
        return sendGetRequest(GET_INTERFACE)
                .thenApply(jsonNode -> {
                    try {
                        return jsonParser.parseApiSchema(jsonNode);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .exceptionally(
                        throwable -> {
                            RuntimeException invalidHttpStatusCodeException = (RuntimeException) throwable.getCause();
                            Logger.getInstance().log(
                                    System.Logger.Level.ERROR,
                                    "an exception in /interface.json request",
                                    invalidHttpStatusCodeException
                            );

                            throw invalidHttpStatusCodeException;
                        });
    }

    @Override
    public CompletableFuture<JsonNode> predict(JsonNode requestBody) throws IOException, ExecutionException, InterruptedException {
        RequestBody body = jsonParser.parseRequestBody(requestBody);
        return validateAndSendRequest(POST_PREDICT, body);
    }

    @Override
    public <RequestT extends RequestBody, ResponseT extends ResponseBody> CompletableFuture<ResponseT> predict(RequestT requestBody) throws IOException, ExecutionException, InterruptedException {
        return validateAndSendRequest(POST_PREDICT, requestBody).thenApply(jsonNode -> {
            ResponseT responseBody = (ResponseT) requestBody.createResponse();
            try {
                responseBody.parseJson(jsonNode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return responseBody;
        });
    }

    @Override
    public <RequestT extends RequestBody, ResponseT extends ResponseBody> CompletableFuture<List<ResponseT>> predict(List<RequestT> requestBody) throws IOException, ExecutionException, InterruptedException {
        List<CompletableFuture<ResponseT>> completableFutures = new ArrayList<>();
        for (RequestT jsonNode : requestBody) {
            CompletableFuture<ResponseT> predict = predict(jsonNode);
            completableFutures.add(predict);
        }

        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).thenApply(v -> completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<JsonNode> call(String path, JsonNode requestBody) throws IOException, ExecutionException, InterruptedException {
        RequestBody body = jsonParser.parseRequestBody(requestBody);
        return validateAndSendRequest(path, body);
    }

    @Override
    public <RequestT extends RequestBody, ResponseT extends ResponseBody> CompletableFuture<ResponseT> call(String path, RequestT requestBody) throws IOException, ExecutionException, InterruptedException {
        return validateAndSendRequest(path, requestBody).thenApply(jsonNode -> {
            ResponseT responseBody = (ResponseT) requestBody.createResponse();
            try {
                responseBody.parseJson(jsonNode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return responseBody;
        });
    }

    @Override
    public <RequestT extends RequestBody, ResponseT extends ResponseBody> CompletableFuture<List<ResponseT>> call(String path, List<RequestT> requestBody) throws IOException, ExecutionException, InterruptedException {
        List<CompletableFuture<ResponseT>> completableFutures = new ArrayList<>();
        for (RequestT jsonNode : requestBody) {
            CompletableFuture<ResponseT> call = call(path, jsonNode);
            completableFutures.add(call);
        }

        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).thenApply(v -> completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    /**
     * Download and parse the schema if necessary. Then validate the request by schema and send the request.
     *
     * @param path        the specific resource in the host that the client wants to access.
     * @param requestBody the requests data represented in RequestBody class.
     * @return a RequestBody response wrapped in the CompletableFuture object.
     * @throws IOException          will be thrown if input can not be detected as JsonNode type.
     * @throws ExecutionException   if this future completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    private synchronized <T extends RequestBody> CompletableFuture<JsonNode> validateAndSendRequest(String path, T requestBody) throws IOException, ExecutionException, InterruptedException {
        assert path != null && !path.isEmpty() : String.format(EM.InputValueIsEmpty, "path");
        assert requestBody != null : String.format(EM.InputValueIsNull, "requestBody");

        if (validationOn) {
            if (apiSchema == null) {
                // if true, send /interface.json request to obtain the schema.
                apiSchema = interfaceJsonAsync().get();
            }

            ApiValidator apiValidator = new ApiValidator();
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
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(requestBody.toString())).uri(URI.create(url)).header("Content-Type", "application/json").build();

        Logger.getInstance().log(System.Logger.Level.INFO, "url: " + url);

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                // Returns a new CompletionStage that, when this stage completes normally,
                // is executed with this stage's result as the argument to the supplied function.
                .thenApply(this::checkAndReadResponse).thenApply(jsonNode -> validateResponse(path, jsonNode));
    }

    /**
     * Send the get request asynchronously for given method name.
     *
     * @param path the specific resource in the host that the client wants to access.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    private CompletableFuture<JsonNode> sendGetRequest(String path) {
        String url = host + path;
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();

        Logger.getInstance().log(System.Logger.Level.INFO, "url: " + url);

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
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
            Logger.getInstance().log(System.Logger.Level.ERROR, "JsonMapper read body exception", e);
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

        Logger.getInstance().log(System.Logger.Level.INFO, httpResponse.toString());
    }

    /**
     * Validate Response object by given schema represented in ApiSchema.
     *
     * @param path     the method name for the request.
     * @param jsonNode the Json object to validate.
     * @return a JsonNode response wrapped in the CompletableFuture object.
     */
    private JsonNode validateResponse(String path, JsonNode jsonNode) {
        if (apiSchema != null) {
            ApiValidator responseValidator = new ApiValidator();
            responseValidator.validateResponse(path, jsonNode, apiSchema);
        }

        return jsonNode;
    }
}