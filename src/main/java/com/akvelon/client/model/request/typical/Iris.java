package com.akvelon.client.model.request.typical;

import com.akvelon.client.model.request.Record;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;

public final class Iris extends Request {
    public Iris(String propertyName, double sepalLength, double petalLength, double sepalWidth, double petalWidth) {
        Record record = new Record();
        record.addColumn("sepal length (cm)", sepalLength);
        record.addColumn("sepal width (cm)", petalLength);
        record.addColumn("petal length (cm)", sepalWidth);
        record.addColumn("petal width (cm)", petalWidth);

        RecordSet recordSet = new RecordSet();
        recordSet.addRecord(record);

        this.addParameter(propertyName, recordSet);
    }
}
