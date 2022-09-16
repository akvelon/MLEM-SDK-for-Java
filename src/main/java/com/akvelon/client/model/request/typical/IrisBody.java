package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RequestBody;

/**
 * A class that provides the Iris request parameters.
 */
public final class IrisBody extends RequestBody {
    /**
     * Create new Iris request parameters.
     *
     * @param property    a parameter property name.
     * @param sepalLength a value for sepal length (cm).
     * @param petalLength a value for sepal width (cm).
     * @param sepalWidth  a value for petal length (cm).
     * @param petalWidth  a value for petal width (cm).
     */
    public IrisBody(String property, double sepalLength, double petalLength, double sepalWidth, double petalWidth) {
        Record record = new Record();
        record.addColumn(IrisProperty.SEPAL_LENGTH, sepalLength);
        record.addColumn(IrisProperty.PETAL_LENGTH, petalLength);
        record.addColumn(IrisProperty.SEPAL_WIDTH, sepalWidth);
        record.addColumn(IrisProperty.PETAL_WIDTH, petalWidth);

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        this.addParameter(property, recordSet);
    }
}