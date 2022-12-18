package com.m.plantkeeper.ui;

import static com.m.plantkeeper.Constants.EXTRA_PLANT_ID;
import static com.m.plantkeeper.Constants.INFO_TYPE_ADDITIONAL;
import static com.m.plantkeeper.Constants.INFO_TYPE_PROBLEMS;
import static com.m.plantkeeper.Constants.PLANT_INFO_TYPE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.m.plantkeeper.R;
import com.m.plantkeeper.models.AdditionalPlantInfo;
import com.m.plantkeeper.models.CombinedPlantInfo;
import com.m.plantkeeper.models.PotentialPlantProblems;
import com.m.plantkeeper.ui.adapters.PlantsLongInfoAdapter;
import com.m.plantkeeper.viewmodels.PlantInfoViewModel;

import java.util.List;

public class LongInfoFragment extends Fragment {

    private PlantInfoViewModel viewModel;
    private PlantsLongInfoAdapter adapter;
    private Button closeButton;
    private RecyclerView recyclerView;
    private TextView title;

    public LongInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_long_info, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(PlantInfoViewModel.class);
        initializeView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int plantId = bundle.getInt(EXTRA_PLANT_ID);
            String infoTypeToBeDisplayed = bundle.getString(PLANT_INFO_TYPE);
            if (infoTypeToBeDisplayed.equals(INFO_TYPE_ADDITIONAL)) {
                List<AdditionalPlantInfo> additionalPlantInfoList = viewModel.getAdditionalInfoForPlantById(plantId);
                loadDatafroPlant(additionalPlantInfoList);
                title.setText("More Info");
            }
            if (infoTypeToBeDisplayed.equals(INFO_TYPE_PROBLEMS)) {
                List<PotentialPlantProblems> potentialPlantProblemsList = viewModel.getPotentialProblemsForPlantById(plantId);
                loadDatafroPlant(potentialPlantProblemsList);
                title.setText("Potential Problems");
            }
        }

        closeButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void initializeView(View view) {
        closeButton = view.findViewById(R.id.closeLongInfoFragmentButton);
        recyclerView = view.findViewById(R.id.plantsLongInfoRecyclerView);
        title = view.findViewById(R.id.commonInfoTitle);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(adapter);

    }

    private <T extends CombinedPlantInfo> void loadDatafroPlant(List<T> info) {
        adapter = new PlantsLongInfoAdapter(info);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}