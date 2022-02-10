package com.m.plantkeeper.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.m.plantkeeper.models.AuthCredentials;
import com.m.plantkeeper.models.AuthResponse;
import com.m.plantkeeper.models.RegistrationInfo;
import com.m.plantkeeper.services.AuthService;
import com.m.plantkeeper.services.impl.AuthServiceImpl;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivityViewModel extends ViewModel {

    public MutableLiveData<Boolean> isUserAuthenticated = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> isUserRegistered = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loadingError = new MutableLiveData<Boolean>();
    public MutableLiveData<AuthCredentials> credentials = new MutableLiveData<AuthCredentials>();

    private AuthService authService;
    private CompositeDisposable disposable = new CompositeDisposable();


    public AuthActivityViewModel() {
        authService = AuthServiceImpl.getAuthInstance();
    }

    public void authenticate(String email, String password) {
        isLoading.setValue(true);
        authService
                .authenticate(email, password)
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                        isLoading.setValue(false);
                        isUserAuthenticated.setValue(true);
                        loadingError.setValue(false);
                        String token = response.headers().get("authToken");
                        int userId = response.body().getUserId();
                        AuthCredentials authCredentials = new AuthCredentials();
                        authCredentials.setUserToken(token);
                        authCredentials.setUserId(userId);
                        credentials.setValue(authCredentials);
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        isLoading.setValue(false);
                        isUserAuthenticated.setValue(false);
                        loadingError.setValue(true);
                    }
                });
    }

    public void registerNewUser (RegistrationInfo registrationInfo){
        isLoading.setValue(true);
        disposable.add(authService.registerNewUser(registrationInfo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(@NonNull String s) {
                        isLoading.setValue(false);
                        isUserRegistered.setValue(true);
                        loadingError.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoading.setValue(false);
                        isUserRegistered.setValue(false);
                        loadingError.setValue(true);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
