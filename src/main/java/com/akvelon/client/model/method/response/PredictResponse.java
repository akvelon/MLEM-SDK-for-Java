package com.akvelon.client.model.method.response;

import java.util.ArrayList;

public class PredictResponse {
    private ArrayList<Integer> myArray;

    public PredictResponse(ArrayList<Integer> myArray) {
        this.myArray = myArray;
    }

    public PredictResponse() {
    }

    public ArrayList<Integer> getMyArray() {
        return myArray;
    }

    public void setMyArray(ArrayList<Integer> myArray) {
        this.myArray = myArray;
    }
}
