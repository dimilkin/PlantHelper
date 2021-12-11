package com.m.plantkeeper.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.m.plantkeeper.models.Plant;

import java.util.ArrayList;
import java.util.List;

public class MainPlantsListViewModel extends AndroidViewModel {

    public MutableLiveData<List<Plant>> plantsList = new MutableLiveData<>();



    public MainPlantsListViewModel(@NonNull Application application) {
        super(application);
        initializePlants();
    }

    private void initializePlants(){

        Plant plant1 = new Plant();
        Plant plant2 = new Plant();
        Plant plant3 = new Plant();

        plant1.setCommonName("Orchidea");
        plant1.setWatering("every two weeks");
        plant2.setCommonName("Kokiche");
        plant2.setWatering("every week");
        plant3.setCommonName("Cactus");
        plant3.setWatering("every two months");

        List<Plant> plants = new ArrayList<>();
        plants.add(plant1);
        plants.add(plant2);
        plants.add(plant3);
        plants.add(plant1);
        plants.add(plant2);
        plants.add(plant3);
        plants.add(plant1);

        plantsList.setValue(plants);

    }
}
