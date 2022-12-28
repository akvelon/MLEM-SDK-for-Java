package com.akvelon.client;

import com.akvelon.client.model.request.RequestBody;
import com.akvelon.client.util.TestDataBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.akvelon.client.util.AssertionsUtil.assertResponseJsonOrHandleException;

public class MlemJClientWithHostTest extends MlemJClientTest {
    public MlemJClientWithHostTest() {
        jClient = MlemJClientFactory.createMlemJClient(HOST_URL, true);
    }

    @Disabled("Disabled until Digits MLEM model is up!")
    @Test
    @DisplayName("Test post /predict method with Digits request body")
    public void testPredictDigits() throws IOException, ExecutionException, InterruptedException {
        int arr1Size = 1;
        int arr2Size = 64;
        Double[][] array = new Double[arr1Size][arr2Size];
        for (int i = 0; i < arr1Size; i++) {
            for (int j = 0; j < arr2Size; j++) {
                array[i][j] = 0.1d;
            }
        }
        RequestBody<Double[][]> requestBody = TestDataBuilder.buildRequest("data", array);
        assertResponseJsonOrHandleException(jClient.predict(requestBody));
    }
}
