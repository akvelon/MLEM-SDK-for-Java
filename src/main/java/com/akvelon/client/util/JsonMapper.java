package com.akvelon.client.util;

import com.akvelon.client.model.request.Record;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Provides functionality for reading and writing JSON, either to and from basic POJOs.
 */
public class JsonMapper {

    /**
     * Provides reading and writing JSON, either to and from basic POJOs.
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Method to deserialize JSON content from given JSON content String.
     *
     * @param json   the string representation of Json.
     * @param tClass the class for conversion result.
     * @param <T>    the generic for setting the class.
     * @return the result object of the conversion.
     * @throws JsonProcessingException used to signal fatal problems with mapping of content.
     */
    public static <T> T readValue(String json, Class<T> tClass) throws JsonProcessingException {
        // deserialize JSON content from given JSON content String.
        return mapper.readValue(json, tClass);
    }

    /**
     * Constructs ObjectReader that will read instances of List<T> type.
     * Convert results from given JSON tree into List<T> type.
     *
     * @param json the Json data.
     * @param <T>  the generic for setting the class.
     * @return the list of given type T.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static <T> List<T> readValues(JsonNode json) throws IOException {
        // construct ObjectReader for given type List<T>.
        ObjectReader reader = mapper.readerFor(new TypeReference<List<T>>() {
        });
        // convert results from given JSON tree into List<T>
        return reader.readValue(json);
    }

    /**
     * Constructs ObjectReader that will read instances of Map<String, JsonNode> type.
     * Convert results from given JSON tree into Map<String, JsonNode> type.
     *
     * @param json the Json data.
     * @return the list of given type Map.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static Map<String, JsonNode> readMap(JsonNode json) throws IOException {
        // construct ObjectReader for given type Map<String, JsonNode>.
        ObjectReader reader = mapper.readerFor(new TypeReference<Map<String, JsonNode>>() {
        });
        // convert results from given JSON tree into Map<String, JsonNode>.
        return reader.readValue(json);
    }

    /**
     * Method that can be used to serialize any Java value as a String.
     *
     * @param object for serialization.
     * @return the String representation of given object.
     * @throws JsonProcessingException caused when processing JSON finished with a problem.
     */
    public static String writeValueAsString(Object object) throws JsonProcessingException {
        // serialize a java object to string.
        return mapper.writeValueAsString(object);
    }

    /**
     * @param propertyName the name of property to set value.
     * @param records      the list of record values.
     * @return This node after adding property value.
     * @throws JsonProcessingException caused when processing JSON finished with a problem.
     */
    public static JsonNode createObjectNodeWithArray(String propertyName, List<Record> records) throws JsonProcessingException {
        // construct an empty JSON Array node.
        ArrayNode arrayNode = mapper.createArrayNode();

        // convert record to json and add to JSON Array node.
        for (Record record : records) {
            arrayNode.add(record.toJsonNode());
        }

        // set specified property with given JSON Array node.
        return mapper.createObjectNode().set(propertyName, arrayNode);
    }
}