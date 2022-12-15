package com.m.plantkeeper.viewmodels;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.m.plantkeeper.models.UserPlant;
import com.m.plantkeeper.models.dtos.PlantResponseBody;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if (userPlants != null && !userPlants.isEmpty()) {
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

    public void deleteUserPlant(String token, UserPlant userPlant) {
        userPlantsService.deleteUserPlant(token, userPlant).enqueue(new Callback<PlantResponseBody>() {
            @Override
            public void onResponse(Call<PlantResponseBody> call, Response<PlantResponseBody> response) {
                if (response.isSuccessful()) {
                    userPlantsService.deleteUserPlantFromLocalStorage(userPlant);
                    try {
                        initializeUserPlants(token, userPlant.getUserOwnerId());
                    } catch (ExecutionException | InterruptedException e) {
                        Log.e("ERROR", e.getMessage());
                    }
                }
                else {
                    Toast.makeText(getApplication(), "Deletion Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlantResponseBody> call, Throwable t) {
                Toast.makeText(getApplication(), "Deletion Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<List<UserPlant>> getUserPlantsList() {
        return userPlantsList;
    }

    private List<UserPlant> transformUserPlantDtosToDataEntities(List<UserPlantDto> userPlantDtoList, int userOwnerId) {
        return userPlantDtoList.stream()
                .map(userPlantDto -> generateUserPlant(userPlantDto, userOwnerId))
                .collect(Collectors.toList());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    private UserPlant generateUserPlant(UserPlantDto userPlantDto, int userOwnerId) {
        UserPlant userPlant = new UserPlant();
        userPlant.setId(userPlantDto.getId());
        userPlant.setPlantId(userPlantDto.getPlant().getId());
        userPlant.setProvidedName(userPlantDto.getProvidedName());
        userPlant.setWaterPeriod(userPlantDto.getWaterPeriod());
        userPlant.setUserOwnerId(userOwnerId);
        return userPlant;
    }
}
