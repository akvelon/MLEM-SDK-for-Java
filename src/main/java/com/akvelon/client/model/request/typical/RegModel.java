package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;

public final class RegModel extends Request {
    public RegModel(String propertyName, double value) {
        Record record = new Record();
        record.addColumn("0", value);

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        this.addParameter(propertyName, recordSet);
    }
}
