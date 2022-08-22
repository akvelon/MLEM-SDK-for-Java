package com.akvelon.client.model.validation;

public class ParameterDesc {
    private String name;
    private RecordSetDesc type;

    public ParameterDesc(String name, RecordSetDesc type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public RecordSetDesc getType() {
        return type;
    }
}