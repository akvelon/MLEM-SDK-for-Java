package com.akvelon.client.model.validation;

import java.util.ArrayList;

public class RecordSetDesc {
    private String type;
    private ArrayList<RecordSetColumn> columns;

    public RecordSetDesc(String type, ArrayList<RecordSetColumn> columns) {
        this.type = type;
        this.columns = columns;
    }

    public String getType() {
        return type;
    }

    public ArrayList<RecordSetColumn> getColumns() {
        return columns;
    }
}