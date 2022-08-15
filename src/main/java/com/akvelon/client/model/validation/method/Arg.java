package com.akvelon.client.model.validation.method;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Arg {
    private String name;
    private Type type_;
    private boolean required;
    @JsonProperty("kw_only")
    private boolean kwOnly;

    public Arg() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType_() {
        return type_;
    }

    public void setType_(Type type_) {
        this.type_ = type_;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isKwOnly() {
        return kwOnly;
    }

    public void setKwOnly(boolean kwOnly) {
        this.kwOnly = kwOnly;
    }
}
