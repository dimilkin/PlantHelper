package com.m.plantkeeper.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
    public void navigateToFragment(Fragment fragment, FragmentActivity activity, int bundleInfoId,
                                   String key, int containerId) {
        Bundle bundle = new Bundle();
        bundle.putInt(key, bundleInfoId);
        fragment.setArguments(bundle);
        navigateToFragment(fragment, activity, containerId);
    }

    private void navigateToChildFragment(FragmentActivity activity){
//        FragmentManager cfManager = activity.getChildFragmentManager();
//        FragmentTransaction fragmentTransaction = cfManager.beginTransaction();
//        fragmentTransaction.replace(R.id.nestedFrameLayout, addEditTransactionFragment);
//        fragmentTransaction.addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragmentTransaction.commit();
    }
}