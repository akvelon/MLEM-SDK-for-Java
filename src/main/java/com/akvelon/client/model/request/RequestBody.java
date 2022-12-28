package com.akvelon.client.model.request;

import com.akvelon.client.model.response.ResponseBody;
import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that maps the requests parameters.
 */
public class RequestBody <T> {
    /**
     * The map of record set as a value and name as a key.
     */
    private final Map<String, T> parameters = new HashMap<>();

    /**
     * Associates the specified record set with the specified name.
     *
     * @param property  the property name.
     * @param recordSet the RecordSet object.
     */
    public void addParameter(String property, T recordSet) {
        parameters.put(property, recordSet);
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

    public Map<String, T> getParameters() {
        return parameters;
    }

    public ResponseBody createResponse() {
        return new ResponseBody();
    }

    @Override
    public String toString() {
        try {
            return JsonMapper.writeValueAsString(getParameters());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
