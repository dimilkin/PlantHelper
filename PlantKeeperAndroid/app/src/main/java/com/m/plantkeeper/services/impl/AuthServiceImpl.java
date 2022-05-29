package com.m.plantkeeper.services.impl;

import com.m.plantkeeper.models.AuthCredentials;
import com.m.plantkeeper.models.RegistrationInfo;
import com.m.plantkeeper.models.dtos.AccountActivationDto;
import com.m.plantkeeper.network.NetworkProvider;
import com.m.plantkeeper.services.AuthService;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AuthServiceImpl implements AuthService {

    private static AuthServiceImpl instance;
    private NetworkProvider networkProvider;
    private AuthCredentials authCredentials;

    private AuthServiceImpl() {
        networkProvider = new NetworkProvider();
    }

    public static AuthServiceImpl getAuthInstance() {
        if (instance == null) {
            instance = new AuthServiceImpl();
        }
        return instance;
    }


    @Override
    public Call<Integer> authenticate(String email, String password) {
        return networkProvider.getConnection().authenticate(email, password);
    }

    @Override
    public Single<ResponseBody> registerNewUser(RegistrationInfo registrationInfo) {
        return networkProvider.getConnection().registerNewUser(registrationInfo);
    }

    @Override
    public Call<ResponseBody> activateNewUser(AccountActivationDto accountActivationDt) {
        return networkProvider.getConnection().activateNewUser(accountActivationDt);
    }

    @Override
    public AuthCredentials getAuthCredentials() {
        return authCredentials;
    }

    @Override
    public void setAuthCredentials(AuthCredentials authCredentials) {
        this.authCredentials = authCredentials;
    }
}
