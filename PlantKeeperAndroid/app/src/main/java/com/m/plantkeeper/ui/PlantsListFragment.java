package com.m.plantkeeper.ui;

import static com.m.plantkeeper.Constants.EXTRA_USER_PLANT_ID;
import static com.m.plantkeeper.Constants.EXTRA_USER_PLANT_NAME;
import static com.m.plantkeeper.Constants.EXTRA_PLANT_ID;
import static com.m.plantkeeper.Constants.UPDATE_USER_PLANT;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.m.plantkeeper.R;
import com.m.plantkeeper.models.AuthCredentials;
import com.m.plantkeeper.models.UserPlant;
import com.m.plantkeeper.navigation.Navigation;
import com.m.plantkeeper.navigation.NavigationProviderImpl;
import com.m.plantkeeper.services.AuthService;
import com.m.plantkeeper.services.impl.AuthServiceImpl;
import com.m.plantkeeper.ui.adapters.MainPlantsListAdapter;
import com.m.plantkeeper.viewmodels.MainPlantsListViewModel;

import java.util.concurrent.ExecutionException;

public class PlantsListFragment extends Fragment {

    private MainPlantsListAdapter adapter;
    private RecyclerView recyclerView;
    private MainPlantsListViewModel viewModel;
    private AuthService authService;

    public PlantsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = new ViewModelProvider(requireActivity()).get(MainPlantsListViewModel.class);
        authService = AuthServiceImpl.getAuthInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plants_list, container, false);

        AuthCredentials authCredentials = authService.getAuthCredentials();
        String authtoken = authCredentials.getUserToken();
        int userId = authCredentials.getUserId();

        try {
            viewModel.initializeUserPlants(authtoken, userId);
        } catch (ExecutionException | InterruptedException exception) {
            Log.e("ERROR : ", "Data is missing", exception);
            exception.printStackTrace();
        }
        initializeView(view);

        attachItemTouchHelper(authtoken, userId);

        adapter.setAdapterClickListener(this::navigateToPlantInfoFragment);
        adapter.setAdapterLongClickListener(this::navigateToEdditPlant);
        return view;
    }

    private void attachItemTouchHelper(String authToken, int userId) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                UserPlant userPlant = adapter.getUserPlantAt(position);
//                alarmProvider.cancellAllAlarm(userPlant);
                viewModel.deleteUserPlant(authToken, userPlant);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Plant Deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        observePlants();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.app_bar_menu, menu);
    }

    private void observePlants() {
        viewModel.getUserPlantsList().observe(getViewLifecycleOwner(), plants -> {
            adapter.submitList(plants);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionAddPlant: {
                navigateToAddNewPlant();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToAddNewPlant() {
        Navigation navigation = new NavigationProviderImpl();
        AddEditPlantFragment addEditPlantFragment = new AddEditPlantFragment();
        navigation.navigateToFragment(addEditPlantFragment, getActivity(), R.id.mainFragmentContainer);
    }

    private void navigateToEdditPlant(UserPlant userPlant) {
        Navigation navigation = new NavigationProviderImpl();
        AddEditPlantFragment addEditPlantFragment = new AddEditPlantFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_PLANT_ID, userPlant.getPlantId());
        bundle.putString(EXTRA_USER_PLANT_NAME, userPlant.getProvidedName());
        bundle.putInt(EXTRA_USER_PLANT_ID, userPlant.getId());
        getParentFragmentManager().setFragmentResult(UPDATE_USER_PLANT, bundle);
        navigation.navigateToFragment(addEditPlantFragment, getActivity(), R.id.mainFragmentContainer, bundle);
    }

    private void navigateToPlantInfoFragment(UserPlant userPlant) {
        Navigation navigation = new NavigationProviderImpl();
        PlantInfoFragment plantInfoFragment = new PlantInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_PLANT_ID, userPlant.getPlantId());
        bundle.putString(EXTRA_USER_PLANT_NAME, userPlant.getProvidedName());
        navigation.navigateToFragment(plantInfoFragment, getActivity(), R.id.mainFragmentContainer, bundle);
    }

    private void initializeView(View view){
        recyclerView = view.findViewById(R.id.plantsRecyclerView);
        adapter = new MainPlantsListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        observePlants();
    }
}