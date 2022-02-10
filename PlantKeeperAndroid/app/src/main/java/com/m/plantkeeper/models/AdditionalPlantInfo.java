package com.m.plantkeeper.models;

public class AdditionalPlantInfo {

    private long id;

    private String title;

    private String content;

    private Plant plantId;

    public AdditionalPlantInfo() {
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

    public void setTitle(String header) {
        this.title = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Plant getPlantId() {
        return plantId;
    }

    public void setPlantId(Plant plantId) {
        this.plantId = plantId;
    }
}
