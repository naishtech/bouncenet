package com.covyne.bouncenet.admin;

import com.covyne.bouncenet.core.rest.AbstractRestResponse;

public class GetUserResponse extends AbstractRestResponse {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
