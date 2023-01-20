package com.akvelon.client.model.request;

import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class that provides the list of records.
 */
public final class RecordSet extends Value {
    /**
     * The records list.
     */
    private final List<Record> records = new ArrayList<>();

    private final String property;

    public RecordSet(String property) {
        this.property = property;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void addRecord(Record record) {
        records.add(record);
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

    @Override
    public JsonNode toJson() throws JsonProcessingException {
        return JsonMapper.createObjectNodeWithRecords(property, records);
    }
}