package com.m.plantkeeper.notifications.local;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.m.plantkeeper.models.UserPlant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmProvider alarmProvider = new AlarmProvider(context);
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            restartAlarms();
        }
    }

    private void restartAlarms() {

        List<UserPlant> allChildrenList = new ArrayList<UserPlant>();
//        try {
//            allChildrenList = localRepo.reloadChildAlarmInfo();
//        } catch (ExecutionException | InterruptedException e) {
//            Log.e("Failure", "Failed to restart alarms on reboot", e);
//        }
//        for (Child child : allChildrenList) {
//            provider.setAllAlarmsOnChild(child);
//        }
    }
}