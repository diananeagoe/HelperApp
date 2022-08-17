package com.example.apphelper.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.apphelper.R;
import com.example.apphelper.databinding.ActivityWorkoutsBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.vo.DateData;

public class WorkoutsActivity extends AppCompatActivity {

    public ActivityWorkoutsBinding binding;
    ExpCalendarView calendarView;
    TextView textViewMonth;
    public int month = 1;
    public int year = 2022;
    Button addWorkoutToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWorkoutsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Workout calendar");

        calendarView = ((ExpCalendarView) binding.calendarViewWorkouts);
        textViewMonth = binding.textViewMonth;
        addWorkoutToday = binding.buttonAddWorkoutToday;

        SharedPreferences sharedPreferences = this.getSharedPreferences("gymLogPrefs", MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();


        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);


        month = cal.get(Calendar.MONTH);
        month++;

        year = cal.get(Calendar.YEAR);

        String mon = updateMonth();
        String yr = "" + year;
        textViewMonth.setText( mon + " " + yr );

        calendarView.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChange(int yearChange, int monthChange) {
                month = monthChange;
                year = yearChange;
                String mon = updateMonth();
                String yr = "" + year;
                textViewMonth.setText( mon + " " + yr );
            }
        });

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {

                if( isWorkoutDay(date.getDay(), date.getMonth(), date.getYear()) ){


                    AlertDialog builder = new AlertDialog.Builder(binding.calendarViewWorkouts.getContext())
                            .setTitle("Delete a workout")
                            .setMessage("Are you sure you want to delete the workout on " + date.getDay() + "/" + date.getMonth() + "/" + date.getYear() + " ?")

                            .setPositiveButton( "Yes" , new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    int wn = sharedPreferences.getInt("WORKOUT NUMBER", 0);

                                    int ind = findWorkoutDay(date.getDay(), date.getMonth(), date.getYear());

                                    if( ind>0 ){

                                        for( int i=ind; i<wn; i++ ){
                                            int next = i+1;

                                            int dd = sharedPreferences.getInt( "workout day"+next, 0 );
                                            int mm = sharedPreferences.getInt( "workout month"+next, 0 );
                                            int yy = sharedPreferences.getInt( "workout year"+next, 0 );

                                            preferencesEditor.putInt( "workout day"+i, dd );
                                            preferencesEditor.putInt( "workout month"+i, mm );
                                            preferencesEditor.putInt( "workout year"+i, yy );
                                        }

                                    }

                                    preferencesEditor.remove( "workout day"+wn );
                                    preferencesEditor.remove( "workout month"+wn );
                                    preferencesEditor.remove( "workout year"+wn );

                                    wn--;

                                    preferencesEditor.putInt( "WORKOUT NUMBER", wn );

                                    preferencesEditor.commit();





                                    Log.d( "este marcat", "este" );


                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("Cancel", null)
                            .setIcon(R.drawable.ic_baseline_delete_24)
                            .show();


                } else {

                    AlertDialog builder = new AlertDialog.Builder(binding.calendarViewWorkouts.getContext())
                            .setTitle("Log a workout")
                            .setMessage("Do you want to log a workout for " + date.getDay() + "/" + date.getMonth() + "/" + date.getYear() + " ?")

                            .setPositiveButton( "Yes" , new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    if( sharedPreferences.contains("WORKOUT NUMBER") ){
                                        int wn = sharedPreferences.getInt("WORKOUT NUMBER", 0);
                                        wn++;

                                        preferencesEditor.putInt( "WORKOUT NUMBER", wn );

                                        preferencesEditor.putInt( "workout year"+wn, date.getYear() );
                                        preferencesEditor.putInt( "workout month"+wn, date.getMonth() );
                                        preferencesEditor.putInt( "workout day"+wn, date.getDay() );

                                    } else {

                                        preferencesEditor.putInt( "WORKOUT NUMBER", 1 );

                                        preferencesEditor.putInt( "workout year"+1, date.getYear() );
                                        preferencesEditor.putInt( "workout month"+1, date.getMonth() );
                                        preferencesEditor.putInt( "workout day"+1, date.getDay() );

                                    }

                                    preferencesEditor.commit();

                                    calendarView.markDate(
                                            new DateData(date.getYear(), date.getMonth(), date.getDay()).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND,
                                                    Color.rgb( 255, 166, 0 )))
                                    );

                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("Cancel", null)
                            .setIcon(R.drawable.ic_baseline_run_circle_24)
                            .show();

                }



            }
        });


        addWorkoutToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int yy = cal.get( Calendar.YEAR );
                int mm = cal.get( Calendar.MONTH );
                mm++;
                int dd = cal.get( Calendar.DAY_OF_MONTH );

                if( !isWorkoutDay(dd, mm, yy) ){

                    if( sharedPreferences.contains("WORKOUT NUMBER") ){
                        int wn = sharedPreferences.getInt("WORKOUT NUMBER", 0);
                        wn++;

                        preferencesEditor.putInt( "WORKOUT NUMBER", wn );

                        preferencesEditor.putInt( "workout year"+wn, yy );
                        preferencesEditor.putInt( "workout month"+wn, mm );
                        preferencesEditor.putInt( "workout day"+wn, dd );

                    } else {

                        preferencesEditor.putInt( "WORKOUT NUMBER", 1 );

                        preferencesEditor.putInt( "workout year"+1, yy );
                        preferencesEditor.putInt( "workout month"+1, mm );
                        preferencesEditor.putInt( "workout day"+1, dd );

                    }

                    preferencesEditor.commit();


                    calendarView.markDate(
                            new DateData( yy, mm, dd ).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND,
                                    Color.rgb( 255, 166, 0 )))
                    );

                }

            }
        });



        //calendarView = binding.calendarViewWorkouts;

        //calendarView.setDate( calendarView.getDate() );




        //calendarView.setDateCell(R.layout.layout_date_cell);



        /*
        ArrayList<DateData> dates=new ArrayList<>();
        dates.add(new DateData(2018,04,26));
        dates.add(new DateData(2018,04,27));

         */


        /*
        calendarView.markDate(
                new DateData(2022, 7, 1).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.rgb( 255, 166, 0 )))
                );

         */

        if( sharedPreferences.contains("WORKOUT NUMBER") ){
            int wn = sharedPreferences.getInt("WORKOUT NUMBER", 0);

            for( int i=1; i<=wn; i++ ){

                int y = sharedPreferences.getInt( "workout year"+i, 0 );
                int m = sharedPreferences.getInt( "workout month"+i, 0 );
                int d = sharedPreferences.getInt( "workout day"+i, 0 );


                calendarView.markDate(
                        new DateData( y, m, d ).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND,
                                Color.rgb( 255, 166, 0 )))
                );

            }
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public String updateMonth(){
        if(month == 1)
            return "JANUARY";
        if(month == 2)
            return "FEBRUARY";
        if(month == 3)
            return "MARCH";
        if(month == 4)
            return "APRIL";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUNE";
        if(month == 7)
            return "JULY";
        if(month == 8)
            return "AUGUST";
        if(month == 9)
            return "SEPTEMBER";
        if(month == 10)
            return "OCTOBER";
        if(month == 11)
            return "NOVEMBER";
        if(month == 12)
            return "DECEMBER";

        return "";
    }

    public boolean isWorkoutDay( int d, int m, int y ){

        SharedPreferences sharedPreferences = this.getSharedPreferences("gymLogPrefs", MODE_PRIVATE);
        //SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        int wn = sharedPreferences.getInt( "WORKOUT NUMBER", 0 );

        for(int i=1; i<=wn; i++){

            int d1 = sharedPreferences.getInt( "workout day"+i, 0 );
            int m1 = sharedPreferences.getInt( "workout month"+i, 0 );
            int y1 = sharedPreferences.getInt( "workout year"+i, 0 );

            if( d1==d && m1==m && y1==y ){
                return true;
            }

        }

        return false;
    }

    public int findWorkoutDay( int d, int m, int y ){
        SharedPreferences sharedPreferences = this.getSharedPreferences("gymLogPrefs", MODE_PRIVATE);
        //SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        int wn = sharedPreferences.getInt( "WORKOUT NUMBER", 0 );

        for(int i=1; i<=wn; i++){

            int d1 = sharedPreferences.getInt( "workout day"+i, 0 );
            int m1 = sharedPreferences.getInt( "workout month"+i, 0 );
            int y1 = sharedPreferences.getInt( "workout year"+i, 0 );

            if( d1==d && m1==m && y1==y ){
                return i;
            }

        }

        return 0;
    }

    @Override
    public void onResume() {

        super.onResume();

        SharedPreferences sharedPreferences = this.getSharedPreferences("gymLogPrefs", MODE_PRIVATE);
        //SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        if( sharedPreferences.contains("WORKOUT NUMBER") ){
            int wn = sharedPreferences.getInt("WORKOUT NUMBER", 0);

            for( int i=1; i<=wn; i++ ){

                int y = sharedPreferences.getInt( "workout year"+i, 0 );
                int m = sharedPreferences.getInt( "workout month"+i, 0 );
                int d = sharedPreferences.getInt( "workout day"+i, 0 );


                calendarView.markDate(
                        new DateData( y, m, d ).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND,
                                Color.rgb( 255, 166, 0 )))
                );

            }
        }

    }

}