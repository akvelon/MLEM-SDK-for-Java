package com.akvelon.client.model.request;

import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that maps the requests parameters.
 */
public class Request {
    /**
     * The map of record set as a value and name as a key.
     */
    private final Map<String, RecordSet> parameters = new HashMap<>();

    /**
     * Associates the specified record set with the specified name.
     *
     * @param propertyName the name.
     * @param recordSet    the RecordSet object.
     */
    public void addParameter(String propertyName, RecordSet recordSet) {
        parameters.put(propertyName, recordSet);
    }

    /**
     * Method to deserialize JSON content from given JSON content String.
     *
     * @return the JsonNode object of the conversion.
     * @throws JsonProcessingException used to signal fatal problems with mapping of content.
     */
    public JsonNode toJson() throws JsonProcessingException {
        String jsonString = JsonMapper.writeValueAsString(parameters);
        return JsonMapper.readValue(jsonString, JsonNode.class);
    }

    public Map<String, RecordSet> getParameters() {
        return parameters;
    }
}
