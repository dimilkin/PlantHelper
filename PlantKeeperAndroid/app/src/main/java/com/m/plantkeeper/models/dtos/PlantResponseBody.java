package com.m.plantkeeper.models.dtos;

public class PlantResponseBody {

    private String responseCode;
    private String responseStatus;

    public PlantResponseBody(String responseCode, String responseStatus) {
        this.responseCode = responseCode;
        this.responseStatus = responseStatus;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }
}
