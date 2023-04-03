package com.akvelon.client.modelgenerator;


import java.util.List;

/**
 * A class that provides a Java class description.
 */
public class Context {
    /**
     * Attributes or features that characterize request classes.
     */
    List<RequestProperty> requestProperties;
    /**
     * Attributes or features that characterize response classes.
     */
    List<ResponseProperty> responseProperties;
    /**
     * Representing the name of a Java class.
     */
    String className;
    /**
     * Used to group related classes.
     */
    String packages;

    /**
     * Used to add an import.
     */
    String importClass;

    public void setRequestProperties(List<RequestProperty> requestProperties) {
        this.requestProperties = requestProperties;
    }

    public void setResponseProperties(List<ResponseProperty> responseProperties) {
        this.responseProperties = responseProperties;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public void setImportClass(String importClass) {
        this.importClass = importClass;
    }

    /**
     * Attribute or feature that characterize classes.
     */
    public static class RequestProperty {
        /**
         * The part of the method name: add + "methodName"().
         */
        String methodName;
        /**
         * The property name.
         */
        String property;

        /**
         * The value class
         */
        String valueClass;

        public RequestProperty(String property, String valueClass) {
            this.property = property;
            this.methodName = Util.capitalize(property);
            this.valueClass = valueClass;
        }
    }

    /**
     * Attribute or feature that characterize classes.
     */
    public static class ResponseProperty {
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
        String responseConstructorFirstArg;
        /**
         * Second argument for call super() in the Response body constructor.
         * See response.mustache file.
         */
        String responseConstructorSecondArg;

        public ResponseProperty(String argument, String type, String responseConstructorFirstArg, String responseConstructorSecondArg) {
            this.argument = argument;
            this.type = type;
            this.responseConstructorFirstArg = responseConstructorFirstArg;
            this.responseConstructorSecondArg = responseConstructorSecondArg;
        }
    }
}
