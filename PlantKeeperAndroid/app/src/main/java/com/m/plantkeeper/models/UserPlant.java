package com.m.plantkeeper.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_plants")
public class UserPlant {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int waterPeriod;
    private String providedName;
    private int plantId;
    private int userOwnerId;
    private int assugnedUserId;

    public UserPlant() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPlantId() {
        return plantId;
    }

    public void setPlantId(int plantId) {
        this.plantId = plantId;
    }

    public int getUserOwnerId() {
        return userOwnerId;
    }

    public void setUserOwnerId(int userOwnerId) {
        this.userOwnerId = userOwnerId;
    }

    public int getAssugnedUserId() {
        return assugnedUserId;
    }

    public void setAssugnedUserId(int assugnedUserId) {
        this.assugnedUserId = assugnedUserId;
    }
}
