package com.akvelon.client.model.validation;

import java.util.ArrayList;

public class RecordSetColumnsDesc {
    private String name;
    private ArrayList<RecordSetColumn> columns;

    public RecordSetColumnsDesc(String name, ArrayList<RecordSetColumn> columns) {
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<RecordSetColumn> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<RecordSetColumn> columns) {
        this.columns = columns;
    }
}