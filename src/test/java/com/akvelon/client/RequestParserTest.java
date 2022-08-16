package com.akvelon.client;

import com.akvelon.client.model.validation.RecordSetColumn;
import com.akvelon.client.model.validation.RecordSetColumnsDesc;
import com.akvelon.client.util.RequestParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class RequestParserTest {
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
        ArrayList<RecordSetColumn> recordSetColumns = RequestParser.mapToRecordSetColumn(columnsJson, dtypesJson);

        Assertions.assertNotNull(recordSetColumns);
        Assertions.assertEquals(4, recordSetColumns.size());
    }

    @Test
    public void testMapToRecordSetDesc() throws IOException {
        RecordSetColumnsDesc recordSetColumnsDesc = RequestParser.mapToRecordSetDesc("data", columnsJson, dtypesJson);

        Assertions.assertNotNull(recordSetColumnsDesc);
        Assertions.assertEquals(4, recordSetColumnsDesc.getColumns().size());
    }
}
