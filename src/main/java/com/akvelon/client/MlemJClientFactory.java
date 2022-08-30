package com.akvelon.client;

import java.util.concurrent.ExecutorService;

/**
 * Factory class provides creating MlemHttpClient implementation.
 */
public class MlemJClientFactory {
    /**
     * Create the implementation of MlemHttpClient.
     *
     * @param host   the host URL.
     * @param logger the events logger.
     * @return the new MlemHttpClient.
     */
    public static MlemJClient createMlemHttpClient(String host, System.Logger logger) {
        return new MlemJClientImpl(host, logger);
    }

    /**
     * Create the implementation of MlemHttpClient.
     *
     * @param executorService provides a pool of threads and an API for assigning tasks to it.
     * @param host            the host URL.
     * @param logger          the events logger.
     * @return the new MlemHttpClient.
     */
    public static MlemJClient createMlemHttpClient(ExecutorService executorService, String host, System.Logger logger) {
        return new MlemJClientImpl(executorService, host, logger);
    }
}
