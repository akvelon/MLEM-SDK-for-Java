package com.akvelon.client.modelgenerator;

import com.akvelon.client.model.validation.RecordSetColumnSchema;
import com.akvelon.client.model.validation.RecordSetSchema;
import com.akvelon.client.model.validation.RequestBodySchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.akvelon.client.modelgenerator.Constant.REQUEST_BODY_NAME;
import static com.akvelon.client.modelgenerator.Constant.VALUE_NAME;

/**
 * A mapper that provides functionality for transfer data from schema to request body.
 */
final class SchemaToRequestBodyMapper {

    public static List<Context> requestBodySchemasToContextList(Map<String, RequestBodySchema> requestBodySchemas, String packageName) {
        List<Context> contextList = new ArrayList<>();
        for (Map.Entry<String, RequestBodySchema> schemaEntry : requestBodySchemas.entrySet()) {
            contextList.addAll(requestBodySchemaToContext(schemaEntry.getKey(), schemaEntry.getValue(), packageName));
        }

        return contextList;
    }

    private static List<Context> requestBodySchemaToContext(String name, RequestBodySchema requestBodySchema, String packageName) {
        List<String> propertiesList = new ArrayList<>();

        for (Map.Entry<String, RecordSetSchema> entry : requestBodySchema.getParameterDescMap().entrySet()) {
            propertiesList.add(entry.getKey());
        }

        return new ArrayList<>(recordSetSchemaToContext(name, propertiesList, packageName));
    }

    private static List<Context> recordSetSchemaToContext(String methodName, List<String> properties, String packageName) {
        List<Context> contextList = new ArrayList<>();

        Context context = new Context();
        String className = Util.formatToJavaClass(methodName);
        context.setClassName(className + REQUEST_BODY_NAME);
        context.setPackages(packageName);
        List<Context.Property> propertyList = toMustacheFormat(properties);
        context.setProperties(propertyList);
        contextList.add(context);

        return contextList;
    }

    private static List<Context.Property> toMustacheFormat(List<String> propertyName) {
        List<Context.Property> propertyList = new ArrayList<>();
        for (String property : propertyName) {
            propertyList.add(new Context.Property(property));
        }

        return propertyList;
    }
}
