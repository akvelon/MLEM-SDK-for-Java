package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;

/**
 * A class that provides the RegModel request.
 */
public final class RegModel extends Request {
    /**
     * Create new RegModel request
     *
     * @param propertyName a parameter property name
     * @param value        a value for "0"
     */
    public RegModel(String propertyName, double value) {
        Record record = new Record();
        record.addColumn(RegModelProperty.VALUE, value);

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        this.addParameter(propertyName, recordSet);
    }
}
