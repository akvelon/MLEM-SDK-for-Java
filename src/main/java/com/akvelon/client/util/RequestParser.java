package com.akvelon.client.util;

import com.akvelon.client.exception.IllegalArgsTypeException;
import com.akvelon.client.exception.IllegalRecordSetTypeException;
import com.akvelon.client.model.request.RecordSet;
import com.akvelon.client.model.request.Request;
import com.akvelon.client.model.validation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides functionality for deserialization JSON schema to descriptions objects.
 */
public class RequestParser {
    /**
     * System.Logger instances log messages that will be routed to the underlying.
     * logging framework the LoggerFinder uses.
     */
    private final System.Logger logger;

    public RequestParser(System.Logger logger) {
        this.logger = logger;
    }

    /**
     * Method to deserialize a InterfaceDesc object from given JSON schema.
     *
     * @param schema the JsonNode representation of InterfaceDesc.
     * @return the InterfaceDesc object of the conversion.
     * @throws IOException signals that an I/O exception has occurred
     *                     or an illegal recordSetType has occurred or args is not array.
     */
    public InterfaceDesc parseInterfaceSchema(JsonNode schema) throws IOException {
        Map<String, JsonNode> jsonNodeMap = JsonMapper.readMap(schema.get("methods"));
        ArrayList<RequestDesc> methods = parseMethod(jsonNodeMap);

        return new InterfaceDesc(methods);
    }

    /**
     * Method to deserialize a list of RequestDesc object from given JSON schema.
     *
     * @param jsonNodeMap a map with string-json representation of RequestDesc list.
     * @return the ArrayList<RequestDesc> objects of the conversion.
     * @throws IOException signals that an illegal recordSetType has occurred or args is not array.
     */
    private ArrayList<RequestDesc> parseMethod(Map<String, JsonNode> jsonNodeMap) throws IOException {
        ArrayList<RequestDesc> parameters = new ArrayList<>();
        // fill the parameters by created RequestDesc.
        for (Map.Entry<String, JsonNode> entry : jsonNodeMap.entrySet()) {
            // deserialize RequestDesc content from given JSON content.
            RequestDesc parameterDesc = parseParameter(entry);
            // add parameterDesc to list.
            parameters.add(parameterDesc);
        }

        return parameters;
    }

    /**
     * Method to deserialize RequestDesc object from given JSON schema.
     *
     * @param entry a collection-view of the map with arguments description.
     * @return the RequestDesc object of the conversion.
     * @throws IOException signals that an illegal recordSetType has occurred or args is not array.
     */
    private RequestDesc parseParameter(Map.Entry<String, JsonNode> entry) throws IOException {
        // access value of the "args" of an object node.
        JsonNode args = entry.getValue().get("args");
        // if args is not array, throw exception.
        if (!args.isArray()) {
            String exceptionMessage = "args is not array: " + args;
            logger.log(System.Logger.Level.ERROR, exceptionMessage);
            throw new IllegalArgsTypeException(exceptionMessage);
        }

        // access value of the "returns" of an object node.
        JsonNode returnsJsonNode = entry.getValue().get("returns");

        List<ParameterDesc> parameterDescList = new ArrayList<>();

        // fill the parameterDescList by created items.
        for (JsonNode arg : args) {
            // deserialize RecordSetDesc content from a given JSON.
            RecordSetDesc recordSetDesc = parseRecordSetDesc(arg.get("type_"));
            // create a new ParameterDesc with given name and RecordSetDesc.
            ParameterDesc parameterDesc = new ParameterDesc(arg.get("name").asText(), recordSetDesc);
            // add parameterDesc to list parameterDescList.
            parameterDescList.add(parameterDesc);
        }

        // create new RequestDesc and return.
        return new RequestDesc(
                entry.getKey(), //name
                parameterDescList, //parameterDescList
                DataType.fromString(returnsJsonNode.get("dtype").asText()) //return type
        );
    }

    /**
     * Method to deserialize RecordSetDesc object from given JSON schema.
     *
     * @param jsonNode the JsonNode representation of RecordSetDesc.
     * @return the RecordSetDesc object of the conversion.
     * @throws IOException signals that an illegal recordSetType has occurred.
     */
    private RecordSetDesc parseRecordSetDesc(JsonNode jsonNode) throws IOException {
        // get recordSet type description.
        String recordSetDescType = jsonNode.get("type").asText();
        // check recordSet type description.
        if (!recordSetDescType.equals("dataframe")) {
            String exceptionMessage = "RecordSet type is not dataframe: " + recordSetDescType;
            logger.log(System.Logger.Level.ERROR, exceptionMessage);
            throw new IllegalRecordSetTypeException(exceptionMessage);
        }

        // get columns.
        JsonNode columns = jsonNode.get("columns");
        // get dtypes.
        JsonNode dtypes = jsonNode.get("dtypes");

        ArrayList<RecordSetColumn> recordSetColumns = new ArrayList<>();
        // convert results from given columns into List<String>.
        List<String> names = JsonMapper.readValues(columns);
        // convert results from given dtypes into List<String>.
        List<String> dTypes = JsonMapper.readValues(dtypes);

        // fill the recordSetColumns by names and dTypes.
        for (int i = 0; i < names.size(); i++) {
            recordSetColumns.add(new RecordSetColumn(names.get(i), DataType.fromString(dTypes.get(i))));
        }

        // create new RecordSetDesc and return.
        return new RecordSetDesc(
                recordSetDescType, //type
                recordSetColumns    //columns
        );
    }

    /**
     * Method to deserialize Request object from given JSON schema.
     *
     * @param body the JsonNode representation of Request parameters.
     * @return the Request object of the conversion.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public Request parseRequest(JsonNode body) throws IOException {
        // Convert results from given JSON tree into Map<String, JsonNode>.
        Map<String, JsonNode> parameters = JsonMapper.readMap(body);
        // create the request and add a parameters.
        Request request = new Request();
        for (Map.Entry<String, JsonNode> entry : parameters.entrySet()) {
            // parse recordSet and add to parameter.
            request.addParameter(entry.getKey(), parseRecordSet(entry.getValue()));
        }

        return request;
    }

    /**
     * Method to deserialize RecordSet object from given JSON schema.
     *
     * @param recordSetJson the JsonNode representation of RecordSet.
     * @return the RecordSet object of the conversion.
     * @throws JsonProcessingException used to signal fatal problems with mapping of content.
     */
    private RecordSet parseRecordSet(JsonNode recordSetJson) throws JsonProcessingException {
        // deserialize RecordSet content from given JSON
        return JsonMapper.readValue(
                recordSetJson.toString(),   //data
                RecordSet.class             //class
        );
    }
}