package com.akvelon.client.model.request;

import com.akvelon.client.model.response.ResponseBody;
import com.akvelon.client.model.response.Value;
import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that maps the requests parameters.
 */
public class RequestBody {
    /**
     * The map of record set as a value and name as a key.
     */
    private final Map<String, Value> parameters = new HashMap<>();

    /**
     * Associates the specified record set with the specified name.
     *
     * @param property the property name.
     * @param value    the RecordSet object.
     */
    public void addParameter(String property, Value value) {
        parameters.put(property, value);
    }

    /**
     * Method to deserialize JSON content from given JSON content String.
     *
     * @return the JsonNode object of the conversion.
     */
    public JsonNode toJson() throws JsonProcessingException {
        return JsonMapper.createObjectNodeWith2DArray(parameters);
    }

    public Map<String, Value> getParameters() {
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
