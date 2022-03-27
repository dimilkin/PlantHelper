package com.m.plantkeeper.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.m.plantkeeper.models.dtos.UserPlantDto;
import com.m.plantkeeper.navigation.Navigation;
import com.m.plantkeeper.navigation.NavigationProviderImpl;
import com.m.plantkeeper.services.PlantsInfoService;
import com.m.plantkeeper.services.UserPlantsService;
import com.m.plantkeeper.services.impl.PlantsInfoServiceImpl;
import com.m.plantkeeper.services.impl.UserPlantsServiceImpl;
import com.m.plantkeeper.viewmodels.AddEditPlantViewModel;
import com.m.plantkeeper.viewmodels.MainPlantsListViewModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditPlantFragment extends Fragment {

    private Button choosePlantTypeButton, setDaysButton, setWeeksButton, savePlantButton;
    private EditText setPlantNameTextField;
    private TextView plantTypeTextView;
    private NumberPicker intervalNumberPicker;
    private Navigation navigation;
    private AddEditPlantViewModel viewModel;

    private boolean weeksChosen = false;
    private String providedName;
    private String plantName;
    private int plantId;


    public AddEditPlantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigation = new NavigationProviderImpl();
        viewModel = new ViewModelProvider(requireActivity()).get(AddEditPlantViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_edit_plant, container, false);
        initialiseView(view);

        getParentFragmentManager().setFragmentResultListener("PLANT_CHOICE", this, (requestKey, bundle) -> {
            plantName = bundle.getString("PLANT_SCI_NAME");
            plantId = bundle.getInt("PLANT_ID");
            plantTypeTextView.setText(plantName);
        });

        choosePlantTypeButton.setClickable(false);
        choosePlantTypeButton.setOnClickListener(click -> openSearchFragment());

        setUpNumberPicker();
        final int[] timeInterval = new int[1];
        intervalNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> timeInterval[0] = numberPicker.getValue());

        int userId = -1;
        String authToken = "";

        SharedPreferences prefs = getActivity().getSharedPreferences("UserAuthInfo", Context.MODE_PRIVATE);
        if (prefs != null) {
            authToken = "Bearer " + prefs.getString("AUTHTOKEN", "AuthToken missing");
            userId = prefs.getInt("USERID", -1);
        }

        setDaysButton.setOnClickListener(btn -> daysSetButtonClicked());
        setWeeksButton.setOnClickListener(btn -> weeksSetButtonClicked());

        final String finalAuthToken = authToken;

        final int finalUserId = userId;
        savePlantButton.setOnClickListener(btn -> {
            final int finalPlantId = plantId;
            final int waterPeriod = timeInterval[0];
            saveNewUserPlant(waterPeriod, finalUserId, finalPlantId, finalAuthToken);
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        providedName = setPlantNameTextField.getText().toString();
    }

    private void initialiseView(View view){
        choosePlantTypeButton = view.findViewById(R.id.choosePlantTypeButton);
        plantTypeTextView = view.findViewById(R.id.chosenPlantTypeText);
        setPlantNameTextField = view.findViewById(R.id.plantNameEditText);
        setDaysButton = view.findViewById(R.id.setDaysButton);
        setWeeksButton = view.findViewById(R.id.setWeeksButton);
        savePlantButton = view.findViewById(R.id.savePlantButton);
        intervalNumberPicker = view.findViewById(R.id.periodNumbersPicker);
    }

    private void setUpNumberPicker(){
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

    private void saveNewUserPlant(int waterInterval, int userid, int plantId, String authToken) {
        UserPlantDto userPlantDto = new UserPlantDto();
        if (weeksChosen) {
            waterInterval = waterInterval * 7;
        }
        if (providedName == null) {
            providedName = setPlantNameTextField.getText().toString();
        }
        userPlantDto.setWaterPeriod(waterInterval);
        userPlantDto.setProvidedName(providedName);

        viewModel.createNewUserPlant(authToken, userid, plantId, userPlantDto).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                viewModel.savePlantDataFromServerToLocalStoarage(authToken, plantId);
                navigation.navigateToPreviousFragment(getActivity());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to save plant", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
    }
}