package com.akvelon.client.model.interface_;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Arg {
    private String name;
    @JsonProperty("type_")
    private Type type;
    private boolean required;
    @JsonProperty("default")
    private String myDefault;
    @JsonProperty("kw_only")
    private boolean kwOnly;

    public Arg(String name, Type type, boolean required, String myDefault, boolean kwOnly) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.myDefault = myDefault;
        this.kwOnly = kwOnly;
    }

    public Arg() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Object getMyDefault() {
        return myDefault;
    }

    public void setMyDefault(String myDefault) {
        this.myDefault = myDefault;
    }

    public boolean isKwOnly() {
        return kwOnly;
    }

    public void setKwOnly(boolean kwOnly) {
        this.kwOnly = kwOnly;
    }
}
