package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;

/**
 * A class that provides the Wine request body as an example request.
 */
public class WineBody extends RequestBody {
    /**
     * Create new Wine body request parameters.
     *
     * @param property a parameter property name.
     */
    public WineBody(String property, WineColumn[] wineColumns) {
        Record record = new Record();
        for (WineColumn wineColumn : wineColumns) {
            record.addColumn(wineColumn.getProperty().property, wineColumn.getValue());
        }

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        this.addParameter(property, recordSet);
    }
}