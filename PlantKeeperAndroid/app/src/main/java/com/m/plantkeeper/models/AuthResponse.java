package com.m.plantkeeper.models;

public class AuthResponse {

    private int userId;
    private String userEmail;

    public AuthResponse() {
    }

    public int getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
