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
 * A mapper that provides functionality for reading and writing JSON, either to and from basic POJOs.
 */
public final class JsonMapper {

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
        return mapper.readValue(json, tClass);
    }

    /**
     * Constructs ObjectReader that will read instances of List type.
     * Convert results from given JSON tree into List type.
     *
     * @param json the Json data.
     * @param <T>  the generic for setting the class.
     * @return the list of given type T.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static <T> List<T> readList(JsonNode json) throws IOException {
        ObjectReader reader = mapper.readerFor(new TypeReference<List<T>>() {});
        return reader.readValue(json);
    }

    /**
     * Constructs ObjectReader that will read instances of Map type.
     * Convert results from given JSON tree into Map type.
     *
     * @param json the Json data.
     * @return the list of given type Map.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static Map<String, JsonNode> readMap(JsonNode json) throws IOException {
        ObjectReader reader = mapper.readerFor(new TypeReference<Map<String, JsonNode>>() {});
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
        return mapper.writeValueAsString(object);
    }

    /**
     * @param property the name of property to set value.
     * @param records      the list of record values.
     * @return This node after adding property value.
     * @throws JsonProcessingException caused when processing JSON finished with a problem.
     */
    public static JsonNode createObjectNodeWithArray(String property, List<Record> records) throws JsonProcessingException {
        ArrayNode arrayNode = mapper.createArrayNode();

        for (Record record : records) {
            arrayNode.add(record.toJsonNode());
        }

        return mapper.createObjectNode().set(property, arrayNode);
    }
}