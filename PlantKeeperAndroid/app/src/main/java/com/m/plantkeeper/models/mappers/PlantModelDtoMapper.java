package com.m.plantkeeper.models.mappers;

import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.dtos.PlantsInfoDto;

public class PlantModelDtoMapper {
    public static Plant mapDtoToPlantModel(PlantsInfoDto plantsInfoDto){
        Plant plant = new Plant();
        plant.setId(plantsInfoDto.getPlantId());
        plant.setOrigin(plantsInfoDto.getOrigin());
        plant.setCommonName(plantsInfoDto.getCommonName());
        plant.setScientificName(plantsInfoDto.getScientificName());
        plant.setMaxGrowth(plantsInfoDto.getMaxGrowth());
        plant.setPoisonousForPets(plantsInfoDto.getPoisonousForPets());
        plant.setTemperature(plantsInfoDto.getTemperature());
        plant.setLight(plantsInfoDto.getLight());
        plant.setWatering(plantsInfoDto.getWatering());
        plant.setSoil(plantsInfoDto.getSoil());
        plant.setRePotting(plantsInfoDto.getRePotting());
        plant.setAirHumidity(plantsInfoDto.getAirHumidity());
        plant.setPropagation(plantsInfoDto.getPropagation());
        plant.setWhereItGrowsBest(plantsInfoDto.getWhereItGrowsBest());
        return plant;
    }
}
