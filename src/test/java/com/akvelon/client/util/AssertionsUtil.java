package com.akvelon.client.util;

import com.akvelon.client.exception.InvalidHttpStatusCodeException;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AssertionsUtil {
    public static void assertResponseString(String response) {
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());
    }

    public static <T> void assertResponseJsonOrHandleException(CompletableFuture<T> future) throws ExecutionException, InterruptedException {
        Assertions.assertNotNull(future);

        T response = future
                .exceptionally(throwable -> {
                    InvalidHttpStatusCodeException invalidHttpStatusCodeException = (InvalidHttpStatusCodeException) throwable.getCause();
                    assertResponseException(invalidHttpStatusCodeException);
                    return null;
                })
                .get();

        if (response == null) {
            return;
        }

        Assertions.assertNotNull(response);
    }

    public static <T> void assertResponseListOrHandleException(CompletableFuture<List<T>> future) throws ExecutionException, InterruptedException {
        Assertions.assertNotNull(future);

        List<T> response = future
                .exceptionally(throwable -> {
                    InvalidHttpStatusCodeException invalidHttpStatusCodeException = (InvalidHttpStatusCodeException) throwable.getCause();
                    assertResponseException(invalidHttpStatusCodeException);
                    return null;
                })
                .get();

        if (response == null) {
            return;
        }

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());
    }

    public static void assertResponseException(InvalidHttpStatusCodeException invalidHttpStatusCodeException) {
        Assertions.assertNotNull(invalidHttpStatusCodeException);
        Assertions.assertNotNull(invalidHttpStatusCodeException.getMessage(), invalidHttpStatusCodeException.getMessage());
        assertResponseString(invalidHttpStatusCodeException.getMessage());
    }
}
