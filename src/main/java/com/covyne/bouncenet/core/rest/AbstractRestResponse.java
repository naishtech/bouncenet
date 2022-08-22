package com.covyne.bouncenet.core.rest;

public abstract class AbstractRestResponse {

    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
