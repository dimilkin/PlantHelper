package com.m.plantkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.m.plantkeeper.ui.PlantsListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = this.getSharedPreferences("UserAuthInfo", Context.MODE_PRIVATE);
        String authToken = "Bearer " + prefs.getString("AUTHTOKEN", "AuthToken missing");
        int userId = prefs.getInt("USERID", -1);

        Bundle bundle = new Bundle();
        bundle.putString("AUTHTOKEN", authToken);
        bundle.putInt("USERID", userId);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.mainFragmentContainer, PlantsListFragment.class, bundle)
                .commit();
    }
}