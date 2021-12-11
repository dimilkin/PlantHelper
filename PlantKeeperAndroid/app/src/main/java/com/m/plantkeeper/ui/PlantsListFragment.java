package com.m.plantkeeper.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.m.plantkeeper.R;
import com.m.plantkeeper.navigation.Navigation;
import com.m.plantkeeper.navigation.NavigationProviderImpl;
import com.m.plantkeeper.ui.adapters.MainPlantsListAdapter;
import com.m.plantkeeper.viewmodels.MainPlantsListViewModel;

public class PlantsListFragment extends Fragment {

    private MainPlantsListAdapter adapter;
    private RecyclerView recyclerView;
    private MainPlantsListViewModel viewModel;

    public PlantsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = new ViewModelProvider(requireActivity()).get(MainPlantsListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plants_list, container, false);


        recyclerView = view.findViewById(R.id.plantsRecyclerView);
        adapter = new MainPlantsListAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        observePlants();

        adapter.setAdapterClickListener(plantItem -> {
            navigateToPlantInfoFragment();
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.app_bar_menu, menu);
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

    private void observePlants() {
        viewModel.plantsList.observe(getViewLifecycleOwner(), plants -> {
            adapter.submitList(plants);
            adapter.notifyDataSetChanged();
        });
    }

    private void navigateToAddNewPlant() {
        Navigation navigation = new NavigationProviderImpl();
        AddEditPlantFragment addEditPlantFragment = new AddEditPlantFragment();
        navigation.navigateToFragment(addEditPlantFragment, getActivity(), R.id.mainFragmentContainer);
    }

    private void navigateToPlantInfoFragment() {
        Navigation navigation = new NavigationProviderImpl();
        PlantInfoFragment plantInfoFragment = new PlantInfoFragment();
        navigation.navigateToFragment(plantInfoFragment, getActivity(), 1, "plantInfo", R.id.mainFragmentContainer);
    }
}