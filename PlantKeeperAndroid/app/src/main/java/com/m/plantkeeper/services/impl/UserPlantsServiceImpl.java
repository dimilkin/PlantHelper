package com.m.plantkeeper.services.impl;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.m.plantkeeper.localdb.UserPlantsRepository;
import com.m.plantkeeper.models.UserPlant;
import com.m.plantkeeper.models.dtos.PlantResponseBody;
import com.m.plantkeeper.models.dtos.UserInfoDto;
import com.m.plantkeeper.network.NetworkProvider;
import com.m.plantkeeper.services.UserPlantsService;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;
import retrofit2.Call;

public class UserPlantsServiceImpl implements UserPlantsService {

    private static UserPlantsServiceImpl instance;
    private NetworkProvider networkProvider;
    private UserPlantsRepository userPlantsRepository;

    public UserPlantsServiceImpl(Application application) {
        networkProvider = new NetworkProvider();
        userPlantsRepository = UserPlantsRepository.getInstance(application);
    }

    public static UserPlantsServiceImpl getInstance(Application application) {
        if (instance == null) {
            instance = new UserPlantsServiceImpl(application);
        }
        return instance;
    }

    @Override
    public Single<UserInfoDto> getUserPlantsFromServer(String token, int userId) {
        return networkProvider.getConnection().getInfoForUser(token, userId);
    }

    @Override
    public Call<UserPlant> createNewUserPlantOnServer(String authToken, int userId, int plantid, UserPlant userPlantDto) {
        return networkProvider.getConnection().addPlantToUserProfile(authToken, userId, plantid, userPlantDto);
    }

    @Override
    public Call<UserPlant> updateUserPlantOnServer(String authToken, int userId, int plantid, UserPlant userPlantDto) {
        return networkProvider.getConnection().updateUserPlant(authToken, userId, plantid, userPlantDto);
    }

    @Override
    public LiveData<List<UserPlant>> getUserPlantsLiveDataFromLocalStorage() {
        return userPlantsRepository.getAllUserPlantsLiveData();
    }

    @Override
    public List<UserPlant> getUserPlantsListFromLocalStorage(int userId) throws ExecutionException, InterruptedException {
        return userPlantsRepository.getAllUserPlantsListForUserById(userId);
    }

    @Override
    public void saveUserPlantToLocalStorage(UserPlant userPlant) {
        userPlantsRepository.insert(userPlant);
    }

    @Override
    public void updateUserPlantToLocalStorage(UserPlant userPlant) {
        userPlantsRepository.update(userPlant);
    }

    @Override
    public void saveListOfUserPlantsToStorage(List<UserPlant> userPlantList) {
        userPlantList.forEach(this::saveUserPlantToLocalStorage);
    }

    @Override
    public Call<PlantResponseBody> deleteUserPlant(String authToken, UserPlant userPlant) {
        return networkProvider.getConnection().removePlantFromUserProfile(authToken, userPlant.getUserOwnerId(), userPlant.getId());
    }

    @Override
    public void deleteUserPlantFromLocalStorage(UserPlant userPlant) {
        userPlantsRepository.delete(userPlant);
    }
}
