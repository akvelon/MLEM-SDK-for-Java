package com.akvelon.client.model.validation;

/**
 * Class represents the parameter description for validation the parameters objects.
 */
public class ParameterDesc {
    /**
     * The parameter name.
     */
    private final String name;
    /**
     * The parameter record set description.
     */
    private final RecordSetDesc type;

    /**
     * Construct a new ParameterDesc.
     *
     * @param name the parameter name.
     * @param type the parameter record set description.
     */
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