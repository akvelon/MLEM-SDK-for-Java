package com.akvelon.client.model.error;

import java.util.ArrayList;

/**
 * The class represents the validation error
 */
public class ValidationError {
    /**
     * The error details
     */
    private ArrayList<Detail> detail;

    public ValidationError(ArrayList<Detail> detail) {
        this.detail = detail;
    }

    public ValidationError() {
    }

    public ArrayList<Detail> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<Detail> detail) {
        this.detail = detail;
    }
}