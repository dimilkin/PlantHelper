package com.m.plantkeeper.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.m.plantkeeper.R;
import com.m.plantkeeper.models.CombinedPlantInfo;

import java.util.List;

public class PlantsLongInfoAdapter<T extends CombinedPlantInfo> extends RecyclerView.Adapter<PlantsLongInfoAdapter.ViewHolder> {

    private List<T> localDataSet;

    public PlantsLongInfoAdapter(List<T> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public PlantsLongInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant_search, parent, false);
        return new PlantsLongInfoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantsLongInfoAdapter.ViewHolder holder, int position) {
        holder.title.setText(localDataSet.get(position).getInfoTitle());
        holder.mainContent.setText(localDataSet.get(position).getInfoContent());
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title, mainContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.searchedCommonName);
            mainContent = itemView.findViewById(R.id.searchedScientificName);

        }
    }
}
