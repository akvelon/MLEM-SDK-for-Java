package com.akvelon.client;

import com.akvelon.client.model.validation.ParameterDesc;
import com.akvelon.client.model.validation.RecordSetColumn;
import com.akvelon.client.model.validation.RecordSetDesc;
import com.akvelon.client.util.RequestMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class RequestMapperTest {
    private static JsonNode columnsJson;
    private static JsonNode dtypesJson;

    @BeforeAll
    public static void initJson() {
        JsonNode typeJson = TestDataFactory.buildType_();
        columnsJson = typeJson.get("columns");
        dtypesJson = typeJson.get("dtypes");

        Assertions.assertNotNull(columnsJson);
        Assertions.assertNotNull(dtypesJson);

        Assertions.assertNotNull(typeJson);
    }

    @Test
    public void testMapToRecordSetColumn() throws IOException {
        ArrayList<RecordSetColumn> recordSetColumns = RequestMapper.mapToRecordSetColumn(columnsJson, dtypesJson);

        Assertions.assertNotNull(recordSetColumns);
        Assertions.assertEquals(4, recordSetColumns.size());
    }

    @Test
    public void testMapToRecordSetDesc() throws IOException {
        RecordSetDesc recordSetDesc = RequestMapper.mapToRecordSetDesc(columnsJson, dtypesJson);

        Assertions.assertNotNull(recordSetDesc);
        Assertions.assertEquals(4, recordSetDesc.getColumns().size());
    }

    @Test
    public void testMapToParameterDesc() throws IOException {
        RecordSetDesc recordSetDesc = RequestMapper.mapToRecordSetDesc(columnsJson, dtypesJson);

        ParameterDesc parameterDesc = RequestMapper.mapToParameterDesc("data", recordSetDesc);
        Assertions.assertNotNull(parameterDesc);
        Assertions.assertEquals(4, parameterDesc.getType().getColumns().size());
    }
}
