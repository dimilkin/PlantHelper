package com.m.plantkeeper.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.m.plantkeeper.R;
import com.m.plantkeeper.models.Plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainPlantsListAdapter extends ListAdapter<Plant, MainPlantsListAdapter.PlantsHolder> {

    private AdapterClickListener adapterClickListener;

    public MainPlantsListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Plant> DIFF_CALLBACK = new DiffUtil.ItemCallback<Plant>() {
        @Override
        public boolean areItemsTheSame(@NonNull Plant oldItem, @NonNull Plant newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Plant oldItem, @NonNull Plant newItem) {
            return oldItem.getCommonName().equals(newItem.getCommonName());
        }
    };

    @NonNull
    @Override
    public PlantsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant, parent, false);
        return new MainPlantsListAdapter.PlantsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantsHolder holder, int position) {
        Plant plant = getItem(position);

        List<String> urls = new ArrayList<>();
        urls.add("https://assets7.lottiefiles.com/packages/lf20_hocmonst.json");
        urls.add("https://assets5.lottiefiles.com/private_files/lf30_uMKwX0.json");
        urls.add("https://assets9.lottiefiles.com/packages/lf20_Bom6gU.json");
        urls.add("https://assets1.lottiefiles.com/packages/lf20_A2CKzb.json");
        urls.add("https://assets2.lottiefiles.com/private_files/lf30_xsg73jmq.json");
        urls.add("https://assets10.lottiefiles.com/packages/lf20_valvvrfy.json");
        urls.add("https://assets3.lottiefiles.com/private_files/lf30_xjwmnq0o.json");

        Random random= new Random();
        int index = random.nextInt(urls.size());
        holder.animationView.setAnimationFromUrl(urls.get(index));
        holder.plantName.setText(plant.getCommonName());
        holder.plantWaterTime.setText(plant.getWatering());
    }

    class PlantsHolder extends RecyclerView.ViewHolder {

        private TextView plantName, plantWaterTime;
        private LottieAnimationView animationView;

        public PlantsHolder(@NonNull View itemView) {
            super(itemView);

            animationView = itemView.findViewById(R.id.itemAnimationView);
            plantName = itemView.findViewById(R.id.itemPlantName);
            plantWaterTime = itemView.findViewById(R.id.itemPlantWaterTime);

            itemView.setOnClickListener(view-> {
                int position = getAdapterPosition();
                if (adapterClickListener != null && position != RecyclerView.NO_POSITION) {
                    adapterClickListener.onAdapterClick(getItem(position));
                }
            });

        }
    }

    public interface AdapterClickListener {
        void onAdapterClick(Plant plantItem);
    }

    public void setAdapterClickListener(AdapterClickListener adapterClickListener){
        this.adapterClickListener = adapterClickListener;
    }
}
