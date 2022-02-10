package com.m.plantkeeper.services;

import io.reactivex.Single;
import retrofit2.Call;


import com.m.plantkeeper.models.AuthResponse;
import com.m.plantkeeper.models.RegistrationInfo;

public interface AuthService {

    Call<AuthResponse> authenticate(String email, String password);

    Single<String> registerNewUser(RegistrationInfo registrationInfo);

}
