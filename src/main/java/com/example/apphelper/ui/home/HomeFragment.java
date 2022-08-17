package com.example.apphelper.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.apphelper.MainActivity;
import com.example.apphelper.R;
import com.example.apphelper.databinding.FragmentHomeBinding;

import java.util.Calendar;

public class HomeFragment extends Fragment implements BottomSheetDialogStepTarget.ShareDataInterface {

    public final String CHANNEL_ID = "channel1";
    public final String STEPS_CHANNEL_ID = "channel2";

    private static final int ACTIVITY_PERMISSION_CODE = 1;

    private FragmentHomeBinding binding;
    //private BottomSheetLayoutBinding b;
    public randomMessageTextArray messageTextArray;

    public int stepTarget;

    TextView percentageText;
    TextView numberSteps;
    TextView randomMessage;
    TextView waterLiters;
    TextView waterPercentageText;
    TextView gymHistoryButton;
    TextView sleepHoursText;
    TextView sleepMinutesText;
    TextView setStepTargetButton;
    TextView stepGoal;

    ProgressBar stepsProgressBar;
    ProgressBar waterProgressBar;

    Button workoutButton;
    Button continueButton;

    EditText stepTargetEditText;
    EditText waterEditText;
    EditText sleepHEditText;
    EditText sleepMinEditText;

    CardView waterCardView;
    CardView sleepCardView;

    public int stepNumber;
    public int sleepHours;
    public int sleepMinutes;
    public String stringWaterLiters = "0";




    private SensorManager sensorManager;
    private Sensor sensor;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /**
         * initialise
         */
        percentageText = binding.progressBarPercentageText;
        numberSteps = binding.currentStepNumber;
        randomMessage=binding.randomMessageText;
        waterLiters = binding.waterLiters;
        waterPercentageText = binding.waterProgressBarPercentageText;
        gymHistoryButton = binding.viewGymHistoryButton;
        sleepHoursText = binding.sleepHours;
        sleepMinutesText = binding.sleepMinutes;
        setStepTargetButton = binding.setStepTargetButton;
        stepGoal = binding.stepGoal;

        stepsProgressBar = binding.progressBarSteps;
        waterProgressBar = binding.progressBarWater;

        workoutButton = binding.addGymWorkoutButton;

        waterCardView = binding.waterCard;
        sleepCardView = binding.sleepCard;



        messageTextArray = new randomMessageTextArray();




        /**
         * sharedPreferences
         */
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("homeValuesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        if( sharedPreferences.contains("step target") ){
            stepTarget = sharedPreferences.getInt("step target", 0);
        } else {
            stepTarget = 10000;
        }


        if( ! sharedPreferences.contains("step number") ){

            preferencesEditor.putInt( "step number", 0 );
            preferencesEditor.putInt( "sleep hours", 0 );
            preferencesEditor.putInt( "sleep minutes", 0 );
            preferencesEditor.putString( "water liters", "0" );

            preferencesEditor.putInt( "step target", 10000 );

        } else {

            stepNumber = sharedPreferences.getInt("step number", 0);
            sleepHours = sharedPreferences.getInt("sleep hours", 0);
            sleepMinutes = sharedPreferences.getInt("sleep minutes", 0);
            stringWaterLiters = sharedPreferences.getString("water liters", "0");

        }




        /**
         * step counter and permission
         */

        checkPermission(Manifest.permission.ACTIVITY_RECOGNITION, ACTIVITY_PERMISSION_CODE);




        /**
         * onClick listeners
         */
        workoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), WorkoutsActivity.class);
                view.getContext().startActivity(intent);

            }
        });


        gymHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), WorkoutsActivity.class);
                view.getContext().startActivity(intent);

            }
        });


        setStepTargetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.bottom_sheet_layout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                //continueButton = dialoglayout.findViewById(R.id.buttonContinue);
                stepTargetEditText = dialoglayout.findViewById(R.id.editTextStepTarget);


                builder.setView(dialoglayout);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String StringTarget = stepTargetEditText.getText().toString();

                                try{
                                    stepTarget = Integer.parseInt(StringTarget);

                                    preferencesEditor.putInt("step target", stepTarget);
                                    preferencesEditor.commit();

                                    calculateStepPercentage(stepNumber);
                                    stepGoal.setText( "/" + stepTarget + " steps" );
                                } catch(NumberFormatException e){
                                    Toast.makeText(getContext(), "Invalid number!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                );
                builder.show();

            }
        });


        waterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.sheet_layout_water, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                //continueButton = dialoglayout.findViewById(R.id.buttonContinue);
                waterEditText = dialoglayout.findViewById(R.id.editTextWater);


                builder.setView(dialoglayout);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String StringWater = waterEditText.getText().toString();


                                try {
                                    double addWater = Double.parseDouble(StringWater);
                                    double currentWater = Double.parseDouble(sharedPreferences.getString("water liters", "0"));
                                    addWater += currentWater;

                                    String StringTarget = String.format("%.2f", addWater);


                                    preferencesEditor.putString("water liters", StringTarget);
                                    preferencesEditor.commit();

                                    stringWaterLiters = StringTarget;

                                    setWaterPercentage(StringTarget);

                                    //Log.e("water", "changed");

                                }
                                catch(NumberFormatException e)
                                {
                                    //not a double
                                    Toast.makeText(getContext(), "Invalid number!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                );
                builder.show();

            }
        });


        sleepCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.sheet_layout_sleep, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                //continueButton = dialoglayout.findViewById(R.id.buttonContinue);
                sleepHEditText = dialoglayout.findViewById(R.id.editTextSleepHours);
                sleepMinEditText = dialoglayout.findViewById(R.id.editTextSleepMinutes);


                builder.setView(dialoglayout);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                try{
                                    int sleepH = Integer.parseInt( sleepHEditText.getText().toString() );
                                    sleepHours = sleepH;
                                    int sleepMin = Integer.parseInt( sleepMinEditText.getText().toString() );
                                    sleepMinutes = sleepMin;

                                    preferencesEditor.putInt("sleep hours", sleepHours);
                                    preferencesEditor.putInt("sleep minutes", sleepMinutes);
                                    preferencesEditor.commit();

                                    setSleepTime(sleepHours, sleepMinutes);
                                } catch (NumberFormatException e){
                                    Toast.makeText(getContext(), "Invalid number!", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }
                );
                builder.show();

            }
        });





        /**
         * initialise values/TextViews
         */
        setRandomMessage();
        calculateStepPercentage(stepNumber);
        setSleepTime(sleepHours, sleepMinutes);
        setWaterPercentage(stringWaterLiters);



        /**
         * reset values every 24h
         */
        alarmManagerSet();


        /**
         * notifications
         */
        notificationManagerSet();


        /**
         * foreground service
         */

        Intent notificationIntent = new Intent(getContext(), ForegroundService.class);
        //notificationIntent.putExtra("context", (Parcelable) getContext());
        getActivity().startService(notificationIntent);






        preferencesEditor.commit();
        return root;
    }

    public int getMessage(int max){

        int min = 0;


        int indice = (int)Math.floor(Math.random()*(max-min+1)+min);


        if(indice > max)
            indice = max;

        if(indice < 0)
            indice = 0;



        return indice;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void sendDataName(int data) {

        stepTarget = data;

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("homeValuesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        preferencesEditor.putInt("step target", data);
        preferencesEditor.commit();

        calculateStepPercentage(stepNumber);
        stepGoal.setText( "/" + data + " steps" );


        /*
        String StringNumberOfSteps = (String) numberSteps.getText();
        int numberOfSteps = Integer.parseInt(StringNumberOfSteps);

        double percentage = (double)numberOfSteps/(double)stepTarget;
        percentage = percentage*100;

        int p = (int)percentage;
        String pText = "" + p + "%";

        percentageText.setText( pText );
        stepsProgressBar.setProgress( p );
        stepGoal.setText( "/" + data + " steps" );

         */

    }

    public void calculateStepPercentage(int numberOfSteps){
        double percentage = (double)numberOfSteps/(double)stepTarget;
        percentage = percentage*100;

        int p = (int)percentage;
        String pText = "" + p + "%";

        numberSteps.setText(""+numberOfSteps);
        stepGoal.setText( "/" + stepTarget + " steps" );

        percentageText.setText( pText );
        stepsProgressBar.setProgress( p );
    }

    public void setSleepTime(int hours, int minutes){
        sleepHoursText.setText(""+hours);
        sleepMinutesText.setText(""+minutes);
    }

    public void setRandomMessage(){
        int size = messageTextArray.messageNumber;
        int indice = getMessage(size);
        String mes = messageTextArray.messages.get(indice);

        randomMessage.setText( mes );
    }

    public void setWaterPercentage(String stringwl){


        String StringWaterL = stringwl;
        double waterL = Double.parseDouble( StringWaterL );
        double per = waterL/(double)2.0;
        per = per*100;

        int pWater = (int)per;
        String waterPText = "" + pWater + "%";

        waterLiters.setText(stringwl);

        waterPercentageText.setText( waterPText );
        waterProgressBar.setProgress( pWater );



        //waterPercentageText.setText(stringwl);
        //waterProgressBar.setProgress(25);

    }

    public void alarmManagerSet(){
        // intent
        AlarmManager alarmMgr = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Set the alarm to start at approximately 00:00 h
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        //repeat alarm every 24 hours
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }


    public void notificationManagerSet(){

        // intent
        AlarmManager alarmMgr = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), NotificationsAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Set the alarm to start at a fixed hour
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        //repeat alarm every 2 hours
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HOUR * 2, alarmIntent);
    }



    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(getActivity(), new String[] { permission }, requestCode);
        }
        //else {
            //Toast.makeText(getContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
        //}
    }
    

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == ACTIVITY_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Physical Activity Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), "Physical Activity Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }



}