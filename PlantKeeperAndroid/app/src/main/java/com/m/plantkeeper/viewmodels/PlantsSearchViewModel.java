package com.m.plantkeeper.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.m.plantkeeper.models.PlantShortInfo;
import com.m.plantkeeper.services.PlantsInfoService;
import com.m.plantkeeper.services.impl.PlantsInfoServiceImpl;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PlantsSearchViewModel extends AndroidViewModel {

    private MutableLiveData<List<PlantShortInfo>> plantsInfoList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isInfoLoaded = new MutableLiveData<>();

    private final CompositeDisposable disposable = new CompositeDisposable();
    private final PlantsInfoService plantsInfoService;


    public PlantsSearchViewModel(@NonNull Application application) {
        super(application);
        plantsInfoService = PlantsInfoServiceImpl.getInstance(application);
        isInfoLoaded.setValue(false);
    }

    public void getShortInfoForPlants(String token) {
        if (plantsInfoList.getValue() != null){
            isInfoLoaded.setValue(true);
            return;
        }
        disposable.add(plantsInfoService.getPlantsShortInfoFromServer(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<PlantShortInfo>>() {
                    @Override
                    public void onSuccess(List<PlantShortInfo> plantShortInfoList) {
                        plantsInfoList.setValue(new ArrayList<>());
                        plantsInfoList.setValue(plantShortInfoList);
                        isInfoLoaded.setValue(true);
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public MutableLiveData<List<PlantShortInfo>> getPlantsInfoList() {
        return plantsInfoList;
    }

    public MutableLiveData<Boolean> getIsInfoLoaded() {
        return isInfoLoaded;
    }
}
