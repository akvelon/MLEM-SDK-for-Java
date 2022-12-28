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
    private final MlemJClient jClient;
    private final static String REQUEST_BODY_PATTERN_FILE_NAME = "request_body.mustache";
    private final static String RESPONSE_PATTERN_FILE_NAME = "response.mustache";
    private final static String DIRECTORY_AND_FILE_NAME = "generated/{0}.java";

    public ModelGenerator(String host) {
        this.jClient = MlemJClientFactory.createMlemJClient(host);
    }

    public void generate(String path, String packageName) throws ExecutionException, InterruptedException {
        if (path == null || path.isEmpty()) {
            throw new EmptyDirectoryPathException("Directory path must not be null or empty");
        }
        ApiSchema apiSchema = jClient.interfaceJsonAsync().get();
        generateRequestBodies(apiSchema, path, packageName);
        generateResponses(apiSchema, path, packageName);
    }

    private static void generateRequestBodies(ApiSchema apiSchema, String path, String packageName) {
        List<Context> requestBodiesContexts = SchemaToRequestBodyMapper.requestBodySchemasToContextList(apiSchema.getRequestBodySchemas(), packageName);
        generateClassesForPattern(requestBodiesContexts, REQUEST_BODY_PATTERN_FILE_NAME, path);
    }

    private static void generateResponses(ApiSchema apiSchema, String path, String packageName) {
        List<Context> responsesContexts = SchemaToResponseMapper.requestBodySchemasToContextList(apiSchema.getRequestBodySchemas(), packageName);
        generateClassesForPattern(responsesContexts, RESPONSE_PATTERN_FILE_NAME, path);
    }

    private static void generateClassesForPattern(List<Context> contextList, String pattern, String path) {
        for (Context context : contextList) {
            String content = Util.mustacheContent(context, pattern).replace("\\&lt;", "<").replace("\\&gt;", ">");
            Util.generateFile(MessageFormat.format(path + DIRECTORY_AND_FILE_NAME, context.className), content);
        }
    }
}