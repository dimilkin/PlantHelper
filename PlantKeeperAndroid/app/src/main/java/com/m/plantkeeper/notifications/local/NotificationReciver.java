package com.m.plantkeeper.notifications.local;

import static com.m.plantkeeper.Constants.EXTRA_PLANT_NAME;
import static com.m.plantkeeper.Constants.EXTRA_USER_PLANT_NAME;
import static com.m.plantkeeper.Constants.WATER_PERIOD;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.m.plantkeeper.MainActivity;
import com.m.plantkeeper.R;

import java.util.Date;

public class NotificationReciver extends BroadcastReceiver {

    private Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @Override
    public void onReceive(Context context, Intent intent) {
        String plantName = intent.getStringExtra(EXTRA_USER_PLANT_NAME);
        int waterPeriod = intent.getIntExtra(WATER_PERIOD, -1);
        Intent notifyIntent = new Intent(context, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntent.putExtra(WATER_PERIOD, waterPeriod);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        showNotification(plantName, context, notifyPendingIntent);
    }

    private void showNotification(String plantName, Context context, PendingIntent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Alarm";
            String description = "alarm description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel1 = new NotificationChannel("alarmID", name, importance);
            channel1.setDescription(description);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarmID")
                .setContentTitle(plantName)
                .setContentText("Go do things right")
                .setContentIntent(intent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Go do things right"))
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(notificationId, builder.build());
    }
}
