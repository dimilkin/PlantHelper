package com.m.plantkeeper.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.UserPlant;
import com.m.plantkeeper.models.dtos.UserPlantDto;
import com.m.plantkeeper.notifications.local.AlarmProvider;
import com.m.plantkeeper.services.PlantsInfoService;
import com.m.plantkeeper.services.UserPlantsService;
import com.m.plantkeeper.services.impl.PlantsInfoServiceImpl;
import com.m.plantkeeper.services.impl.UserPlantsServiceImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AddEditPlantViewModel extends AndroidViewModel {

    private PlantsInfoService plantsInfoService;
    private UserPlantsService userPlantsService;
    private CompositeDisposable disposable = new CompositeDisposable();
    private AlarmProvider alarmProvider;


    public AddEditPlantViewModel(@NonNull Application application) {
        super(application);
        plantsInfoService = PlantsInfoServiceImpl.getInstance(application);
        userPlantsService = UserPlantsServiceImpl.getInstance();
        alarmProvider = new AlarmProvider(application.getApplicationContext());
    }

    public Call<UserPlant> createNewUserPlant(String authToken, int userid, int plantId, UserPlantDto userPlantDto) {
        return userPlantsService.createNewUserPlant(authToken, userid, plantId, userPlantDto);
    }

    public void startNewAlarm(String name, int waterPeriodDays, int intentId){
        alarmProvider.startAlarm(name, waterPeriodDays, intentId);
    }

    public void savePlantDataFromServerToLocalStoarage(String authToken, int plantId) {
        if (plantsInfoService.plantExistsInStorage(plantId)){
            return;
        }
        disposable.add(plantsInfoService.getInfoForPlantByIdFromServer(authToken, plantId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Plant>() {
                    @Override
                    public void onSuccess(Plant plant) {
                        plantsInfoService.savePlantDataFromServerToLocalStoarage(plant);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Error:", "Failed to add plant to local storage", e);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
