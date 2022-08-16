package com.akvelon.client.model.validation;

public class ParameterDesc {
    private String name;
    private RecordSetColumnsDesc type;

    public ParameterDesc(String name, RecordSetColumnsDesc type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RecordSetColumnsDesc getType() {
        return type;
    }

    public void setType(RecordSetColumnsDesc type) {
        this.type = type;
    }
}