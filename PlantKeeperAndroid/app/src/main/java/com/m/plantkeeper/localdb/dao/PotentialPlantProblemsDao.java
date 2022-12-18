package com.m.plantkeeper.localdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.m.plantkeeper.models.AdditionalPlantInfo;
import com.m.plantkeeper.models.PotentialPlantProblems;

import java.util.List;

@Dao
public interface PotentialPlantProblemsDao {

    @Insert
    void insert(PotentialPlantProblems plantProblem);

    @Update
    void update(PotentialPlantProblems plantProblem);

    @Delete
    void delete(PotentialPlantProblems plantProblem);

    @Query("SELECT * FROM plants_problems WHERE plantId =:providedPlantId")
    List<PotentialPlantProblems> getPlantProblemsForSpecificPlant(int providedPlantId);
}
