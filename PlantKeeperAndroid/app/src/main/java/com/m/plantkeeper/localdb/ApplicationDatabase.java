package com.m.plantkeeper.localdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.m.plantkeeper.localdb.dao.PlantDao;
import com.m.plantkeeper.localdb.dao.UserPlantDao;
import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.UserPlant;

@Database(entities = {Plant.class, UserPlant.class}, version = 1)
public abstract class ApplicationDatabase extends RoomDatabase {


    public static ApplicationDatabase instance;

    public abstract PlantDao plantDao();
    public abstract UserPlantDao userPlantDao();

    public static synchronized ApplicationDatabase getInstance(Context cotext) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(cotext.getApplicationContext(), ApplicationDatabase.class, "plants_database")
                    .build();
        }
        return instance;
    }
}

