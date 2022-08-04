package com.akvelon.client.model.error;

import java.util.ArrayList;

/**
 * The class represents the detail information about the error
 */
public class Detail {
    /**
     * The location of the error
     */
    private ArrayList<String> loc;
    /**
     * The message of the error
     */
    private String msg;
    /**
     * The type of the error
     */
    private String type;

    public Detail(ArrayList<String> loc, String msg, String type) {
        this.loc = loc;
        this.msg = msg;
        this.type = type;
    }

    public Detail() {
    }

    public ArrayList<String> getLoc() {
        return loc;
    }

    public void setLoc(ArrayList<String> loc) {
        this.loc = loc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}