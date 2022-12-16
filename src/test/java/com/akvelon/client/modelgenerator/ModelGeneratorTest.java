package com.akvelon.client.modelgenerator;

import com.akvelon.client.exception.EmptyDirectoryPathException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class ModelGeneratorTest {
    private final static String HOST_URL = "http://example-mlem-get-started-app.herokuapp.com/";
    private final static String directoryPath = "src/main/java/com/akvelon/client/modelgenerator/";

    @Test
    @DisplayName("Test model generation empty directory path")
    public void testGenerationEmptyDirectoryPathException() {
        ModelGenerator modelGenerator = new ModelGenerator(HOST_URL);
        EmptyDirectoryPathException thrown = Assertions.assertThrows(EmptyDirectoryPathException.class, () -> modelGenerator.generate(""));
        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Test model generation")
    public void testGeneration() throws ExecutionException, InterruptedException {
        ExampleGenerator.main(null);

        Path path = Paths.get(directoryPath + "generated");
        Assertions.assertTrue(Files.exists(path));
        Assertions.assertTrue(deleteDirectory(path.toFile()));
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
