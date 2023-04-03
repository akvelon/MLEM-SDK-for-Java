package com.akvelon.client.modelgenerator;

import com.akvelon.client.model.common.DataType;
import com.akvelon.client.model.validation.RequestBodySchema;
import com.akvelon.client.model.validation.ReturnType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.akvelon.client.modelgenerator.Constant.*;

/**
 * A mapper that provides functionality for transfer data from schema to response body.
 */
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
        List<Context.ResponseProperty> propertyList = new ArrayList<>();
        propertyList.add(toResponseProperty(returnType));
        context.setResponseProperties(propertyList);

        return context;
    }

    private static Context.ResponseProperty toResponseProperty(ReturnType returnType) {
        return new Context.ResponseProperty(
                RETURN_VALUE_NAME,
                toReturnValue(returnType),
                toResponseConstructorFirstArg(returnType),
                toDataType(returnType));
    }

    private static String toReturnValue(ReturnType returnType) {
        String type = returnType.getType();
        if (type.equals(DataType.Primitive.type)) {
            return DataType.fromString(returnType.getType()).getClazz().getSimpleName();
        }

        return DataType.fromString(returnType.getType()).getClazz().getSimpleName() +
                "\\<" + DataType.fromString(returnType.getDtype()).getClazz().getSimpleName() + "\\>";
    }

    private static String toResponseConstructorFirstArg(ReturnType returnType) {
        String type = returnType.getType();
        if (type.equals(DataType.Primitive.type) || type.equals(DataType.Torch.type)) {
            return RETURN_VALUE_NAME;
        }

        return NEW_ARRAY_VALUE;
    }

    private static String toDataType(ReturnType returnType) {
        return DATA_TYPE_WITH_DOT + DataType.fromString(returnType.getDtype());
    }
}