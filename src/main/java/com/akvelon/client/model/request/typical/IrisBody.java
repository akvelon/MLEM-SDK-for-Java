package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.RecordType;
import com.akvelon.client.model.request.RequestBody;

/**
 * A class that provides the Iris request body as an example request.
 */
public final class IrisBody extends RequestBody {
    /**
     * Create new Iris request body.
     *
     * @param property    a parameter property name.
     * @param sepalLength a value for sepal length (cm).
     * @param petalLength a value for sepal width (cm).
     * @param sepalWidth  a value for petal length (cm).
     * @param petalWidth  a value for petal width (cm).
     */
    public IrisBody(String property, double sepalLength, double petalLength, double sepalWidth, double petalWidth) {
        RecordType recordType = new RecordType();
        recordType.addColumn(IrisProperty.SEPAL_LENGTH, sepalLength);
        recordType.addColumn(IrisProperty.PETAL_LENGTH, petalLength);
        recordType.addColumn(IrisProperty.SEPAL_WIDTH, sepalWidth);
        recordType.addColumn(IrisProperty.PETAL_WIDTH, petalWidth);

        RecordSet recordSet = new RecordSet(property);
        recordSet.addRecord(recordType);

        this.setParameter(property, recordSet);
    }
}