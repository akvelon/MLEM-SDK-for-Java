package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;

public class DigitsBody extends RequestBody {
    public DigitsBody(DigitsColumn[] records) {
        Record record = new Record();
        for (DigitsColumn digitsColumn : records) {
            record.addColumn(digitsColumn.getProperty().property, digitsColumn.getValue());
        }

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        this.addParameter("data", recordSet);
    }
}
