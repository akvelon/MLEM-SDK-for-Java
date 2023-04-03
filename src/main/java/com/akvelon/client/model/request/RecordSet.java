package com.akvelon.client.model.request;

import com.akvelon.client.model.common.Value;
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
    private final List<RecordType> recordTypes = new ArrayList<>();

    private final String property;

    public RecordSet(String property) {
        this.property = property;
    }

    public List<RecordType> getRecords() {
        return recordTypes;
    }

    public void addRecord(RecordType recordType) {
        recordTypes.add(recordType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordSet recordSet = (RecordSet) o;
        return Objects.equals(recordTypes, recordSet.recordTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordTypes);
    }

    @Override
    public JsonNode toJson() throws JsonProcessingException {
        return JsonMapper.createObjectNodeWithRecords(property, recordTypes);
    }
}