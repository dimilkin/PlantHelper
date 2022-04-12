package com.m.plantkeeper.notifications.local;

import static android.app.PendingIntent.FLAG_ONE_SHOT;
import static com.m.plantkeeper.Constants.EXTRA_PLANT_NAME;
import static com.m.plantkeeper.Constants.EXTRA_USER_PLANT_NAME;
import static com.m.plantkeeper.Constants.WATER_PERIOD;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import java.util.Calendar;

public class AlarmProvider extends ContextWrapper {

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

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getApplicationContext(), NotificationReciver.class);
        alarmIntent.putExtra(EXTRA_USER_PLANT_NAME, name);
        alarmIntent.putExtra(WATER_PERIOD, waterPeriodDays);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), intentID, alarmIntent, FLAG_ONE_SHOT);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),5000, pendingIntent);
//        } else {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        }

//      Set the alarm to start at 11:00 AM
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), 86400000L * waterPeriodDays, // for repeating in every 24 hours
                pendingIntent);
    }
}
