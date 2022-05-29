package com.m.plantkeeper.ui;

import static com.m.plantkeeper.Constants.EXTRA_PLANT_CHOICE;
import static com.m.plantkeeper.Constants.EXTRA_PLANT_ID;
import static com.m.plantkeeper.Constants.EXTRA_PLANT_SCI_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.m.plantkeeper.R;
import com.m.plantkeeper.models.PlantShortInfo;
import com.m.plantkeeper.navigation.Navigation;
import com.m.plantkeeper.navigation.NavigationProviderImpl;
import com.m.plantkeeper.ui.adapters.PlantsSearchAdapter;
import com.m.plantkeeper.viewmodels.PlantsSearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlantNameSearchFragment extends Fragment {

    private PlantsSearchAdapter adapter;
    private RecyclerView recyclerView;
    private PlantsSearchViewModel viewModel;
    private SearchView searchView;
    private List<PlantShortInfo> plants;
    private Navigation navigation;

    public PlantNameSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plants = new ArrayList<>();
        navigation = new NavigationProviderImpl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_name_search, container, false);
        searchView = view.findViewById(R.id.searchPlantSearchView);
        recyclerView = view.findViewById(R.id.plantSearchRecyclerView);
        viewModel = new ViewModelProvider(requireActivity()).get(PlantsSearchViewModel.class);

        SharedPreferences prefs = getActivity().getSharedPreferences("UserAuthInfo", Context.MODE_PRIVATE);
        String authToken = "Bearer " + prefs.getString("AUTHTOKEN", "AuthToken missing");
        adapter = new PlantsSearchAdapter(plants);
        if (plants.isEmpty()) {
            viewModel.getShortInfoForPlants(authToken);
            loadPlantsShortInfo();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        adapter.setAdapterClickListener(this::sendInfoBackToAddEditFragment);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return view;
    }

    // TODO -> this logic can be improved by moving the check logic from the viw model to this field
    //  - so as to call viewModel.getShortInfoForPlants(authToken); only if the mutable live data is empty

    private void loadPlantsShortInfo() {
        viewModel.getPlantsInfoList().observe(getViewLifecycleOwner(), plantShortInfoList -> {
                plants.addAll(plantShortInfoList);
                adapter.submitList(plants);
                adapter.notifyDataSetChanged();
        });
    }

    private void sendInfoBackToAddEditFragment(PlantShortInfo plantShortInfo) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PLANT_SCI_NAME, plantShortInfo.getScientificName());
        bundle.putInt(EXTRA_PLANT_ID, plantShortInfo.getId());
        getParentFragmentManager().setFragmentResult(EXTRA_PLANT_CHOICE, bundle);
        navigation.navigateToPreviousFragment(getActivity());
    }
}
