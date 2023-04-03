package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RecordType;
import com.akvelon.client.model.request.RequestBody;

import java.util.Map;

/**
 * A class that provides the request body with light gbm type as an example request.
 */
public final class LightGBMTypeBody extends RequestBody {
    /**
     * Create new SeriesType request body.
     *
     * @param values a data map, where key - property, value - value;
     */
    public LightGBMTypeBody(Map<String, Integer> values) {
        RecordType recordType = new RecordType();

        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            recordType.addColumn(entry.getKey(), entry.getValue());
        }

        RecordSet recordSet = new RecordSet("values");
        recordSet.addRecord(recordType);

        this.setParameter("data", recordSet);
    }
}