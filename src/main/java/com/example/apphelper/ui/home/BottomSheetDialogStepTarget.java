package com.example.apphelper.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.apphelper.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialogStepTarget extends BottomSheetDialogFragment {

    ShareDataInterface shareDataInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        /*
        EditText targetStepsEditText = v.findViewById(R.id.editTextStepTarget);
        Button continueButton = v.findViewById(R.id.buttonContinue);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("homeValuesPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String StringTarget = targetStepsEditText.getText().toString();

                int data = Integer.parseInt(StringTarget);

                preferencesEditor.putInt("step target", data);
                preferencesEditor.commit();


                shareDataInterface.sendDataName(data);
                dismiss();





            }
        });

         */


        return v;
    }

    public interface ShareDataInterface
    {
        void sendDataName(int data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            shareDataInterface = (ShareDataInterface) context;
        } catch (RuntimeException e) {
            throw new RuntimeException(context.toString()+" Must implement method");
        }

    }
}