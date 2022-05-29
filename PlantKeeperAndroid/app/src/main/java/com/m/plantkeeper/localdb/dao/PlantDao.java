package com.m.plantkeeper.localdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.m.plantkeeper.models.Plant;

import java.util.List;

@Dao
public interface PlantDao {

    @Insert
    void insert(Plant plant);

    @Update
    void update(Plant plant);

    @Delete
    void delete(Plant plant);

    @Query("SELECT * FROM plants_info ORDER BY id DESC")
    LiveData<List<Plant>> getAllPlants();

    @Query("SELECT * FROM plants_info ORDER BY id DESC")
    List<Plant> reloadPlantsAlarmInfo();

    @Query("SELECT * FROM plants_info WHERE id = :plantId")
    Plant getPlantById(int plantId);
}
