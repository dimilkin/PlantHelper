package com.m.plantkeeper.models.dtos;

import com.m.plantkeeper.models.Plant;

public class UserPlantDto {

    private int id;
    private int waterPeriod;
    private String providedName;
    private Plant plant;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
