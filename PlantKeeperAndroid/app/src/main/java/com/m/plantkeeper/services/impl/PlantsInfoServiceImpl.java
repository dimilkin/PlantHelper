package com.m.plantkeeper.services.impl;

import android.app.Application;
import android.content.res.Resources;

import com.m.plantkeeper.localdb.AdditionalPlantInfoRepository;
import com.m.plantkeeper.localdb.PlantsRepository;
import com.m.plantkeeper.localdb.PotentialPlantProblemsRepository;
import com.m.plantkeeper.localdb.dao.AdditionalPlantInfoDao;
import com.m.plantkeeper.models.AdditionalPlantInfo;
import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.PlantShortInfo;
import com.m.plantkeeper.models.PotentialPlantProblems;
import com.m.plantkeeper.models.dtos.PlantsInfoDto;
import com.m.plantkeeper.network.NetworkProvider;
import com.m.plantkeeper.services.PlantsInfoService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class PlantsInfoServiceImpl implements PlantsInfoService {

    private static PlantsInfoServiceImpl instance;
    private NetworkProvider networkProvider;
    private PlantsRepository plantsRepository;
    private AdditionalPlantInfoRepository additionalPlantInfoRepository;
    private PotentialPlantProblemsRepository potentialPlantProblemsRepository;

    public PlantsInfoServiceImpl(Application application) {
        networkProvider = new NetworkProvider();
        plantsRepository = PlantsRepository.getPlantRepositoryInstance(application);
        additionalPlantInfoRepository = AdditionalPlantInfoRepository.getPlantRepositoryInstance(application);
        potentialPlantProblemsRepository = PotentialPlantProblemsRepository.getPlantRepositoryInstance(application);
    }

    public static PlantsInfoServiceImpl getInstance(Application application) {
        if (instance == null) {
            instance = new PlantsInfoServiceImpl(application);
        }
        return instance;
    }

    @Override
    public void savePlantDataFromServerToLocalStoarage(Plant plant) {
        plantsRepository.insert(plant);
    }

    @Override
    public Single<List<PlantShortInfo>> getPlantsShortInfoFromServer(String token) {
        return networkProvider.getConnection().getPlantsNamesAndIds(token);
    }

    @Override
    public Single<PlantsInfoDto> getInfoForPlantByIdFromServer(String authToken, int plantId) {
        return networkProvider.getConnection().getInfoForPlant(authToken, plantId);
    }

    @Override
    public Plant getInfoForPlantbyIdFromLocalDb(int plantid) throws ExecutionException, InterruptedException {
        if (plantsRepository.plantWithIdExists(plantid)) {
            return plantsRepository.getPlantFromDbById(plantid);
        }
        throw new Resources.NotFoundException();
    }

    @Override
    public void saveAdditionalInfoForPlantToLocalStorage(Set<AdditionalPlantInfo> additionalPlantInfos, int plantId) {
        for (AdditionalPlantInfo additionalPlantInfo : additionalPlantInfos) {
            additionalPlantInfo.setPlantId(plantId);
            additionalPlantInfoRepository.insert(additionalPlantInfo);
        }
    }

    @Override
    public void savePotentialProblemsForPlantToLocalStorage(Set<PotentialPlantProblems> potentialPlantProblems, int plantId) {
        for (PotentialPlantProblems potentialPlantProblem : potentialPlantProblems) {
            potentialPlantProblem.setPlantId(plantId);
            potentialPlantProblemsRepository.insert(potentialPlantProblem);
        }
    }

    @Override
    public List<AdditionalPlantInfo> getAdditionalInfoForPlantById(int plantId) throws ExecutionException, InterruptedException {
        return additionalPlantInfoRepository.getAdditionalInforListFroPlantbyId(plantId);
    }

    @Override
    public List<PotentialPlantProblems> getPotentialProblemsForPlantById(int plantId) throws ExecutionException, InterruptedException {
        return potentialPlantProblemsRepository.getPotentialProblemdForPlantById(plantId);
    }

    @Override
    public boolean plantExistsInStorage(int plantId) {
        return plantsRepository.plantWithIdExists(plantId);
    }
}
