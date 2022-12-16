package com.akvelon.client.modelgenerator;


import java.util.List;

/**
 * A class that provides a Java class description.
 */
public class Context {
    List<Property> properties;
    List<String> getters;
    List<String> setters;
    String className;
    String packages;
    String parameterProperty;

    public void setParameterProperty(String parameterProperty) {
        this.parameterProperty = parameterProperty;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public void setGetters(List<String> getters) {
        this.getters = getters;
    }

    public void setSetters(List<String> setters) {
        this.setters = setters;
    }

    public static class Property {
        String property;
        String getter;
        String camelcase;
        String type;
        String columnName;
        String divider;
        String argument;
        String superFirstArgument;
        String superSecondArgument;

        public Property(String columnName, String property, String type, String divider) {
            this.columnName = columnName;
            this.property = property.replaceAll("\\W|_", " ");
            String[] propertyes = this.property.split(" ");
            this.property = "";
            for (int i = 0; i < propertyes.length; i++) {
                if (i == 0) {
                    this.property += propertyes[0];
                    continue;
                }

                this.property += Util.capitalize(propertyes[i]);
            }

            this.argument = this.property + divider;
            this.getter = "get" + Util.capitalize(this.property);
            this.camelcase = Util.capitalize(this.property);
            this.type = type;
            this.divider = divider;
        }

        public Property(String property, String type, String divider) {
            this(null, property, type, divider);
        }

        public Property(String property, String type, String divider, String superFirstArgument, String superSecondArgument) {
            this(property, type, divider);
            this.superFirstArgument = superFirstArgument;
            this.superSecondArgument = superSecondArgument;
        }
    }
}
