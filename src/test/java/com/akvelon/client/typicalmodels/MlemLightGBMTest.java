package com.akvelon.client.typicalmodels;

import com.akvelon.client.MlemJClient;
import com.akvelon.client.MlemJClientFactory;
import com.akvelon.client.model.request.typical.LightGBMTypeBody;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.akvelon.client.util.AssertionsUtil.assertResponseJsonOrHandleException;

public class MlemLightGBMTest {
    protected final static String HOST_URL = "http://localhost:8080/";
    protected MlemJClient jClient = MlemJClientFactory.createMlemJClient(HOST_URL, true);

    @Test
    @Disabled("Disabled until LightGbm MLEM model is up!")
    @DisplayName("Test post /predict method with GBM request body")
    public void testPredictGbm() throws IOException, ExecutionException, InterruptedException {
        int arr1Size = 28;
        Map<String, Integer> values = new HashMap<>();
        for (int i = 0; i < arr1Size; i++) {
            String property = String.valueOf(i + 1);
            values.put(property, 0);
        }

        LightGBMTypeBody lightGBMTypeBody = new LightGBMTypeBody(values);
        assertResponseJsonOrHandleException(jClient.predict(lightGBMTypeBody));
    }
}