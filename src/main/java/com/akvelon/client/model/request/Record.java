package com.akvelon.client.model.request;

import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Objects;

public class Record {
    @JsonValue
    private HashMap<String, Number> columns = new HashMap<>();

    public Record() {
    }

    @JsonCreator
    public Record(HashMap<String, Number> columns) {
        this.columns = columns;
    }

    public HashMap<String, Number> getColumns() {
        return columns;
    }

    public void addColumn(String name, Number value) {
        columns.put(name, value);
    }

    public String toString() {
        return JsonMapper.writeValueAsString(columns);
    }

    public JsonNode toJsonNode() {
        return JsonMapper.stringToObject(toString(), JsonNode.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return columns.equals(record.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns);
    }
}