package com.akvelon.client;

import java.util.concurrent.ExecutorService;

/**
 * A Factory that provides creating MlemJClient implementation.
 */
public class MlemJClientFactory {
    /**
     * Create the implementation of MlemJClient.
     *
     * @param host   the host URL.
     * @param logger the events logger.
     * @return the new MlemJClient.
     */
    public static MlemJClient createMlemJClient(String host, System.Logger logger) {
        return new MlemJClientImpl(host, logger);
    }

    /**
     * Create the implementation of MlemJClient.
     *
     * @param executorService provides a pool of threads and an API for assigning tasks to it.
     * @param host            the host URL.
     * @param logger          the events logger.
     * @return the new MlemJClient.
     */
    public static MlemJClient createMlemJClient(ExecutorService executorService, String host, System.Logger logger) {
        return new MlemJClientImpl(executorService, host, logger);
    }
}
