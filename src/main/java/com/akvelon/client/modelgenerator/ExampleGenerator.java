package com.akvelon.client.modelgenerator;

import java.util.concurrent.ExecutionException;

/**
 * An example of using model generator.
 */
public final class ExampleGenerator {
    public static final String packageName = "com.akvelon.client.modelgenerator.generated;";
    private final static String HOST_URL = "http://example-mlem-get-started-app.herokuapp.com/";
    private final static String path = "src/main/java/com/akvelon/client/modelgenerator/";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ModelGenerator modelGenerator = new ModelGenerator(HOST_URL);
        modelGenerator.generate(path, packageName);
    }
}