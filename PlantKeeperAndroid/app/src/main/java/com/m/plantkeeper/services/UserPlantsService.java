package com.m.plantkeeper.services;

import androidx.lifecycle.LiveData;

import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.UserPlant;
import com.m.plantkeeper.models.dtos.UserInfoDto;
import com.m.plantkeeper.models.dtos.UserPlantDto;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;

public interface UserPlantsService {

    Single<UserInfoDto> getUserPlantsFromServer(String token, int userId);

    Call<UserPlant> createNewUserPlantOnServer(String authToken, int userId, int plantid, UserPlant userPlantDto);

    Call<UserPlant> updateUserPlantOnServer(String authToken, int userId, int plantid, UserPlant userPlantDto);

    LiveData<List<UserPlant>> getUserPlantsLiveDataFromLocalStorage();

    List<UserPlant> getUserPlantsListFromLocalStorage(int userId) throws ExecutionException, InterruptedException;

    void saveUserPlantToLocalStorage(UserPlant userPlant);

    void saveListOfUserPlantsToStorage(List<UserPlant> userPlantList);

    void updateUserPlantToLocalStorage(UserPlant userPlant);


}
