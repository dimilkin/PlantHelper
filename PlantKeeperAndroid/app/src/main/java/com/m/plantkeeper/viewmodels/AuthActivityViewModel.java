package com.m.plantkeeper.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.m.plantkeeper.models.AuthCredentials;
import com.m.plantkeeper.models.RegistrationInfo;
import com.m.plantkeeper.models.dtos.AccountActivationDto;
import com.m.plantkeeper.services.AuthService;
import com.m.plantkeeper.services.impl.AuthServiceImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivityViewModel extends ViewModel {

    private MutableLiveData<Boolean> userAuthenticated = new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> userActivated = new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> userRegistered = new MutableLiveData<Boolean>();

    private AuthService authService;
    private CompositeDisposable disposable = new CompositeDisposable();


    public AuthActivityViewModel() {
        authService = AuthServiceImpl.getAuthInstance();
    }

    public void authenticate(String email, String password) {
        Call<Integer> authCall = authService.authenticate(email, password);
        authCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.raw().code() == 401) {
                    Log.i("Auth:", "Unauthorized");
                    userAuthenticated.setValue(false);
                }
                if (response.headers().get("authToken") != null) {
                    userAuthenticated.setValue(true);
                    String token = "Bearer " + response.headers().get("authToken");
                    int userId = response.body() == null ? -1 : response.body();
                    AuthCredentials authCredentials = new AuthCredentials();
                    authCredentials.setUserToken(token);
                    authCredentials.setUserId(userId);
                    authService.setAuthCredentials(authCredentials);

                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
                userAuthenticated.setValue(false);
            }
        });
    }

    public void registerNewUser(RegistrationInfo registrationInfo) {
        disposable.add(authService.registerNewUser(registrationInfo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(@NonNull ResponseBody body) {
                        userRegistered.setValue(true);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        userRegistered.setValue(false);
                    }
                }));
    }

    public void activateNewUser(AccountActivationDto accountActivationDto) {
        Call<ResponseBody> authCall = authService.activateNewUser(accountActivationDto);
        authCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.i("Auth", "Successful Activation with code : " + response.code());
                    userActivated.setValue(true);
                    return;
                }
                if (response.code() == 400) {
                    Log.i("Auth:", "Wrong Activation Code : " + response.code());
                    userActivated.setValue(false);
                    return;
                }
                userActivated.setValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
                userActivated.setValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public MutableLiveData<Boolean> isUserAuthenticated() {
        return userAuthenticated;
    }

    public MutableLiveData<Boolean> isUserRegistered() {
        return userRegistered;
    }

    public MutableLiveData<Boolean> isUserActivated() {
        return userActivated;
    }
}
