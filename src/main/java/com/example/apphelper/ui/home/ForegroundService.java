package com.example.apphelper.ui.home;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.apphelper.MainActivity;
import com.example.apphelper.R;

public class ForegroundService extends Service {

    public final int STEPS_ID = 2;
    public final String STEPS_CHANNEL_ID = "channel2";
    public Context NotifContext;
    public int stepNumber = 0;

    private SensorManager sensorManager;
    private Sensor sensor;

    /**
     * @deprecated
     */
    public ForegroundService() {
        //super("Foreground Service");
        //NotifContext = getApplicationContext();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //NotifContext = intent.getExtras().getParcelable("context");
        NotifContext = getApplicationContext();

        createNotificationChannel(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("homeValuesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);

        //Notification notif = new Notification.Builder( NotifContext, STEPS_CHANNEL_ID )

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, STEPS_CHANNEL_ID)
                .setOngoing(true)
                .setContentTitle("Pedometer is currently running")
                //.setContentText("")
                .setSmallIcon(R.drawable.ic_baseline_run_circle_24)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE);

        Notification notif = notificationBuilder.build();

        //Notification notification = notificationBuilder//.setTicker(getText(R.string.ticker_text)).build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify( STEPS_ID, notif);


        startForeground(STEPS_ID, notif);

        Log.e("started ", "NOTIF SERVICE");

        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener( new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                //stepNumber = sharedPreferences.getInt("step number", 0);

                stepNumber = (int) event.values[0];
                preferencesEditor.putInt("step number", stepNumber);
                preferencesEditor.commit();
                //numberSteps.setText(""+stepNumber);

                //Log.e("step number", "changed");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //Log.d("MY_APP", sensor.toString() + " - " + accuracy);
            }
        }, sensor, SensorManager.SENSOR_DELAY_FASTEST );

        // returns the status
        // of the program
        return START_STICKY;
    }


    //protected void onHandleIntent(@Nullable Intent intent) { }


    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channelNotif2";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(STEPS_CHANNEL_ID, name, importance);
            //channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
