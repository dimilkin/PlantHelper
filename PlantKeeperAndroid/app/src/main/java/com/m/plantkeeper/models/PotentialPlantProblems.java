package com.m.plantkeeper.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "plants_problems")
public class PotentialPlantProblems implements CombinedPlantInfo {

    @PrimaryKey(autoGenerate = true)
    private int databaseKey;
    private long id;
    private String title;
    private String content;
    private int plantId;

    public PotentialPlantProblems() {
    }

    public int getDatabaseKey() {
        return databaseKey;
    }

    public void setDatabaseKey(int databaseKey) {
        this.databaseKey = databaseKey;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPlantId() {
        return plantId;
    }

    public void setPlantId(int plantId) {
        this.plantId = plantId;
    }

    @Override
    public String getInfoTitle() {
        return title;
    }

    @Override
    public String getInfoContent() {
        return content;
    }
}
