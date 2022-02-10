package com.m.plantkeeper.models.dtos;

import com.m.plantkeeper.models.UserPlant;

import java.util.List;

public class UserInfoDto {

    private int id;
    private String email;
    private List<UserPlant> ownPlants;
    private List<UserPlant> assignedPlants;

}
