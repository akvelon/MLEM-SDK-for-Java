package com.akvelon.client.model.validation.method;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Method {
    private String name;
    private ArrayList<Arg> args;
    private Returns returns;

    public Method() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Arg> getArgs() {
        return args;
    }

    public void setArgs(ArrayList<Arg> args) {
        this.args = args;
    }

    public Returns getReturns() {
        return returns;
    }

    public void setReturns(Returns returns) {
        this.returns = returns;
    }
}
