package com.m.plantkeeper.network;

import com.m.plantkeeper.models.AuthResponse;
import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.RegistrationInfo;
import com.m.plantkeeper.models.dtos.PlantsInfoDto;
import com.m.plantkeeper.models.dtos.UserPlantDto;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NetworkApi {

    @GET("user/authentication")
    Call<Integer> authenticate(
            @Header("userMail") String userEmail,
            @Header("userPass") String userPassword
    );

    @POST("user/registration")
    Single<ResponseBody> registerNewUser(
            @Body RegistrationInfo registrationInfo
    );

    @GET("user/profile/{userId}")
    Single<AuthResponse> getInfoForUser(
            @Header("Authorization") String token,
            @Path("userId") int userId
    );

    // -------------------------------------------------

    @GET("plants/{plantId}")
    Single<Plant> getInfoForPlant(
            @Header("Authorization") String token,
            @Path("plantId") int plantId
    );

    @GET("plants/allPlants")
    Single<List<PlantsInfoDto>> getPlantsNamesAndIds(
            @Header("Authorization") String token
    );

    // -------------------------------------------------

    @POST("userplants/{hostUserId}/{plantId}")
    Single<AuthResponse> addPlantToUserProfile(
            @Header("Authorization") String token,
            @Path("hostUserId") int hostUserId,
            @Path("plantId") int plantId,
            @Body UserPlantDto userPlantDto
    );

    @DELETE("userplants/{hostUserId}/{plantId}")
    Single<AuthResponse> removePlantFromUserProfile(
            @Header("Authorization") String token,
            @Path("hostUserId") int hostUserId,
            @Path("plantId") int plantId
    );

    @POST("userplants/{hostUserId}/{plantId}/{recepientUserId}")
    Single<AuthResponse> assignOwnPlantToAnotherUser(
            @Header("Authorization") String token,
            @Path("hostUserId") int hostUserId,
            @Path("plantId") int plantId,
            @Path("recepientUserId") int recepientUserId
    );

    @PUT("userplants/{hostUserId}/{plantId}/{recepientUserId}")
    Single<AuthResponse> unAssignOwnPlantFromAnotherUser(
            @Header("Authorization") String token,
            @Path("hostUserId") int hostUserId,
            @Path("plantId") int plantId,
            @Path("recepientUserId") int recepientUserId
    );
}
