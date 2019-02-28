package com.example.cliff.fitnessapp;

import android.app.ActionBar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class WorkoutDetailActivity extends AppCompatActivity {

    private int workoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.workout_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null)
        {
            String workoutName = (String)getIntent().getStringExtra("WorkoutName");
            workoutId = getIntent().getIntExtra("WorkoutID", 0);

            System.out.println("Intent is not null! Name = " + workoutName + "ID = " + workoutId);

            displayWorkoutDetails(workoutName, workoutId);
        }
    }

    private void displayWorkoutDetails(String workoutName, int workoutID)
    {
        TextView workoutNameTextView = findViewById(R.id.workout_name);
        workoutNameTextView.setText(workoutName);

        TextView exerciseNamesTextView = findViewById(R.id.exercise_names);

        FitnessAppHelper helper = new FitnessAppHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM EXERCISE WHERE WORKOUT = " + workoutID, null);
        cursor.moveToFirst();

        String exerciseNames = "";

        do
        {
            exerciseNames += cursor.getString(1) + "\n";
        }while (cursor.moveToNext());

        exerciseNamesTextView.setText(exerciseNames);
    }

    //Necessary for the back button to work
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void deleteWorkout(View v)
    {
        FitnessAppHelper helper = new FitnessAppHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        //String whereClause = "_id=?";
        //String[] whereArgs = new String[] { String.valueOf(row) };
        //db.delete("WORKOUT", whereClause, whereArgs);

        db.delete("WORKOUT", "_id = " + workoutId, null);
        db.delete("EXERCISE", "WORKOUT = " + workoutId, null);
    }

    public void performWorkout(View v)
    {
        System.out.println("perform workout pressed");
    }
}
