package com.example.apphelper.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.apphelper.R;
import com.example.apphelper.databinding.FragmentHomeBinding;

import java.util.Calendar;

public class NotificationsAlarmReceiver extends BroadcastReceiver {

    public final String CHANNEL_ID = "channel1";

    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        SharedPreferences sharedPreferences = context.getSharedPreferences("homeValuesPrefs", MODE_PRIVATE);
        //SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        int h1 = 9;
        int h2 = 22;

        if( sharedPreferences.contains("first notif hour") ){
            h1 = sharedPreferences.getInt("first notif hour", 9);
            h2 = sharedPreferences.getInt("second notif hour", 22);
        }

        if( (calendar.get(Calendar.HOUR) >= h1) && (calendar.get(Calendar.HOUR) <= h2) ){

            createNotificationChannel(context);

            NotificationCompat.Builder builder = new NotificationCompat.Builder( context, CHANNEL_ID )
                    .setSmallIcon(R.drawable.ic_baseline_water_drop_24)
                    .setContentTitle("Drink water!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // notificationId is a unique int for each notif
            notificationManager.notify( 1, builder.build());

        }

    }


    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channelNotif1";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            //channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}
