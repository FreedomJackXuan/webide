package com.webide.vo;

import javax.validation.constraints.NotNull;

public class LoginByToken {
    @NotNull
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
