package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;

/**
 * A class that provides the Iris request.
 */
public final class Iris extends Request {
    /**
     * Create new Iris request
     *
     * @param propertyName a parameter property name
     * @param sepalLength  a value for sepal length (cm)
     * @param petalLength  a value for sepal width (cm)
     * @param sepalWidth   a value for petal length (cm)
     * @param petalWidth   a value for petal width (cm)
     */
    public Iris(String propertyName, double sepalLength, double petalLength, double sepalWidth, double petalWidth) {
        // create an empty records
        Record record = new Record();
        // add typical Iris columns
        record.addColumn(IrisProperty.SEPAL_LENGTH, sepalLength);
        record.addColumn(IrisProperty.PETAL_LENGTH, petalLength);
        record.addColumn(IrisProperty.SEPAL_WIDTH, sepalWidth);
        record.addColumn(IrisProperty.PETAL_WIDTH, petalWidth);

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        this.addParameter(propertyName, recordSet);
    }
}