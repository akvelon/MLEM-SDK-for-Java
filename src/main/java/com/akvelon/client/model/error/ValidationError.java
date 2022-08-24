package com.akvelon.client.model.error;

import java.util.ArrayList;
import java.util.List;

/**
 * The class represents the validation error
 */
public class ValidationError {
    /**
     * The error details
     */
    private List<Detail> detail;

    public ValidationError(List<Detail> detail) {
        this.detail = detail;
    }

    public ValidationError() {
    }

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<Detail> detail) {
        this.detail = detail;
    }
}