package com.example.cliff.fitnessapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class PerformWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_workout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.workout_toolbar);
        setSupportActionBar(toolbar);
    }


    //The back button needs to be disabled in this activity
    //If the user wants to quit a workout they will use the cancel workout button
    @Override
    public void onBackPressed()
    {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Press cancel workout if you want to go back!",
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
