package com.akvelon.client;

public class MlemJClientWithLoggerTest extends MlemJClientTest {
    public MlemJClientWithLoggerTest() {
        jClient = MlemJClientFactory.createMlemJClient(HOST_URL, LOGGER, true);
    }
}
