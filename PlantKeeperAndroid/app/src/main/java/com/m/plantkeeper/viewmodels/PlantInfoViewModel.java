package com.m.plantkeeper.viewmodels;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.services.AuthService;
import com.m.plantkeeper.services.PlantsInfoService;
import com.m.plantkeeper.services.impl.AuthServiceImpl;
import com.m.plantkeeper.services.impl.PlantsInfoServiceImpl;

import java.util.concurrent.ExecutionException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PlantInfoViewModel extends AndroidViewModel {

    private PlantsInfoService plantsInfoService;
    private CompositeDisposable disposable = new CompositeDisposable();
    private AuthService authService;

    private MutableLiveData<Plant> currentPlant = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingPlantInfo = new MutableLiveData<>();


    public PlantInfoViewModel(@NonNull Application application) {
        super(application);
        plantsInfoService = PlantsInfoServiceImpl.getInstance(application);
        authService = AuthServiceImpl.getAuthInstance();
        loadingPlantInfo.setValue(true);
    }

    public Plant getInfoForPlantbyId(int plantid) throws ExecutionException, InterruptedException, Resources.NotFoundException {
        try {
            currentPlant.setValue(plantsInfoService.getInfoForPlantbyIdFromLocalDb(plantid));
            loadingPlantInfo.setValue(false);
        } catch (Resources.NotFoundException exception) {
            String authToken = authService.getAuthCredentials().getUserToken();
            savePlantDataFromServerToLocalStoarage(authToken, plantid);
            loadingPlantInfo.setValue(true);
        }
        throw new Resources.NotFoundException();
    }

    public void savePlantDataFromServerToLocalStoarage(String authToken, int plantId) {
        if (plantsInfoService.plantExistsInStorage(plantId)) {
            return;
        }
        disposable.add(plantsInfoService.getInfoForPlantByIdFromServer(authToken, plantId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Plant>() {
                    @Override
                    public void onSuccess(Plant plant) {
                        plantsInfoService.savePlantDataFromServerToLocalStoarage(plant);
                        currentPlant.setValue(plant);
                        loadingPlantInfo.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Error:", "Failed to add plant to local storage", e);
                        loadingPlantInfo.setValue(true);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
