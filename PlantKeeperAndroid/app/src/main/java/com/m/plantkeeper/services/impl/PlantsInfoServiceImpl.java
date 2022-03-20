package com.m.plantkeeper.services.impl;

import com.m.plantkeeper.models.PlantShortInfo;
import com.m.plantkeeper.network.NetworkProvider;
import com.m.plantkeeper.services.PlantsInfoService;

import java.util.List;

import io.reactivex.Single;

public class PlantsInfoServiceImpl implements PlantsInfoService {

    private static PlantsInfoServiceImpl instance;
    private NetworkProvider networkProvider;

    public PlantsInfoServiceImpl() {
        networkProvider = new NetworkProvider();
    }

    public static PlantsInfoServiceImpl getInstance() {
        if (instance == null) {
            instance = new PlantsInfoServiceImpl();
        }
        return instance;
    }

    @Override
    public Single<List<PlantShortInfo>> getPlantsShortInfo(String token) {
        return networkProvider.getConnection().getPlantsNamesAndIds(token);
    }
}
