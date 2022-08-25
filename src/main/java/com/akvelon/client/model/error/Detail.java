package com.akvelon.client.model.error;

import java.util.ArrayList;
import java.util.List;

/**
 * The class represents the detail information about the error
 */
public class Detail {
    /**
     * The location of the error
     */
    private List<String> loc;
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

    public List<String> getLoc() {
        return loc;
    }

    public String getMsg() {
        return msg;
    }

    public String getType() {
        return type;
    }
}