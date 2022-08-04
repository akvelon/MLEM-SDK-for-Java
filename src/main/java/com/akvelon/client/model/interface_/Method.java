package com.akvelon.client.model.interface_;

import java.util.ArrayList;

/**
 * The class represents the information about the method
 */
public class Method {
    /**
     * The method name
     */
    private String name;
    /**
     * The method arguments
     */
    private ArrayList<Arg> args;
    /**
     * The method returns
     */
    private Returns returns;
    /**
     * The method varargs
     */
    private String varargs;
    /**
     * The method varkw
     */
    private String varkw;

    public Method(String name, ArrayList<Arg> args, Returns returns, String varargs, String varkw) {
        this.name = name;
        this.args = args;
        this.returns = returns;
        this.varargs = varargs;
        this.varkw = varkw;
    }

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

    public Object getVarargs() {
        return varargs;
    }

    public void setVarargs(String varargs) {
        this.varargs = varargs;
    }

    public String getVarkw() {
        return varkw;
    }

    public void setVarkw(String varkw) {
        this.varkw = varkw;
    }
}
