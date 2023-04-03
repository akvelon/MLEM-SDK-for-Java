package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RecordType;
import com.akvelon.client.model.request.RequestBody;

/**
 * A class that provides the Wine request body as an example request.
 */
public class WineBody extends RequestBody {
    /**
     * Create new Wine body request body.
     *
     * @param property a parameter property name.
     */
    public WineBody(String property, WineColumn[] wineColumns) {
        RecordType recordType = new RecordType();
        for (WineColumn wineColumn : wineColumns) {
            recordType.addColumn(wineColumn.getProperty().property, wineColumn.getValue());
        }

        RecordSet recordSet = new RecordSet(property);
        recordSet.addRecord(recordType);

        setParameter(property, recordSet);
    }
}