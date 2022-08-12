package com.akvelon.client;

import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RequestMapperTest {
    private final String type_ = "{\"columns\":[\"sepal length (cm)\",\"sepal width (cm)\",\"petal length (cm)\"," +
            "\"petal width (cm)\"],\"dtypes\":[\"float64\",\"float64\",\"float64\",\"float64\"],\"index_cols\":[]," +
            "\"type\":\"dataframe\"}";

    @Test
    public void testMapToRecordSetColumn() {
        JsonNode typeJson = JsonMapper.readValue(type_, JsonNode.class);
        JsonNode columnsJson = typeJson.get("columns");
        JsonNode dtypesJson = typeJson.get("dtypes");

        Assertions.assertNotNull(columnsJson);
        Assertions.assertNotNull(dtypesJson);

        Assertions.assertNotNull(typeJson);
        // TODO: 8/12/2022  
/*
        RequestMapper.mapToRecordSetColumn();
*/
    }
}
