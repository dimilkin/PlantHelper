package com.m.plantkeeper.network;

import com.m.plantkeeper.models.AuthResponse;
import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.PlantShortInfo;
import com.m.plantkeeper.models.RegistrationInfo;
import com.m.plantkeeper.models.UserPlant;
import com.m.plantkeeper.models.dtos.AccountActivationDto;
import com.m.plantkeeper.models.dtos.PlantResponseBody;
import com.m.plantkeeper.models.dtos.PlantsInfoDto;
import com.m.plantkeeper.models.dtos.UserInfoDto;

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

    @POST("user/activation")
    Call<ResponseBody> activateNewUser(
            @Body AccountActivationDto accountActivationDt
    );

    @GET("user/profile/{userId}")
    Single<UserInfoDto> getInfoForUser(
            @Header("Authorization") String token,
            @Path("userId") int userId
    );

    // -------------------------------------------------

    @GET("plants/{plantId}")
    Single<PlantsInfoDto> getInfoForPlant(
            @Header("Authorization") String token,
            @Path("plantId") int plantId
    );

    @GET("plants/allPlants")
    Single<List<PlantShortInfo>> getPlantsNamesAndIds(
            @Header("Authorization") String token
    );

    // -------------------------------------------------

    @POST("userplants/{hostUserId}/{plantId}")
    Call<UserPlant> addPlantToUserProfile(
            @Header("Authorization") String token,
            @Path("hostUserId") int hostUserId,
            @Path("plantId") int plantId,
            @Body UserPlant userPlantDto
    );

    @PUT("userplants/{hostUserId}/{plantId}")
    Call<UserPlant> updateUserPlant(
            @Header("Authorization") String token,
            @Path("hostUserId") int hostUserId,
            @Path("plantId") int plantId,
            @Body UserPlant userPlantDto
    );

    @DELETE("userplants/{hostUserId}/{userPlantId}")
    Call<PlantResponseBody> removePlantFromUserProfile(
            @Header("Authorization") String token,
            @Path("hostUserId") int hostUserId,
            @Path("userPlantId") int userPlantId
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
