package com.example.apphelper.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.apphelper.databinding.FragmentHomeBinding;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("homeValuesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        preferencesEditor.putInt( "step number", 0 );
        preferencesEditor.putInt( "sleep hours", 0 );
        preferencesEditor.putInt( "sleep minutes", 0 );
        preferencesEditor.putString( "water liters", "0" );

        preferencesEditor.commit();

    }

}
