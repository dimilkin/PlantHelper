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
    private CompositeDisposable disposable = new CompositeDisposable();
    private PlantsInfoService plantsInfoService;


    public PlantsSearchViewModel(@NonNull Application application) {
        super(application);
        plantsInfoService = PlantsInfoServiceImpl.getInstance();
    }

    public void getShortInfoForPlants(String token) {
        if (plantsInfoList.getValue() != null){
            return;
        }
        disposable.add(plantsInfoService.getPlantsShortInfo(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<PlantShortInfo>>() {
                    @Override
                    public void onSuccess(List<PlantShortInfo> plantShortInfoList) {
                        plantsInfoList.setValue(new ArrayList<>());
                        plantsInfoList.setValue(plantShortInfoList);
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

    public void setPlantsInfoList(MutableLiveData<List<PlantShortInfo>> plantsInfoList) {
        this.plantsInfoList = plantsInfoList;
    }
}
