package com.m.plantkeeper.services.impl;

import com.m.plantkeeper.models.AuthResponse;
import com.m.plantkeeper.models.RegistrationInfo;
import com.m.plantkeeper.network.NetworkProvider;
import com.m.plantkeeper.services.AuthService;

import io.reactivex.Single;
import retrofit2.Call;

public class AuthServiceImpl implements AuthService {

    private static AuthServiceImpl instance;
    private NetworkProvider networkProvider;

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
    public Call<AuthResponse> authenticate(String email, String password) {
        return networkProvider.getConnection().authenticate(email, password);
    }

    @Override
    public Single<String> registerNewUser(RegistrationInfo registrationInfo) {
        return networkProvider.getConnection().registerNewUser(registrationInfo);
    }
}
