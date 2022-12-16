package com.akvelon.client;

import java.util.concurrent.ExecutorService;

/**
 * A Factory that provides creating MlemJClient implementation.
 */
public final class MlemJClientFactory {
    /**
     * Create the implementation of MlemJClient.
     *
     * @param host the host URL.
     * @return the new MlemJClient.
     */
    public static MlemJClientImpl createMlemJClient(String host) {
        return new MlemJClientImpl(host);
    }

    /**
     * Create the implementation of MlemJClient.
     *
     * @param host       the host URL.
     * @param validationOn the validation switcher.
     * @return the new MlemJClient.
     */
    public static MlemJClient createMlemJClient(String host, boolean validationOn) {
        return new MlemJClientImpl(host, validationOn);
    }

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
     * @param host       the host URL.
     * @param logger     the events logger.
     * @param validationOn the validation switcher.
     * @return the new MlemJClient.
     */
    public static MlemJClient createMlemJClient(String host, System.Logger logger, boolean validationOn) {
        return new MlemJClientImpl(host, logger, validationOn);
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

    /**
     * Create the implementation of MlemJClient.
     *
     * @param executorService provides a pool of threads and an API for assigning tasks to it.
     * @param host            the host URL.
     * @param logger          the events logger.
     * @param validationOn      the validation switcher.
     * @return the new MlemJClient.
     */
    public static MlemJClient createMlemJClient(ExecutorService executorService, String host, System.Logger logger, boolean validationOn) {
        return new MlemJClientImpl(executorService, host, logger, validationOn);
    }
}