package com.m.plantkeeper.models.dtos;

import com.m.plantkeeper.models.UserPlant;

import java.util.List;

public class UserInfoDto {

    private int id;
    private String email;
    private List<UserPlantDto> ownPlants;
    private List<UserPlantDto> assignedPlants;

    public UserInfoDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserPlantDto> getOwnPlants() {
        return ownPlants;
    }

    public void setOwnPlants(List<UserPlantDto> ownPlants) {
        this.ownPlants = ownPlants;
    }

    public List<UserPlantDto> getAssignedPlants() {
        return assignedPlants;
    }

    public void setAssignedPlants(List<UserPlantDto> assignedPlants) {
        this.assignedPlants = assignedPlants;
    }
}
