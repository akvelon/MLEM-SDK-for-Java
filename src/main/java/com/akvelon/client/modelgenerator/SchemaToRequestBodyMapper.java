package com.akvelon.client.modelgenerator;

import com.akvelon.client.model.validation.RecordSetColumnSchema;
import com.akvelon.client.model.validation.RecordSetSchema;
import com.akvelon.client.model.validation.RequestBodySchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.akvelon.client.modelgenerator.Constant.REQUEST_BODY_NAME;
import static com.akvelon.client.modelgenerator.Constant.VALUE_NAME;

final class SchemaToRequestBodyMapper {

    public static List<Context> requestBodySchemasToContextList(Map<String, RequestBodySchema> requestBodySchemas, String packageName) {
        List<Context> contextList = new ArrayList<>();
        for (Map.Entry<String, RequestBodySchema> schemaEntry : requestBodySchemas.entrySet()) {
            contextList.addAll(requestBodySchemaToContext(schemaEntry.getKey(), schemaEntry.getValue(), packageName));
        }

        return contextList;
    }

    private static List<Context> requestBodySchemaToContext(String name, RequestBodySchema requestBodySchema, String packageName) {
        List<Context> contextList = new ArrayList<>();
        List<String> propertiesList = new ArrayList<>();

        for (Map.Entry<String, RecordSetSchema> entry : requestBodySchema.getParameterDescMap().entrySet()) {
            propertiesList.add(entry.getKey());
        }

        contextList.addAll(recordSetSchemaToContext(name, propertiesList, packageName));

        return contextList;
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

    private static List<Context.Property> columnsToProperties(List<RecordSetColumnSchema> recordSetColumnSchemaList) {
        List<Context.Property> propertyList = new ArrayList<>();
        if (recordSetColumnSchemaList.size() == 1) {
            Context.Property property = recordSetColumnSchemaToProperty(recordSetColumnSchemaList.get(0));
            propertyList.add(property);
            return propertyList;
        }

        for (RecordSetColumnSchema recordSetColumnSchema : recordSetColumnSchemaList) {
            Context.Property property = recordSetColumnSchemaToProperty(recordSetColumnSchema);
            propertyList.add(property);
        }

        return propertyList;
    }

    private static Context.Property recordSetColumnSchemaToProperty(RecordSetColumnSchema recordSetColumnSchema) {
        return new Context.Property(recordSetColumnSchema.getName());
    }

    private static String getCorrectProperty(String propertyName) {
        if (propertyName == null || propertyName.isEmpty()) {
            return "propertyName";
        }

        char firstChar = propertyName.charAt(0);
        if (!Character.isDigit(firstChar)) {
            return propertyName;
        }

        if (propertyName.length() == 1) {
            return getTextForDigit(firstChar) + VALUE_NAME;
        }

        return getTextForDigit(firstChar) + propertyName.substring(1);
    }

    private static String getTextForDigit(char digit) {
        switch (digit) {
            case '0':
                return "zero";
            case '1':
                return "one";
            case '2':
                return "two";
            case '3':
                return "three";
            case '4':
                return "four";
            case '5':
                return "five";
            case '6':
                return "six";
            case '7':
                return "seven";
            case '8':
                return "eight";
            case '9':
                return "nine";

            default:
                return "value";
        }
    }
}
