package com.akvelon.client.model.interface_;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The class represents the methods models
 */
public class Methods {
    /**
     * The /predict method
     */
    private Method method;
    /**
     * The /predict_proba method
     */
    @JsonProperty("predict_proba")
    private Method methodProba;
    /**
     * The /sklearn_predict method
     */
    @JsonProperty("sklearn_predict")
    private Method sklearnMethod;
    /**
     * The /sklearn_predict_proba method
     */
    @JsonProperty("sklearn_predict_proba")
    private Method sklearnMethodProba;

    /**
     * The /torch_predict method
     */
    @JsonProperty("torch_predict")
    private Method torch_predict;

    public Methods(Method method, Method methodProba, Method sklearnMethod, Method sklearnMethodProba, Method torch_predict) {
        this.method = method;
        this.methodProba = methodProba;
        this.sklearnMethod = sklearnMethod;
        this.sklearnMethodProba = sklearnMethodProba;
        this.torch_predict = torch_predict;
    }

    public Methods() {
    }

    public Method getPredict() {
        return method;
    }

    public void setPredict(Method method) {
        this.method = method;
    }

    public Method getPredictProba() {
        return methodProba;
    }

    public void setPredictProba(Method methodProba) {
        this.methodProba = methodProba;
    }

    public Method getSklearnPredict() {
        return sklearnMethod;
    }

    public void setSklearnPredict(Method sklearnMethod) {
        this.sklearnMethod = sklearnMethod;
    }

    public Method getSklearnPredictProba() {
        return sklearnMethodProba;
    }

    public void setSklearnPredictProba(Method sklearnMethodProba) {
        this.sklearnMethodProba = sklearnMethodProba;
    }

    public Method getTorch_predict() {
        return torch_predict;
    }

    public void setTorch_predict(Method torch_predict) {
        this.torch_predict = torch_predict;
    }
}
