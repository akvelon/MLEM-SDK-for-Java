package com.akvelon.client.model.interface_;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Type {
    private ArrayList<String> columns;
    private ArrayList<String> dtypes;
    @JsonProperty("index_cols")
    private ArrayList<String> indexCols;
    private String type;

    public Type(ArrayList<String> columns, ArrayList<String> dtypes, ArrayList<String> indexCols, String type) {
        this.columns = columns;
        this.dtypes = dtypes;
        this.indexCols = indexCols;
        this.type = type;
    }

    public Type() {
    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    public ArrayList<String> getDtypes() {
        return dtypes;
    }

    public void setDtypes(ArrayList<String> dtypes) {
        this.dtypes = dtypes;
    }

    public ArrayList<String> getIndexCols() {
        return indexCols;
    }

    public void setIndexCols(ArrayList<String> indexCols) {
        this.indexCols = indexCols;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
