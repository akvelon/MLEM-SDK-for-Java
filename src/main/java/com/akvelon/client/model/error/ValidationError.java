package com.akvelon.client.model.error;

import java.util.ArrayList;
import java.util.List;

/**
 * The class represents the validation error.
 */
public class ValidationError {
    /**
     * The error details.
     */
    private final List<Detail> detail;

    public ValidationError(List<Detail> detail) {
        this.detail = detail;
    }

    public List<Detail> getDetail() {
        return detail;
    }

}