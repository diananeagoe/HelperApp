package com.example.apphelper.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphelper.R;
import com.example.apphelper.databinding.FragmentHomeBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.w3c.dom.Text;

import java.util.Calendar;

public class HomeFragment extends Fragment implements BottomSheetDialogStepTarget.ShareDataInterface {

    private FragmentHomeBinding binding;
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

    CardView waterCardView;
    CardView sleepCardView;

    public int stepNumber;
    public int sleepHours;
    public int sleepMinutes;
    public String stringWaterLiters;



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
                                stepTarget = Integer.parseInt(StringTarget);

                                preferencesEditor.putInt("step target", stepTarget);
                                preferencesEditor.commit();

                                calculateStepPercentage(stepNumber);
                                stepGoal.setText( "/" + stepTarget + " steps" );
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
                                String StringTarget = waterEditText.getText().toString();


                                preferencesEditor.putString("water liters", StringTarget);
                                preferencesEditor.commit();

                                setWaterPercentage(StringTarget);
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

    }

    public void alarmManagerSet(){
        // intent
        AlarmManager alarmMgr = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        // Set the alarm to start at approximately 00:00 h
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        //repeat alarm every 24hours
        alarmMgr.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}