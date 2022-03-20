package com.m.plantkeeper.models.dtos;

import com.m.plantkeeper.models.UserPlant;

import java.util.List;

public class UserInfoDto {

    private int id;
    private String email;
    private List<UserPlant> ownPlants;
    private List<UserPlant> assignedPlants;

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

    public List<UserPlant> getOwnPlants() {
        return ownPlants;
    }

    public void setOwnPlants(List<UserPlant> ownPlants) {
        this.ownPlants = ownPlants;
    }

    public List<UserPlant> getAssignedPlants() {
        return assignedPlants;
    }

    public void setAssignedPlants(List<UserPlant> assignedPlants) {
        this.assignedPlants = assignedPlants;
    }
}
