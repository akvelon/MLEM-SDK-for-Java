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

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<RecordSetColumn> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<RecordSetColumn> columns) {
        this.columns = columns;
    }
}