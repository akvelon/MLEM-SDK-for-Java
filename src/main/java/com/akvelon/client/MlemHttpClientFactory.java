package com.akvelon.client;

import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

/**
 * Factory class provides creating MlemHttpClient implementation
 */
public class MlemHttpClientFactory {
    /**
     * Create the implementation of MlemHttpClient
     *
     * @param host is the host URL
     * @return the new MlemHttpClient
     */
    public static MlemHttpClient createMlemHttpClient(String host, Logger logger) {
        return new MlemHttpClientImpl(host, logger);
    }

    /**
     * Create the implementation of MlemHttpClient
     *
     * @param executorService provides a pool of threads and an API for assigning tasks to it
     * @param host            is the host URL
     * @return the new MlemHttpClient
     */
    public static MlemHttpClient createMlemHttpClient(ExecutorService executorService, String host, Logger logger) {
        return new MlemHttpClientImpl(executorService, host, logger);
    }
}
