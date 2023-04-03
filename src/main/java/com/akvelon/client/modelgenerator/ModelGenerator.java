package com.akvelon.client.modelgenerator;

import com.akvelon.client.MlemJClient;
import com.akvelon.client.MlemJClientFactory;
import com.akvelon.client.exception.EmptyDirectoryPathException;
import com.akvelon.client.model.validation.ApiSchema;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A class that provides a Java-classes generation.
 */
public class ModelGenerator {
    private final static String REQUEST_BODY_PATTERN_FILE_NAME = "request_body.mustache";
    private final static String RESPONSE_PATTERN_FILE_NAME = "response.mustache";
    private final static String DIRECTORY_AND_FILE_NAME = "generated/{0}.java";
    private final MlemJClient jClient;

    /**
     * Create the ModelGenerator with jClient.
     *
     * @param host the host URL.
     */
    public ModelGenerator(String host) {
        this.jClient = MlemJClientFactory.createMlemJClient(host);
    }

    /*
     * Generate classes for requests bodies.
     * The mustache pattern for request body stored in resource directory: request_body.mustache.
     *
     * @param apiSchema   the container for the requests bodies descriptions.
     * @param path        the path to the file.
     * @param packageName the name of the package.
     */
    private static void generateRequestBodies(ApiSchema apiSchema, String path, String packageName) {
        List<Context> requestBodiesContexts = SchemaToRequestBodyMapper.requestBodySchemasToContextList(apiSchema.getRequestBodySchemas(), packageName);
        generateClassesForPattern(requestBodiesContexts, REQUEST_BODY_PATTERN_FILE_NAME, path);
    }

    /*
     * Generate classes for response bodies.
     * The mustache pattern for response body stored in resource directory: response.mustache.
     *
     * @param apiSchema   the container for the requests bodies descriptions.
     * @param path        the path to the file.
     * @param packageName the name of the package.
     */
    private static void generateResponses(ApiSchema apiSchema, String path, String packageName) {
        List<Context> responsesContexts = SchemaToResponseMapper.requestBodySchemasToContextList(apiSchema.getRequestBodySchemas(), packageName);
        generateClassesForPattern(responsesContexts, RESPONSE_PATTERN_FILE_NAME, path);
    }

    /**
     * Generate classes by given context list, mustache pattern and path to the file.
     *
     * @param contextList the list of Context object provided information
     * @param pattern     the mustache pattern.
     * @param path        the path to the file.
     */
    private static void generateClassesForPattern(List<Context> contextList, String pattern, String path) {
        for (Context context : contextList) {
            String content = Util.mustacheContent(context, pattern).replace("\\&lt;", "<").replace("\\&gt;", ">");
            Util.generateFile(MessageFormat.format(path + DIRECTORY_AND_FILE_NAME, context.className), content);
        }
    }

    /**
     * Generate classes by given directory and package name with mustache pattern.
     * The mustache patterns stored in resource directory.
     *
     * @param path        the path to the file.
     * @param packageName the name of the package.
     * @throws ExecutionException   Exception thrown when attempting to retrieve the result
     *                              of a task that aborted by throwing an exception.
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied,
     *                              and the thread is interrupted, either before or during the activity.
     */
    public void generate(String path, String packageName) throws ExecutionException, InterruptedException {
        if (path == null || path.isEmpty()) {
            throw new EmptyDirectoryPathException("Directory path must not be null or empty");
        }
        ApiSchema apiSchema = jClient.interfaceJsonAsync().get();
        generateRequestBodies(apiSchema, path, packageName);
        generateResponses(apiSchema, path, packageName);
    }
}