package com.akvelon.client;

import com.akvelon.client.model.validation.RecordSetDesc;
import com.akvelon.client.util.RequestParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class RequestParserTest {
    private static JsonNode typeJson;

    @BeforeAll
    public static void initJson() {
        typeJson = TestDataFactory.buildType_();

        Assertions.assertNotNull(typeJson);

        Assertions.assertNotNull(typeJson);
    }

    @Test
    public void testMapToRecordSetColumn() throws IOException {
        RecordSetDesc recordSetDesc = RequestParser.parseRecordSetDesc("", typeJson);

        Assertions.assertNotNull(recordSetDesc);
        Assertions.assertEquals(4, recordSetDesc.getColumns().size());
    }

    @Test
    public void testMapToRecordSetDesc() throws IOException {
        RecordSetDesc recordSetDesc = RequestParser.parseRecordSetDesc("data", typeJson);

        Assertions.assertNotNull(recordSetDesc);
        Assertions.assertEquals(4, recordSetDesc.getColumns().size());
    }
}
