package com.m.plantkeeper.navigation;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public interface Navigation {

    void navigateToActivity(Context context, Class<?> calledActivity);

    void navigateToFragment(Fragment fragment, FragmentActivity activity, int containerId);

    void navigateToPreviousFragment(FragmentActivity activity);

    void navigateToFragment(Fragment fragment, FragmentActivity activity, int containerId,
                            Bundle bundle, String key);

}
