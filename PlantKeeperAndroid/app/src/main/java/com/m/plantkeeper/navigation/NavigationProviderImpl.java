package com.m.plantkeeper.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NavigationProviderImpl implements Navigation {

    @Override
    public void navigateToActivity(Context context, Class<?> calledActivity) {
        Intent intent = new Intent(context, calledActivity);
        context.startActivity(intent);
    }

    @Override
    public void navigateToFragment(Fragment fragment, FragmentActivity activity, int fragmentContainerId) {

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentContainerId, fragment);
        fragmentTransaction.addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public void navigateToPreviousFragment(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Log.i(activity.toString(), "popping backstack");
            fragmentManager.popBackStack();
        } else {
            Log.i(activity.toString(), "nothing on backstack, calling super");
        }
    }

    @Override
    public void navigateToFragment(Fragment fragment, FragmentActivity activity, int containerId, Bundle bundle, String key) {
        fragment.setArguments(bundle);
        navigateToFragment(fragment, activity, containerId);
    }
}