package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RecordType;
import com.akvelon.client.model.request.RequestBody;

/**
 * A class that provides the request body with Series type as an example request.
 */
public final class SeriesTypeBody extends RequestBody {
    /**
     * Create new SeriesType request body.
     *
     * @param pClass a value for PClass.
     * @param parch  a value for Parch.
     */
    public SeriesTypeBody(long pClass, long parch) {
        RecordType recordType = new RecordType();
        recordType.addColumn("Pclass", pClass);
        recordType.addColumn("Parch", parch);

        RecordSet recordSet = new RecordSet("values");
        recordSet.addRecord(recordType);

        this.setParameter("data", recordSet);
    }
}