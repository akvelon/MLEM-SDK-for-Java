package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;

/**
 * A class that provides the RegModel request parameters  as an example request.
 */
public final class RegModelBody extends RequestBody {
    /**
     * Create new RegModel request body.
     *
     * @param property a parameter property name.
     * @param value    a value for "0".
     */
    public RegModelBody(String property, double value) {
        Record record = new Record();
        record.addColumn(RegModelProperty.VALUE, value);

        RecordSet recordSet = new RecordSet(property);
        recordSet.addRecord(record);

        addParameter(property, recordSet);
    }
}
