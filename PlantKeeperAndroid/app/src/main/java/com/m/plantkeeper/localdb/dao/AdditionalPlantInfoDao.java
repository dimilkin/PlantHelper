package com.m.plantkeeper.localdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.m.plantkeeper.models.AdditionalPlantInfo;

import java.util.List;

@Dao
public interface AdditionalPlantInfoDao {
    @Insert
    void insert(AdditionalPlantInfo additionalPlantInfo);

    @Update
    void update(AdditionalPlantInfo additionalPlantInfo);

    @Delete
    void delete(AdditionalPlantInfo additionalPlantInfo);

    @Query("SELECT * FROM plants_additional_info WHERE plantId =:providedPlantId ORDER BY id DESC")
    List<AdditionalPlantInfo> getAdditinalInfoForSpecificPlant(int providedPlantId);
}
