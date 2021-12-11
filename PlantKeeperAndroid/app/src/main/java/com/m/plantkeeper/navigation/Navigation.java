package com.m.plantkeeper.navigation;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public interface Navigation {

    void navigateToActivity(Context context, Class<?> calledActivity);

    void navigateToFragment(Fragment fragment, FragmentActivity activity, int containerId);

    void navigateToFragment(Fragment fragment, FragmentActivity activity, int bundleInfoId,
                            String key, int containerId);

}
