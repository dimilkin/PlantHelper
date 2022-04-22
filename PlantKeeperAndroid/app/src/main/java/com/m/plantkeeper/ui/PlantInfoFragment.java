package com.m.plantkeeper.ui;

import static com.m.plantkeeper.Constants.EXTRA_PLANT_INFO_BODY;
import static com.m.plantkeeper.Constants.EXTRA_PLANT_INFO_TITLE;
import static com.m.plantkeeper.Constants.EXTRA_USER_PLANT_NAME;
import static com.m.plantkeeper.Constants.PLANT_ID;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.m.plantkeeper.R;
import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.UserPlant;
import com.m.plantkeeper.navigation.Navigation;
import com.m.plantkeeper.navigation.NavigationProviderImpl;
import com.m.plantkeeper.viewmodels.PlantInfoViewModel;

import java.util.concurrent.ExecutionException;

public class PlantInfoFragment extends Fragment {

    private TextView plantInfoCommonName, plantGivenName;
    private PlantInfoViewModel viewModel;
    private Navigation navigation;
    private LinearLayout watering, repotting, temperature, origin, size,
            toxicPets, light, additionalInfo, soil, airHumidity,
            bestGrows, scientificNames, potentialProblems;

    public PlantInfoFragment() {
        // Required empty public constructor
        navigation = new NavigationProviderImpl();
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
        UserPlant userPlant = getInfoFromBundle();
        int plantId = userPlant.getId();
        try {
            Plant plantForThisFragmentInfo = getPlantFromStorageById(plantId);
            plantGivenName.setText(userPlant.getProvidedName());
            plantInfoCommonName.setText(plantForThisFragmentInfo.getCommonName());
            initializeButtons(view);
            setClickListeners(plantForThisFragmentInfo);
        } catch (ExecutionException | InterruptedException | Resources.NotFoundException e) {
            Log.e("Error", "Failed to load Plant Info to PlantInfoFragment", e);
            e.printStackTrace();
            Toast.makeText(getContext(), "Plant Info Missing", Toast.LENGTH_SHORT).show();
            plantGivenName.setText("No Info Available");
            plantInfoCommonName.setText("We Are Sorry! We Are Working On It");
        }
        return view;
    }

    private Plant getPlantFromStorageById(int plantId) throws ExecutionException, InterruptedException {
        return viewModel.getInfoForPlantbyId(plantId);
    }

    private UserPlant getInfoFromBundle() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            UserPlant userPlant = new UserPlant();
            userPlant.setId(mBundle.getInt(PLANT_ID));
            userPlant.setProvidedName(mBundle.getString(EXTRA_USER_PLANT_NAME));
            return userPlant;
        }
        throw new Resources.NotFoundException("No Plant Info Found");
    }

    private void initialiseView(View view) {
        plantInfoCommonName = view.findViewById(R.id.plantInfoCommonName);
        plantGivenName = view.findViewById(R.id.plantAssignedName);
    }

    private void initializeButtons(View view) {
        watering = view.findViewById(R.id.plantInfoWatering);
        soil = view.findViewById(R.id.plantInfoSoil);
        temperature = view.findViewById(R.id.plantInfoTemperature);
        origin = view.findViewById(R.id.plantInfoOrigin);
        size = view.findViewById(R.id.plantInfoSize);
        toxicPets = view.findViewById(R.id.plantInfoToxicPets);
        light = view.findViewById(R.id.plantInfoLight);
        additionalInfo = view.findViewById(R.id.plantInfoAdditionalInfo);
        repotting = view.findViewById(R.id.plantInfoRepotting);
        airHumidity = view.findViewById(R.id.plantInfoAirHumidity);
        bestGrows = view.findViewById(R.id.plantInfoBestGrows);
        scientificNames = view.findViewById(R.id.plantInfoScientificName);
        potentialProblems = view.findViewById(R.id.plantInfoPotentialProblems);
    }

    private void setClickListeners(Plant plant){
        watering.setOnClickListener( v -> {
            openInfoFragment("Watering", plant.getWatering());
        });
        soil.setOnClickListener( v -> {
            openInfoFragment("Soil Info", plant.getSoil());
        });
        temperature.setOnClickListener( v -> {
            openInfoFragment("Temperature", plant.getTemperature());
        });
        origin.setOnClickListener( v -> {
            openInfoFragment("Origin", plant.getOrigin());
        });
        size.setOnClickListener( v -> {
            openInfoFragment("Size", plant.getMaxGrowth());
        });
        toxicPets.setOnClickListener( v -> {
            openInfoFragment("Toxic To Pets", plant.getPoisonousForPets());
        });
        light.setOnClickListener( v -> {
            openInfoFragment("Light", plant.getLight());
        });
        repotting.setOnClickListener( v -> {
            openInfoFragment("Repotting", plant.getRePotting());
        });
        airHumidity.setOnClickListener( v -> {
            openInfoFragment("Air Humidity", plant.getAirHumidity());
        });
        bestGrows.setOnClickListener( v -> {
            openInfoFragment("Best Grows", plant.getWhereItGrowsBest());
        });
        scientificNames.setOnClickListener( v -> {
            openInfoFragment("Scientific Name", plant.getScientificName());
        });
        additionalInfo.setOnClickListener( v -> {
            openInfoFragment("Additional Info", plant.getWatering());
        });
        potentialProblems.setOnClickListener( v -> {
            openInfoFragment("Potential Problems", plant.getWatering());
        });
    }

    private void openInfoFragment(String title, String body){
        ShortInfoFragment shortInfoFragment = new ShortInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PLANT_INFO_TITLE, title);
        bundle.putString(EXTRA_PLANT_INFO_BODY, body);
        navigation.navigateToFragment(shortInfoFragment, getActivity(),R.id.plantInfoFragmentContainer, bundle);
    }
}