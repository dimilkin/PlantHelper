package com.m.plantkeeper.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.m.plantkeeper.localdb.dao.UserPlantDao;
import com.m.plantkeeper.models.UserPlant;
import com.m.plantkeeper.models.dtos.UserInfoDto;
import com.m.plantkeeper.models.dtos.UserPlantDto;
import com.m.plantkeeper.services.UserPlantsService;
import com.m.plantkeeper.services.impl.UserPlantsServiceImpl;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainPlantsListViewModel extends AndroidViewModel {

    private MutableLiveData<List<UserPlant>> userPlantsList = new MutableLiveData<>();
    private UserPlantsService userPlantsService;

    private CompositeDisposable disposable = new CompositeDisposable();

    public MainPlantsListViewModel(@NonNull Application application) {
        super(application);
        userPlantsService = UserPlantsServiceImpl.getInstance(application);
    }

    public void initializeUserPlants(String token, int userId) throws ExecutionException, InterruptedException {
        List<UserPlant> userPlants = userPlantsService.getUserPlantsListFromLocalStorage(userId);
        if (userPlants != null && !userPlants.isEmpty()){
            userPlantsList.setValue(userPlants);
            return;
        }
        disposable.add(userPlantsService.getUserPlantsFromServer(token, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserInfoDto>() {
                    @Override
                    public void onSuccess(UserInfoDto userInfoDto) {
                        List<UserPlant> userPlants = transformUserPlantDtosToDataEntities(userInfoDto.getOwnPlants(), userId);
                        userPlantsList.setValue(userPlants);
                        userPlantsService.saveListOfUserPlantsToStorage(userPlants);
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

    public MutableLiveData<List<UserPlant>> getUserPlantsList() {
        return userPlantsList;
    }

    private List<UserPlant> transformUserPlantDtosToDataEntities(List<UserPlantDto> userPlantDtoList, int userOwnerId){
       return userPlantDtoList.stream()
                .map(userPlantDto -> generateUserPlant(userPlantDto, userOwnerId))
                .collect(Collectors.toList());
    }

    private UserPlant generateUserPlant(UserPlantDto userPlantDto, int userOwnerId) {
     UserPlant userPlant= new UserPlant();
     userPlant.setPlantId(userPlantDto.getPlant().getId());
     userPlant.setProvidedName(userPlantDto.getProvidedName());
     userPlant.setWaterPeriod(userPlantDto.getWaterPeriod());
     userPlant.setUserOwnerId(userOwnerId);
     return userPlant;
    }
}
