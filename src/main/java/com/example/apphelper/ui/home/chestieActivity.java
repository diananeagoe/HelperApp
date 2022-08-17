package com.example.apphelper.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.apphelper.databinding.ActivityChestieBinding;

public class chestieActivity extends AppCompatActivity implements BottomSheetDialogStepTarget.ShareDataInterface {

    private ActivityChestieBinding binding;
    Button chestieButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChestieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chestieButton = binding.buttontargetchestie;


        chestieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                BottomSheetDialogStepTarget bottomSheet = new BottomSheetDialogStepTarget();
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");


            }
        });

    }

    @Override
    public void sendDataName(int data) {

        //int stepTarget = data;

        SharedPreferences sharedPreferences = this.getSharedPreferences("homeValuesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        preferencesEditor.putInt("step target", data);
        preferencesEditor.commit();


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
}