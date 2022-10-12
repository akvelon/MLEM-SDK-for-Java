package com.akvelon.client;

public class MlemJClientWithHostTest extends MlemJClientTest {
    public MlemJClientWithHostTest() {
        jClient = MlemJClientFactory.createMlemJClient(HOST_URL, true);
    }
}
