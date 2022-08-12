package com.akvelon.client.model.validation;

import java.util.ArrayList;

public class RecordSetDesc {
    private ArrayList<RecordSetColumn> columns;

    public RecordSetDesc(ArrayList<RecordSetColumn> columns) {
        this.columns = columns;
    }

    public ArrayList<RecordSetColumn> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<RecordSetColumn> columns) {
        this.columns = columns;
    }
}
