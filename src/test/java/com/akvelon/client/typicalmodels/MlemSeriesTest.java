package com.akvelon.client.typicalmodels;

import com.akvelon.client.MlemJClient;
import com.akvelon.client.MlemJClientFactory;
import com.akvelon.client.model.request.typical.SeriesTypeBody;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.akvelon.client.util.AssertionsUtil.assertResponseJsonOrHandleException;

public class MlemSeriesTest {
    protected final static String HOST_URL = "http://localhost:8080/";
    protected MlemJClient jClient = MlemJClientFactory.createMlemJClient(HOST_URL, true);

    @Disabled("Disabled until Series MLEM model is up!")
    @Test
    @DisplayName("Test post /predict method with Series request body")
    public void testPredictSeriesType() throws IOException, ExecutionException, InterruptedException {
        SeriesTypeBody requestBody = new SeriesTypeBody(1L, 2L);
        assertResponseJsonOrHandleException(jClient.predict(requestBody));
    }
}