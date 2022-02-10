package com.m.plantkeeper.models;

public class AuthCredentials {

    private int userId;
    private String userToken;

    public AuthCredentials() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
