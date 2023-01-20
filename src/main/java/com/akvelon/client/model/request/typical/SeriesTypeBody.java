package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;

/**
 * A class that provides the request body with Series type as an example request.
 */
public final class SeriesTypeBody extends RequestBody {
    /**
     * Create new SeriesType request body.
     *
     * @param property a parameter property name.
     * @param pClass   a value for PClass.
     * @param parch    a value for Parch.
     */
    public SeriesTypeBody(String property, long pClass, long parch) {
        Record record = new Record();
        record.addColumn("Pclass", pClass);
        record.addColumn("Parch", parch);

        RecordSet recordSet = new RecordSet(property);
        recordSet.addRecord(record);

        this.addParameter(property, recordSet);
    }
}