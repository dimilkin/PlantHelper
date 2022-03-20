package com.m.plantkeeper.services.impl;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.m.plantkeeper.models.UserPlant;
import com.m.plantkeeper.models.dtos.UserInfoDto;
import com.m.plantkeeper.models.dtos.UserPlantDto;
import com.m.plantkeeper.network.NetworkProvider;
import com.m.plantkeeper.services.UserPlantsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class UserPlantsServiceImpl implements UserPlantsService {

    private static UserPlantsServiceImpl instance;
    private NetworkProvider networkProvider;

    public UserPlantsServiceImpl() {
        networkProvider = new NetworkProvider();
    }

    public static UserPlantsServiceImpl getInstance() {
        if (instance == null) {
            instance = new UserPlantsServiceImpl();
        }
        return instance;
    }

    @Override
    public Single<UserInfoDto> getUserPlants(String token, int userId) {
        return networkProvider.getConnection().getInfoForUser(token, userId);
    }

    @Override
    public Call<ResponseBody> createNewUserPlant(String authToken, int userId, int plantid, UserPlantDto userPlantDto) {
        return networkProvider.getConnection().addPlantToUserProfile(authToken, userId, plantid, userPlantDto);
    }
}
