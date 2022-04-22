package com.m.plantkeeper.services;

import androidx.lifecycle.LiveData;

import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.UserPlant;
import com.m.plantkeeper.models.dtos.UserInfoDto;
import com.m.plantkeeper.models.dtos.UserPlantDto;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;

public interface UserPlantsService {

    Single<UserInfoDto> getUserPlants(String token, int userId);

    Call<UserPlant> createNewUserPlant(String authToken, int userId, int plantid, UserPlantDto userPlantDto);
}
