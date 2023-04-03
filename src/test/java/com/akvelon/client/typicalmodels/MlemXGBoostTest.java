package com.akvelon.client.typicalmodels;

import com.akvelon.client.MlemJClient;
import com.akvelon.client.MlemJClientFactory;
import com.akvelon.client.model.request.typical.XGBoostTypeBody;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.akvelon.client.util.AssertionsUtil.assertResponseJsonOrHandleException;

public class MlemXGBoostTest {
    protected final static String HOST_URL = "http://localhost:8080/";
    protected MlemJClient jClient = MlemJClientFactory.createMlemJClient(HOST_URL, true);

    @Test
    @Disabled("Disabled until XGBoost MLEM model is up!")
    @DisplayName("Test post /predict method with XGBoost request body")
    public void testPredictGbm() throws IOException, ExecutionException, InterruptedException {
        Map<String, Number> values = new HashMap<>();
        values.put("", 0);
        values.put("Pclass", 0);
        values.put("Sex", 0);
        values.put("Age", 0d);
        values.put("SibSp", 0);
        values.put("Parch", 0);
        values.put("Fare", 0d);
        values.put("Embarked", 0);

        XGBoostTypeBody lightGBMTypeBody = new XGBoostTypeBody(values);
        assertResponseJsonOrHandleException(jClient.predict(lightGBMTypeBody));
    }
}