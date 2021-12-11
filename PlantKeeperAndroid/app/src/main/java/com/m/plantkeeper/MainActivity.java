package com.m.plantkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.m.plantkeeper.ui.PlantsListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.mainFragmentContainer, PlantsListFragment.class, null)
                .commit();
    }
}