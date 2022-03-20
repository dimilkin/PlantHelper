package com.m.plantkeeper.services;

import com.m.plantkeeper.models.PlantShortInfo;

import java.util.List;

import io.reactivex.Single;

public interface PlantsInfoService {

    Single<List<PlantShortInfo>> getPlantsShortInfo(String token);
}
