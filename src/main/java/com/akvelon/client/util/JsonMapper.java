package com.akvelon.client.util;

import com.akvelon.client.model.request.Record;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;

public class JsonMapper {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * The method convert the string representation to given class
     *
     * @param json   is the string representation of Json
     * @param tClass is the class for conversion result
     * @param <T>    is the generic for setting the class
     * @return the result object of the conversion
     */
    public static <T> T stringToObject(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String writeValueAsString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode recordsToJsonNode(String propertyName, List<Record> records) {
        ArrayNode arrayNode = mapper.createArrayNode();
        for (Record record : records) {
            arrayNode.add(record.toJsonNode());
        }

        return mapper.createObjectNode().set(propertyName, arrayNode);
    }
}