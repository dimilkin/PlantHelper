package com.m.plantkeeper.ui;

import static com.m.plantkeeper.Constants.EXTRA_PLANT_CHOICE;
import static com.m.plantkeeper.Constants.EXTRA_PLANT_ID;
import static com.m.plantkeeper.Constants.EXTRA_PLANT_SCI_NAME;
import static com.m.plantkeeper.Constants.EXTRA_USER_PLANT_ID;
import static com.m.plantkeeper.Constants.EXTRA_USER_PLANT_NAME;
import static com.m.plantkeeper.Constants.UPDATE_USER_PLANT;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.m.plantkeeper.R;
import com.m.plantkeeper.models.Plant;
import com.m.plantkeeper.models.UserPlant;
import com.m.plantkeeper.navigation.Navigation;
import com.m.plantkeeper.navigation.NavigationProviderImpl;
import com.m.plantkeeper.services.AuthService;
import com.m.plantkeeper.services.impl.AuthServiceImpl;
import com.m.plantkeeper.viewmodels.AddEditPlantViewModel;
import com.m.plantkeeper.viewmodels.PlantInfoViewModel;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditPlantFragment extends Fragment {

    private Button choosePlantTypeButton, setDaysButton, setWeeksButton, savePlantButton;
    private EditText setPlantNameTextField;
    private TextView plantTypeTextView;
    private NumberPicker intervalNumberPicker;
    private Navigation navigation;
    private AddEditPlantViewModel addEditPlantViewModel;
    private PlantInfoViewModel plantInfoViewModel;
    private AuthService authService;

    private boolean weeksChosen = false;
    private boolean isInUpdateMode = false;
    private String providedName;
    private String plantName;
    private int plantId, userPlantId;


    public AddEditPlantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigation = new NavigationProviderImpl();
        addEditPlantViewModel = new ViewModelProvider(requireActivity()).get(AddEditPlantViewModel.class);
        plantInfoViewModel = new ViewModelProvider(requireActivity()).get(PlantInfoViewModel.class);
        authService = AuthServiceImpl.getAuthInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_edit_plant, container, false);
        initialiseView(view);

        final String finalAuthToken = authService.getAuthCredentials().getUserToken();
        final int finalUserId = authService.getAuthCredentials().getUserId();

        getParentFragmentManager().setFragmentResultListener(EXTRA_PLANT_CHOICE, this, (requestKey, bundle) -> {
            plantName = bundle.getString(EXTRA_PLANT_SCI_NAME);
            plantId = bundle.getInt(EXTRA_PLANT_ID);
            plantTypeTextView.setText(plantName);
        });

        getParentFragmentManager().setFragmentResultListener(UPDATE_USER_PLANT, this, (requestKey, bundle) -> {
            try {
                setPlantNameTextField.setText(bundle.getString(EXTRA_USER_PLANT_NAME));
                plantId = bundle.getInt(EXTRA_PLANT_ID);
                userPlantId = bundle.getInt(EXTRA_USER_PLANT_ID);
                Plant plant = getPlantFromStorageById(plantId);
                plantTypeTextView.setText(plant.getScientificName());
                isInUpdateMode = true;
            } catch (ExecutionException | InterruptedException | Resources.NotFoundException exception) {
                Log.e("Error", "Failed to load Plant Info to PlantInfoFragment", exception);
                exception.printStackTrace();
                Toast.makeText(getContext(), "Plant Info Missing", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }
        });


        choosePlantTypeButton.setClickable(false);
        choosePlantTypeButton.setOnClickListener(click -> openSearchFragment());

        setUpNumberPicker();
        final int[] timeInterval = new int[1];
        intervalNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> timeInterval[0] = numberPicker.getValue());

        setDaysButton.setOnClickListener(btn -> daysSetButtonClicked());
        setWeeksButton.setOnClickListener(btn -> weeksSetButtonClicked());

        savePlantButton.setOnClickListener(btn -> {
            final int finalPlantId = plantId;
            final int waterPeriod = timeInterval[0];
            if (isInUpdateMode) {
                updateUserPlant(waterPeriod, finalUserId, finalPlantId, finalAuthToken);
                return;
            }
            saveNewUserPlant(waterPeriod, finalUserId, finalPlantId, finalAuthToken);
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        providedName = setPlantNameTextField.getText().toString();
    }

    private Plant getPlantFromStorageById(int plantId) throws ExecutionException, InterruptedException {
        return plantInfoViewModel.getInfoForPlantbyId(plantId);
    }

    private void initialiseView(View view) {
        choosePlantTypeButton = view.findViewById(R.id.choosePlantTypeButton);
        plantTypeTextView = view.findViewById(R.id.chosenPlantTypeText);
        setPlantNameTextField = view.findViewById(R.id.plantNameEditText);
        setDaysButton = view.findViewById(R.id.setDaysButton);
        setWeeksButton = view.findViewById(R.id.setWeeksButton);
        savePlantButton = view.findViewById(R.id.savePlantButton);
        intervalNumberPicker = view.findViewById(R.id.periodNumbersPicker);
    }

    private void setUpNumberPicker() {
        intervalNumberPicker.setMinValue(1);
        intervalNumberPicker.setMaxValue(30);
        intervalNumberPicker.setWrapSelectorWheel(true);
    }

    private void daysSetButtonClicked() {
        setWeeksButton.setFocusableInTouchMode(true);
        setDaysButton.setFocusableInTouchMode(true);
        setWeeksButton.clearFocus();
        setDaysButton.requestFocus();
        weeksChosen = false;
    }

    private void weeksSetButtonClicked() {
        setWeeksButton.setFocusableInTouchMode(true);
        setDaysButton.setFocusableInTouchMode(true);
        setDaysButton.clearFocus();
        setWeeksButton.requestFocus();
        weeksChosen = true;
    }

    private void openSearchFragment() {
        navigation.navigateToFragment(new PlantNameSearchFragment(), getActivity(), R.id.mainFragmentContainer);
        choosePlantTypeButton.setClickable(false);
    }

    private void updateUserPlant(int waterInterval, int userid, int plantId, String authToken) {
        UserPlant userPlantDto = generateuserPlantDto(waterInterval);
        userPlantDto.setId(userPlantId);
        callBackEnd(addEditPlantViewModel.updateUserPlant(authToken, userid, plantId, userPlantDto), authToken, userPlantDto);
        Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
    }

    private void saveNewUserPlant(int waterInterval, int userid, int plantId, String authToken) {
        UserPlant userPlantDto = generateuserPlantDto(waterInterval);
        userPlantDto.setUserOwnerId(userid);
        userPlantDto.setPlantId(plantId);
        callBackEnd(addEditPlantViewModel.createNewUserPlant(authToken, userid, plantId, userPlantDto), authToken, userPlantDto);
        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
    }

    private UserPlant generateuserPlantDto (int waterInterval){
        UserPlant userPlantDto = new UserPlant();
        if (weeksChosen) {
            waterInterval = waterInterval * 7;
        }
        if (providedName == null) {
            providedName = setPlantNameTextField.getText().toString();
        }
        userPlantDto.setWaterPeriod(waterInterval);
        userPlantDto.setProvidedName(providedName);
        return userPlantDto;
    }

    private void callBackEnd(Call<UserPlant> userPlantCall, String authToken, UserPlant userPlantDto ){
        userPlantCall.enqueue(new Callback<UserPlant>() {
            @Override
            public void onResponse(Call<UserPlant> call, Response<UserPlant> responsePlant) {
                plantInfoViewModel.savePlantDataFromServerToLocalStoarage(authToken, plantId);
                addEditPlantViewModel.startNewAlarm(providedName, userPlantDto.getWaterPeriod(), plantId);
                userPlantDto.setId(responsePlant.body().getId());
                addEditPlantViewModel.saveUserPlantToLocalStorage(userPlantDto);
                navigation.navigateToPreviousFragment(getActivity());
            }

            @Override
            public void onFailure(Call<UserPlant> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to save plant", Toast.LENGTH_SHORT).show();
            }
        });
    }
}