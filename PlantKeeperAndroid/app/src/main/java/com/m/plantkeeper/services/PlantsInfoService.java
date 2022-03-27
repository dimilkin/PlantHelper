package com.m.plantkeeper.services;

import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.PlantShortInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public interface PlantsInfoService {

    Single<List<PlantShortInfo>> getPlantsShortInfoFromServer(String token);

    Single<Plant> getInfoForPlantByIdFromServer(String authToken, int plantId);

    Plant getInfoForPlantbyIdFromLocalDb(int plantid) throws ExecutionException, InterruptedException;

    void savePlantDataFromServerToLocalStoarage(Plant plant);

    boolean plantExistsInStorage(int plantId);
}
