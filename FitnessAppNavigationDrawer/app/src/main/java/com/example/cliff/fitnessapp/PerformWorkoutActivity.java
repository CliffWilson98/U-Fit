package com.example.cliff.fitnessapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;

public class PerformWorkoutActivity extends AppCompatActivity {

    private String workoutName;
    private ArrayList<Exercise> exerciseList = new ArrayList<>();
    private int currentExerciseIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currentExerciseIndex = 0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_workout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.workout_toolbar);
        setSupportActionBar(toolbar);

        getWorkoutDetails();
        updateEditText();
    }

    //this will get the workouts name and populate the exercise ArrayList
    private void getWorkoutDetails()
    {
        int workoutID = getIntent().getIntExtra("workoutID", 0);

        FitnessAppHelper helper = new FitnessAppHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        //getting the workout name
        Cursor cursor = db.rawQuery("SELECT * FROM WORKOUT WHERE _id = " + workoutID, null);
        cursor.moveToFirst();
        workoutName = cursor.getString(cursor.getColumnIndex("NAME"));

        //populating the exercise ArrayList
        cursor = db.rawQuery("SELECT * FROM EXERCISE WHERE WORKOUT = " + workoutID, null);
        cursor.moveToFirst();
        do
        {
            String exerciseName = cursor.getString(cursor.getColumnIndex("NAME"));
            int reps = cursor.getInt(cursor.getColumnIndex("REPS"));
            int repCount = cursor.getInt(cursor.getColumnIndex("REPCOUNT"));
            int weight = cursor.getInt(cursor.getColumnIndex("WEIGHT"));

            Exercise exercise = new Exercise(exerciseName, reps, repCount, weight);
            exerciseList.add(exercise);
        }while (cursor.moveToNext());
    }

    private void updateEditText()
    {

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
