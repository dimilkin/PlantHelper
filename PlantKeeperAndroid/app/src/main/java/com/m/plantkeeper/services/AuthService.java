package com.m.plantkeeper.services;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;


import com.m.plantkeeper.models.AuthCredentials;
import com.m.plantkeeper.models.AuthResponse;
import com.m.plantkeeper.models.RegistrationInfo;
import com.m.plantkeeper.models.dtos.AccountActivationDto;

public interface AuthService {

    Call<Integer> authenticate(String email, String password);

    Single<ResponseBody> registerNewUser(RegistrationInfo registrationInfo);

    Call<ResponseBody> activateNewUser(AccountActivationDto accountActivationDt);

    AuthCredentials getAuthCredentials();

    void setAuthCredentials(AuthCredentials authCredentials);

}
