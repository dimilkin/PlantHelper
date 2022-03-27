package com.m.plantkeeper.viewmodels;

import android.app.Application;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.services.PlantsInfoService;
import com.m.plantkeeper.services.impl.PlantsInfoServiceImpl;

import java.util.concurrent.ExecutionException;

public class PlantInfoViewModel extends AndroidViewModel {

    private PlantsInfoService plantsInfoService;

    public PlantInfoViewModel(@NonNull Application application) {
        super(application);
        plantsInfoService = PlantsInfoServiceImpl.getInstance(application);
    }

    public Plant getInfoForPlantbyId(int plantid) throws ExecutionException, InterruptedException, Resources.NotFoundException {
        return plantsInfoService.getInfoForPlantbyIdFromLocalDb(plantid);
    }
}
