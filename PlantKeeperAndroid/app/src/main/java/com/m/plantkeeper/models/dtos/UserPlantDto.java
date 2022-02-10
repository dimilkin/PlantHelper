package com.m.plantkeeper.models.dtos;

public class UserPlantDto {

    private int waterPeriod;
    private String providedName;

    public UserPlantDto() {
    }

    public int getWaterPeriod() {
        return waterPeriod;
    }

    public void setWaterPeriod(int waterPeriod) {
        this.waterPeriod = waterPeriod;
    }

    public String getProvidedName() {
        return providedName;
    }

    public void setProvidedName(String providedName) {
        this.providedName = providedName;
    }
}
