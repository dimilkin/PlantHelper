package com.m.plantkeeper.viewmodels;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.m.plantkeeper.auth.LoginActivity;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivityViewModel extends ViewModel {

    private MutableLiveData<Boolean> userAuthenticated = new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> userRegistered = new MutableLiveData<Boolean>();
    private MutableLiveData<AuthCredentials> credentials = new MutableLiveData<AuthCredentials>();

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
                if (response.headers().get("authToken") != null) {
                    userAuthenticated.setValue(true);
                    String token = response.headers().get("authToken");
                    int userId = response.body() == null ? -1 : response.body();
                    AuthCredentials authCredentials = new AuthCredentials();
                    authCredentials.setUserToken(token);
                    authCredentials.setUserId(userId);
                    credentials.setValue(authCredentials);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
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

    public MutableLiveData<AuthCredentials> getCredentials() {
        return credentials;
    }
}
