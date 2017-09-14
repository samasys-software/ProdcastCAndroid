package com.samayu.prodcastc.businessObjects.dto;

/**
 * Created by God on 8/10/2017.
 */


public class AdminDTO<T> extends ProdcastDTO {

    private T result;
    private String successMessage;

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public T getResult(){
        return result;
    }

    public void setResult(T result){
        this.result = result;
    }
}
