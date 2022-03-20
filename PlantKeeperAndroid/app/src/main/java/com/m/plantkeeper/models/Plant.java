package com.m.plantkeeper.models;

import androidx.room.Entity;

@Entity(tableName = "plants_info")
public class Plant {

    private int id;
    private String origin;
    private String commonName;
    private String scientificName;
    private String maxGrowth;
    private String poisonousForPets;
    private String temperature;
    private String light;
    private String watering;
    private String soil;
    private String rePotting;
    private String airHumidity;
    private String propagation;
    private String whereItGrowsBest;

    public Plant() {
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getMaxGrowth() {
        return maxGrowth;
    }

    public void setMaxGrowth(String maxGrowth) {
        this.maxGrowth = maxGrowth;
    }

    public String getPoisonousForPets() {
        return poisonousForPets;
    }

    public void setPoisonousForPets(String poisonousForPets) {
        this.poisonousForPets = poisonousForPets;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getLight() {
        return light;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getWatering() {
        return watering;
    }

    public void setWatering(String watering) {
        this.watering = watering;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public String getRePotting() {
        return rePotting;
    }

    public void setRePotting(String rePotting) {
        this.rePotting = rePotting;
    }

    public String getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(String airHumidity) {
        this.airHumidity = airHumidity;
    }

    public String getPropagation() {
        return propagation;
    }

    public void setPropagation(String propagation) {
        this.propagation = propagation;
    }

    public String getWhereItGrowsBest() {
        return whereItGrowsBest;
    }

    public void setWhereItGrowsBest(String whereItGrowsBest) {
        this.whereItGrowsBest = whereItGrowsBest;
    }

}
