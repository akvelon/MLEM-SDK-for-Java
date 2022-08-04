package com.akvelon.client.model.interface_;

/**
 * The class represents the model for the /interface.json response
 */
public class InterfaceModel {
    /**
     * The models version
     */
    private String version;
    /**
     * The class represents the methods models
     */
    private Methods methods;

    public InterfaceModel(String version, Methods methods) {
        this.version = version;
        this.methods = methods;
    }

    public InterfaceModel() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Methods getMethods() {
        return methods;
    }

    public void setMethods(Methods methods) {
        this.methods = methods;
    }
}
