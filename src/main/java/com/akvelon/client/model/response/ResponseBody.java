package com.akvelon.client.model.response;

import com.akvelon.client.model.request.Value;
import com.akvelon.client.model.validation.DataType;
import com.akvelon.client.util.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * A class that provides the responses value and type.
 */
public class ResponseBody {
    private Value value;
    private DataType type;

    public ResponseBody() {
    }

    public ResponseBody(Value value, DataType type) {
        this.value = value;
        this.type = type;
    }

    /**
     * Convert from given jsonNode into value and type.
     *
     * @param jsonNode the json data
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void parseJson(JsonNode jsonNode) throws IOException {
        this.value = new ArrayValue<>(JsonMapper.readList(jsonNode));
        this.type = DataType.Ndarray;
    }

    public Value getValue() {
        return value;
    }

    public DataType getType() {
        return type;
    }
}