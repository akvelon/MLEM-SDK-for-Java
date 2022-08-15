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

    public void setName(String name) {
        this.name = name;
    }

    public RecordSetDesc getType() {
        return type;
    }

    public void setType(RecordSetDesc type) {
        this.type = type;
    }
}
