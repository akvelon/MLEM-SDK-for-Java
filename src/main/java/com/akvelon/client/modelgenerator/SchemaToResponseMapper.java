package com.akvelon.client.modelgenerator;

import com.akvelon.client.model.validation.DataType;
import com.akvelon.client.model.validation.RequestBodySchema;
import com.akvelon.client.model.validation.ReturnType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.akvelon.client.modelgenerator.Constant.*;

final class SchemaToResponseMapper {
    public static List<Context> requestBodySchemasToContextList(Map<String, RequestBodySchema> requestBodySchemas, String packageName) {
        List<Context> contextList = new ArrayList<>();
        for (Map.Entry<String, RequestBodySchema> schemaEntry : requestBodySchemas.entrySet()) {
            contextList.add(requestBodySchemaToContext(schemaEntry.getKey(), schemaEntry.getValue(), packageName));
        }

        return contextList;
    }

    private static Context requestBodySchemaToContext(String name, RequestBodySchema requestBodySchema, String packageName) {
        ReturnType returnType = requestBodySchema.getReturnsSchema();

        Context context = new Context();
        String className = Util.formatToJavaClass(name) + RESPONSE_BODY_NAME;
        context.setClassName(className);
        context.setPackages(packageName);
        List<Context.Property> propertyList = new ArrayList<>();
        propertyList.add(
                // TODO: 1/10/2023
                new Context.Property(
                        toReturnValue(returnType),
                        NEW_ARRAY_VALUE,
                        toDataType(returnType)));
        context.setProperties(propertyList);

        return context;
    }

    private static String toReturnValue(ReturnType returnType) {
        return DataType.fromString(returnType.getType()).getClazz().getSimpleName() +
                "\\<" + DataType.fromString(returnType.getDtype()).getClazz().getSimpleName() + "\\>";
    }

    private static String toDataType(ReturnType returnType) {
        return DATA_TYPE_WITH_DOT + DataType.fromString(returnType.getType());
    }
}
