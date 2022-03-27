package com.m.plantkeeper.ui;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.m.plantkeeper.R;
import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.viewmodels.AddEditPlantViewModel;
import com.m.plantkeeper.viewmodels.PlantInfoViewModel;

import java.util.concurrent.ExecutionException;

public class PlantInfoFragment extends Fragment {

    private TextView plantInfoCommonName, plantInfoScientificName, plantInfoOrigin, plantInfoMaxGrowth,
            plantInfoPoisonousForPets, plantInfoTemperature, plantInfoLighting, plantInfoSoil, plantInfoRePotting,
            plantInfoAirHumidity, plantInfoPropagation, plantInfoGrowsBest;

    private PlantInfoViewModel viewModel;
    private Plant plantForThisFragmentInfo;

    public PlantInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(PlantInfoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plant_info, container, false);

        initialiseView(view);
        int plantId = getPlantIdFromBundle();
        getPlantFromStorageById(plantId);
        setInfoToContainers(plantForThisFragmentInfo);

        return view;
    }

    private void setInfoToContainers(Plant plant){
        if (plant != null) {
            plantInfoCommonName.setText(plant.getCommonName());
            plantInfoScientificName.setText(plant.getScientificName());
            plantInfoOrigin.setText(plant.getOrigin());
            plantInfoMaxGrowth.setText(plant.getMaxGrowth());
            plantInfoPoisonousForPets.setText(plant.getPoisonousForPets());
            plantInfoTemperature.setText(plant.getTemperature());
            plantInfoLighting.setText(plant.getLight());
            plantInfoSoil.setText(plant.getSoil());
            plantInfoRePotting.setText(plant.getRePotting());
            plantInfoAirHumidity.setText(plant.getAirHumidity());
            plantInfoPropagation.setText(plant.getPropagation());
            plantInfoGrowsBest.setText(plant.getWhereItGrowsBest());
        }

    }

    private void getPlantFromStorageById(int plantId){
        try {
            plantForThisFragmentInfo = viewModel.getInfoForPlantbyId(plantId);
        } catch (ExecutionException | InterruptedException | Resources.NotFoundException e) {
            Log.e("Error", "Failed to load Plant Info to PlantInfoFragment", e);
            e.printStackTrace();
            Toast.makeText(getContext(), "Plant Info Missing", Toast.LENGTH_SHORT).show();
        }
    }

    private int getPlantIdFromBundle() {
        Bundle mBundle = getArguments();
        int plantId = -1;
        if (mBundle != null) {
            plantId = mBundle.getInt("PLANT_ID");
        }
        return plantId;
    }

    private void initialiseView(View view) {
        plantInfoCommonName = view.findViewById(R.id.plantInfoCommonName);
        plantInfoScientificName = view.findViewById(R.id.plantInfoScientificName);
        plantInfoOrigin = view.findViewById(R.id.plantInfoOrigin);
        plantInfoMaxGrowth = view.findViewById(R.id.plantInfoMaxGrowth);
        plantInfoPoisonousForPets = view.findViewById(R.id.plantInfoPoisonousForPets);
        plantInfoTemperature = view.findViewById(R.id.plantInfoTemperature);
        plantInfoLighting = view.findViewById(R.id.plantInfoLighting);
        plantInfoSoil = view.findViewById(R.id.plantInfoSoil);
        plantInfoRePotting = view.findViewById(R.id.plantInfoRePotting);
        plantInfoAirHumidity = view.findViewById(R.id.plantInfoAirHumidity);
        plantInfoPropagation = view.findViewById(R.id.plantInfoPropagation);
        plantInfoGrowsBest = view.findViewById(R.id.plantInfoGrowsBest);
    }
}