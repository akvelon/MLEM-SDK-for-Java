package com.akvelon.client.model.request;

import java.util.HashMap;

public class Request {
    private final HashMap<String, RecordSet> request = new HashMap<>();

    public void addRecordSet(String propertyName, RecordSet recordSet) {
        request.put(propertyName, recordSet);
    }
}
