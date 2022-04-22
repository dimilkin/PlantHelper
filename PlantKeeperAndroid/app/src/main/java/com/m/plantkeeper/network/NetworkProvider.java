package com.m.plantkeeper.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkProvider  {

//    public static final String BASE_URL = "https://plant-app-spring-backend.herokuapp.com/";
    public static final String BASE_URL = "https://be20-151-251-246-129.ngrok.io/";

    private String apiVersion = "v.1.0/api/";

    public NetworkApi getConnection(){
        return api;
    }

    private NetworkApi api = new Retrofit.Builder()
            .baseUrl(BASE_URL + apiVersion)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NetworkApi.class);

}
