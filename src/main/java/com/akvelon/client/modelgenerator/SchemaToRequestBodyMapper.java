package com.akvelon.client.modelgenerator;

import com.akvelon.client.model.validation.DataType;
import com.akvelon.client.model.validation.RecordSetColumnSchema;
import com.akvelon.client.model.validation.RecordSetSchema;
import com.akvelon.client.model.validation.RequestBodySchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.akvelon.client.modelgenerator.Constant.*;

final class SchemaToRequestBodyMapper {

    public static List<Context> requestBodySchemasToContextList(Map<String, RequestBodySchema> requestBodySchemas) {
        List<Context> contextList = new ArrayList<>();
        for (Map.Entry<String, RequestBodySchema> schemaEntry : requestBodySchemas.entrySet()) {
            contextList.addAll(requestBodySchemaToContext(schemaEntry.getKey(), schemaEntry.getValue()));
        }

        return contextList;
    }

    private static List<Context> requestBodySchemaToContext(String name, RequestBodySchema requestBodySchema) {
        List<Context> contextList = new ArrayList<>();

        for (Map.Entry<String, RecordSetSchema> entry : requestBodySchema.getParameterDescMap().entrySet()) {
            contextList.addAll(recordSetSchemaToContext(name, entry.getKey(), entry.getValue()));
        }

        return contextList;
    }

    private static List<Context> recordSetSchemaToContext(String methodName, String name, RecordSetSchema recordSetSchema) {
        List<Context> contextList = new ArrayList<>();

        Context context = new Context();
        String className = Util.formatToJavaClass(methodName + Util.capitalize(name));
        context.setClassName(className + REQUEST_BODY_NAME);
        context.setPackages(PACKAGE_NAME);
        context.setParameterProperty(name);
        List<Context.Property> propertyList = columnsToProperties(recordSetSchema.getColumns());
        context.setProperties(propertyList);
        contextList.add(context);

        return contextList;
    }

    private static List<Context.Property> columnsToProperties(List<RecordSetColumnSchema> recordSetColumnSchemaList) {
        List<Context.Property> propertyList = new ArrayList<>();
        if (recordSetColumnSchemaList.size() == 1) {
            Context.Property property = recordSetColumnSchemaToProperty(recordSetColumnSchemaList.get(0), NO_DIVIDER);
            propertyList.add(property);
            return propertyList;
        }

        int size = recordSetColumnSchemaList.size();
        for (int i = 0; i < size; i++) {
            String divider = ARGS_DIVIDER;
            if (i == size - 1) {
                divider = NO_DIVIDER;
            }

            Context.Property property = recordSetColumnSchemaToProperty(recordSetColumnSchemaList.get(i), divider);
            propertyList.add(property);
        }

        return propertyList;
    }

    private static Context.Property recordSetColumnSchemaToProperty(RecordSetColumnSchema recordSetColumnSchema, String divider) {
        return new Context.Property(recordSetColumnSchema.getName(), getCorrectProperty(recordSetColumnSchema.getName()),
                DataType.fromString(recordSetColumnSchema.getType().type).getClazz().getSimpleName(), divider);
    }

    private static String getCorrectProperty(String propertyName) {
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
