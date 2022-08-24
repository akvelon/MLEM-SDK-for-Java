package com.akvelon.client.model.request;

import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class represents numbers with their names.
 */
public class Record {
    /**
     * Map of number values with string name key.
     */
    @JsonValue
    private Map<String, Number> columns = new HashMap<>();

    /**
     * Empty constructor. Do not remove it.
     */
    public Record() {
    }

    /**
     * Constructs a new Record object. Used for deserialization.
     * Do not remove.
     *
     * @param columns Map of number values with string name key.
     */
    @JsonCreator
    public Record(Map<String, Number> columns) {
        this.columns = columns;
    }

    public Map<String, Number> getColumns() {
        return columns;
    }

    public void addColumn(String name, Number value) {
        columns.put(name, value);
    }

    public String toJsonString() throws JsonProcessingException {
        return JsonMapper.writeValueAsString(columns);
    }

    public JsonNode toJsonNode() throws JsonProcessingException {
        return JsonMapper.readValue(toJsonString(), JsonNode.class);
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