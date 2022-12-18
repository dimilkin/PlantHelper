package com.m.plantkeeper.services;

import com.m.plantkeeper.models.AdditionalPlantInfo;
import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.PlantShortInfo;
import com.m.plantkeeper.models.PotentialPlantProblems;
import com.m.plantkeeper.models.dtos.PlantsInfoDto;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public interface PlantsInfoService {

    Single<List<PlantShortInfo>> getPlantsShortInfoFromServer(String token);

    Single<PlantsInfoDto> getInfoForPlantByIdFromServer(String authToken, int plantId);

    Plant getInfoForPlantbyIdFromLocalDb(int plantid) throws ExecutionException, InterruptedException;

    void savePlantDataFromServerToLocalStoarage(Plant plant);

    void saveAdditionalInfoForPlantToLocalStorage(Set<AdditionalPlantInfo> additionalPlantInfos, int plantId);

    void savePotentialProblemsForPlantToLocalStorage(Set<PotentialPlantProblems> potentialPlantProblems, int plantID);

    List<AdditionalPlantInfo> getAdditionalInfoForPlantById(int plantId) throws ExecutionException, InterruptedException;

    List<PotentialPlantProblems> getPotentialProblemsForPlantById(int plantId) throws ExecutionException, InterruptedException;

    boolean plantExistsInStorage(int plantId);
}
