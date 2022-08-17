package com.example.apphelper.ui.dashboard;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphelper.R;
import com.example.apphelper.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    CardView notifsIntervalCard;
    EditText firstHourEditText;
    EditText secondHourEditText;

    TextView firstHText;
    TextView secondHText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        notifsIntervalCard = binding.notificationIntervalCard;
        firstHText = binding.firstHour;
        secondHText = binding.secondHour;


        notifsIntervalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.sheet_layout_notification_interval, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


                firstHourEditText = dialoglayout.findViewById(R.id.editTextFirstHour);
                secondHourEditText = dialoglayout.findViewById(R.id.editTextSecondHour);


                builder.setView(dialoglayout);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String hour1 = firstHourEditText.getText().toString();
                                String hour2 = secondHourEditText.getText().toString();

                                int h1 = Integer.parseInt(hour1);
                                int h2 = Integer.parseInt(hour2);

                                if( ((h2 - h1) < 2) || (h1<0) || (h2<0) || (h1>23) || (h2>23) ){
                                    Toast.makeText(getContext(), "Invalid hours!", Toast.LENGTH_SHORT).show();
                                } else {

                                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("homeValuesPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

                                    preferencesEditor.putInt("first notif hour", h1);
                                    preferencesEditor.putInt("second notif hour", h2);

                                    preferencesEditor.commit();

                                    firstHText.setText("" + h1);
                                    secondHText.setText("" + h2);

                                }

                            }
                        }
                );
                builder.show();

            }
        });




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}