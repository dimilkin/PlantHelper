package com.m.plantkeeper.localdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.UserPlant;

import java.util.List;

@Dao
public interface UserPlantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserPlant userPlant);

    @Update
    void update(UserPlant userPlant);

    @Delete
    void delete(UserPlant userPlant);

    @Query("SELECT * FROM user_plants ORDER BY id DESC")
    LiveData<List<UserPlant>> getAllUserPlantsLiveData();

    @Query("SELECT * FROM user_plants WHERE userOwnerId =:userId ORDER BY id DESC")
    List<UserPlant> getAllUserPlantsByUserId(int userId);

    @Query("SELECT * FROM user_plants WHERE id = :userPlantId")
    UserPlant getPlantById(int userPlantId);
}
