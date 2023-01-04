package com.akvelon.client.modelgenerator;


import java.util.List;

/**
 * A class that provides a Java class description.
 */
public class Context {
    /**
     * Attributes or features that characterize classes.
     */
    List<Property> properties;
    /**
     * Representing the name of a Java class.
     */
    String className;
    /**
     * Used to group related classes.
     */
    String packages;
    /**
     * RecordSet property.
     */
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

    /**
     * Attribute or feature that characterize classes.
     */
    public static class Property {
        /**
         * Record column value.
         */
        String property;
        /**
         * Constructors argument type.
         */
        String type;
        /**
         * Record column name.
         */
        String columnName;
        /**
         * Divider ',' or empty.
         */
        String divider;
        /**
         * Constructors argument.
         */
        String argument;
        /**
         * First argument for call super() in the Response body constructor.
         * See response.mustache file.
         */
        String firstResponseConstructorArg;
        /**
         * Second argument for call super() in the Response body constructor.
         * See response.mustache file.
         */
        String secondResponseConstructorArg;

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
            this.type = type;
            this.divider = divider;
        }

        public Property(String property, String type, String divider) {
            this(null, property, type, divider);
        }

        public Property(String property, String type, String divider, String firstResponseConstructorArg, String secondResponseConstructorArg) {
            this(property, type, divider);
            this.firstResponseConstructorArg = firstResponseConstructorArg;
            this.secondResponseConstructorArg = secondResponseConstructorArg;
        }
    }
}
