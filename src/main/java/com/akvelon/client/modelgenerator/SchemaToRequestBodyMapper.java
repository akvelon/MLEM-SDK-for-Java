package com.akvelon.client.modelgenerator;

import com.akvelon.client.model.common.DataType;
import com.akvelon.client.model.validation.RecordSetSchema;
import com.akvelon.client.model.validation.RequestBodySchema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.akvelon.client.modelgenerator.Constant.REQUEST_BODY_NAME;

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
        List<Context.RequestProperty> propertyList = new ArrayList<>();
        String importClass = "import com.akvelon.client.model.request.*;";

        for (Map.Entry<String, RecordSetSchema> entry : requestBodySchema.getParameterDescMap().entrySet()) {
            DataType valueClassDataType = DataType.fromString(entry.getValue().getType());
            String importString = valueClassDataType.getImportString();
            if (importString != null) {
                importClass = importString;
            }

            String valueClass = valueClassDataType.getClazz().getSimpleName();
            if (valueClassDataType.equals(DataType.List)) {
                DataType dataType = entry.getValue().getColumns().get(0).getType();
                valueClass += "\\<" + dataType.getClazz().getSimpleName() + "\\>";
            }

            propertyList.add(new Context.RequestProperty(entry.getKey(), valueClass));
        }

        return Collections.singletonList(recordSetSchemaToContext(name, propertyList, packageName, importClass));
    }

    private static Context recordSetSchemaToContext(String methodName, List<Context.RequestProperty> propertyList, String packageName, String importClass) {
        Context context = new Context();
        String className = Util.formatToJavaClass(methodName);
        context.setClassName(className + REQUEST_BODY_NAME);
        context.setPackages(packageName);
        context.setRequestProperties(propertyList);
        context.setImportClass(importClass);

        return context;
    }
}