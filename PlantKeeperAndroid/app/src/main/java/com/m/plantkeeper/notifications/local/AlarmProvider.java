package com.m.plantkeeper.notifications.local;

import static com.m.plantkeeper.Constants.EXTRA_USER_PLANT_NAME;
import static com.m.plantkeeper.Constants.EXTRA_WATER_PERIOD;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.widget.Toast;

public class AlarmProvider extends ContextWrapper {

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    public AlarmProvider(Context base) {
        super(base);
    }

    public void cancelAlarm(int intentID) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, intentID, intent, PendingIntent.FLAG_ONE_SHOT + PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public void startAlarm(String name, int waterPeriodDays, int intentID) {
        Intent intent = new Intent(getApplicationContext(), NotificationReciver.class);
        intent.putExtra(EXTRA_USER_PLANT_NAME, name);
        intent.putExtra(EXTRA_WATER_PERIOD, waterPeriodDays);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), intentID, intent, 0);

        int interval = waterPeriodDays * 1000 * 60 * 60 * 24;
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, alarmIntent);
        Toast.makeText(this, "Notification Set", Toast.LENGTH_SHORT).show();
    }
}
