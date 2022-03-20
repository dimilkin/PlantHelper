package com.m.plantkeeper.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.m.plantkeeper.R;
import com.m.plantkeeper.models.PlantShortInfo;

import java.util.ArrayList;
import java.util.List;

public class PlantsSearchAdapter extends ListAdapter<PlantShortInfo, PlantsSearchAdapter.PlantsViewHolder> implements Filterable {
    private List<PlantShortInfo> exampleList;
    private List<PlantShortInfo> exampleListFull;
    private SearchAdapterClickListener adapterClickListener;

    public PlantsSearchAdapter() {
        super(DIFF_CALLBACK);
        exampleList = new ArrayList<>();
        exampleListFull = new ArrayList<>();
    }

    public PlantsSearchAdapter(List<PlantShortInfo> exampleList) {
        super(DIFF_CALLBACK);
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
    }

    private static final DiffUtil.ItemCallback<PlantShortInfo> DIFF_CALLBACK = new DiffUtil.ItemCallback<PlantShortInfo>() {
        @Override
        public boolean areItemsTheSame(@NonNull PlantShortInfo oldItem, @NonNull PlantShortInfo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull PlantShortInfo oldItem, @NonNull PlantShortInfo newItem) {
            return oldItem.getScientificName().equals(newItem.getScientificName());
        }
    };

        @NonNull
    @Override
    public PlantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant_search,
                parent, false);
        return new PlantsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantsViewHolder holder, int position) {
        PlantShortInfo currentItem = exampleList.get(position);
        holder.commonName.setText(currentItem.getCommonName());
        holder.scientificName.setText(currentItem.getScientificName());
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public void onCurrentListChanged(@NonNull List<PlantShortInfo> previousList, @NonNull List<PlantShortInfo> currentList) {
        super.onCurrentListChanged(previousList, currentList);
        exampleListFull.addAll(currentList);
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private final Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PlantShortInfo> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PlantShortInfo item : exampleListFull) {
                    if (item.getScientificName().toLowerCase().contains(filterPattern) ||
                            item.getCommonName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    class PlantsViewHolder extends RecyclerView.ViewHolder {
        TextView commonName, scientificName;

        PlantsViewHolder(View itemView) {
            super(itemView);
            commonName = itemView.findViewById(R.id.searchedCommonName);
            scientificName = itemView.findViewById(R.id.searchedScientificName);

            itemView.setOnClickListener(view-> {
                int position = getAdapterPosition();
                if (adapterClickListener != null && position != RecyclerView.NO_POSITION) {
                    adapterClickListener.onAdapterClick(getItem(position));
                }
            });
        }
    }

    public interface SearchAdapterClickListener {
        void onAdapterClick(PlantShortInfo plantItem);
    }

    public void setAdapterClickListener(SearchAdapterClickListener adapterClickListener){
        this.adapterClickListener = adapterClickListener;
    }
}
