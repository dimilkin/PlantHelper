package com.m.plantkeeper.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.UserPlant;
import com.m.plantkeeper.models.dtos.UserInfoDto;
import com.m.plantkeeper.services.UserPlantsService;
import com.m.plantkeeper.services.impl.UserPlantsServiceImpl;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainPlantsListViewModel extends AndroidViewModel {

    private MutableLiveData<List<UserPlant>> plantsList = new MutableLiveData<>();
    private UserPlantsService userPlantsService;

    private CompositeDisposable disposable = new CompositeDisposable();

    public MainPlantsListViewModel(@NonNull Application application) {
        super(application);
        userPlantsService = UserPlantsServiceImpl.getInstance();
    }

    public void initializePlants(String token, int userId) {
        disposable.add(userPlantsService.getUserPlants(token, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserInfoDto>() {
                    @Override
                    public void onSuccess(UserInfoDto userInfoDto) {
                        List<UserPlant> userPlants = userInfoDto.getOwnPlants();
                        plantsList.setValue(userPlants);
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

    public MutableLiveData<List<UserPlant>> getPlantsList() {
        return plantsList;
    }
}
