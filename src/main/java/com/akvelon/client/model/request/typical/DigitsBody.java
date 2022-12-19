package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;

/**
 * A class that provides the Digits request body as an example request.
 */
public class DigitsBody extends RequestBody {
    /**
     * Create new Digits request body.
     *
     * @param records 2-dim array of parameters
     */
    public DigitsBody(DigitsColumn[][] records) {
        Record record = new Record();
        for (DigitsColumn[] digitsColumns : records) {
            for (DigitsColumn digitsColumn : digitsColumns) {
                record.addColumn(digitsColumn.getProperty().property, digitsColumn.getValue());
            }
        }

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        this.addParameter("data", recordSet);
    }
}