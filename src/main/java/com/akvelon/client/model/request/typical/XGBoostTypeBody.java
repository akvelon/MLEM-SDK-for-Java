package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RecordType;
import com.akvelon.client.model.request.RequestBody;

import java.util.Map;

/**
 * A class that provides the request body with xg boost type as an example request.
 */
public final class XGBoostTypeBody extends RequestBody {
    /**
     * Create new SeriesType request body.
     *
     * @param values a data map, where key - property, value - value;
     */
    public XGBoostTypeBody(Map<String, Number> values) {
        RecordType recordType = new RecordType();

        for (Map.Entry<String, Number> entry : values.entrySet()) {
            recordType.addColumn(entry.getKey(), entry.getValue());
        }

        RecordSet recordSet = new RecordSet("values");
        recordSet.addRecord(recordType);

        this.setParameter("data", recordSet);
    }
}