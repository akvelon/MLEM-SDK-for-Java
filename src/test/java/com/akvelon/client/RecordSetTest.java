package com.akvelon.client;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RecordSetTest {
    private final RecordSet recordSet = new RecordSet();

    @Test
    public void testToJson() {
        Record record = new Record();
        record.addColumn("name1", 1);
        record.addColumn("name2", 2);
        JsonNode jsonNode = record.toJsonNode();
        Assertions.assertNotNull(jsonNode);

        recordSet.addRecord(record);
        JsonNode jsonArray = recordSet.toJson("data");
        Assertions.assertNotNull(jsonArray);
    }
}
