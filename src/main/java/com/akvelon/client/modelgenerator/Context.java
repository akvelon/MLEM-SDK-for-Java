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
         * The part of the method name: add + "methodName"().
         */
        String methodName;
        /**
         * The property name.
         */
        String property;
        /**
         * Constructors argument type.
         */
        String type;
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

        public Property(String property) {
            this.property = property;
            this.methodName = Util.capitalize(property);
        }

        public Property(String argument, String type, String firstResponseConstructorArg, String secondResponseConstructorArg) {
            this.argument = argument;
            this.type = type;
            this.firstResponseConstructorArg = firstResponseConstructorArg;
            this.secondResponseConstructorArg = secondResponseConstructorArg;
        }
    }
}
