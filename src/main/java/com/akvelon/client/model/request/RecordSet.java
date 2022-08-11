package com.akvelon.client.model.request;

import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecordSet {
    @JsonProperty("values")
    private final List<Record> records = new ArrayList<>();

    public RecordSet() {
    }

    public List<Record> getRecords() {
        return records;
    }

    public void addRecord(Record record) {
        records.add(record);
    }

    public JsonNode toJson(String propertyName) {
        return JsonMapper.createObjectNodeWithArray(propertyName, records);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordSet recordSet = (RecordSet) o;
        return Objects.equals(records, recordSet.records);
    }

    @Override
    public int hashCode() {
        return Objects.hash(records);
    }
}