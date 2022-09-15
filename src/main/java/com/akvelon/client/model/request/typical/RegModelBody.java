package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;

/**
 * A class that provides the RegModel request parameters.
 */
public final class RegModelBody extends RequestBody {
    /**
     * Create new RegModel request parameters.
     *
     * @param property a parameter property name.
     * @param value    a value for "0".
     */
    public RegModelBody(String property, double value) {
        Record record = new Record();
        record.addColumn(RegModelProperty.VALUE, value);

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        this.addParameter(property, recordSet);
    }
}
