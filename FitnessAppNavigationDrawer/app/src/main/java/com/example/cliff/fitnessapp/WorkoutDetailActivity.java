package com.example.cliff.fitnessapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

        Database db = Database.getDatabase();
        QueryResult queryResult = db.query("SELECT * FROM EXERCISE WHERE WORKOUT = " + workoutID);
        queryResult.moveToFirst();
        String exercises = "";
        do {
            exercises += (queryResult.getString(1) + "\n" +
                            "Reps: " + queryResult.getInt(2) + "  -  " +
                            "Sets: " + queryResult.getInt(3) + "  -  " +
                            "Weight: " + queryResult.getInt(4) + "\n\n");
        } while (queryResult.moveToNext());

        exerciseNamesTextView.setText(exercises);
    }

    //Necessary for the back button to work
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void checkForDeleteConfirmation(View v)
    {
        AlertDialog diaBox = AskOption();
        diaBox.show();
    }

    private void deleteWorkout()
    {
        Database db = Database.getDatabase();

        db.delete("WORKOUT", "_id = " + workoutId);
        db.delete("EXERCISE", "WORKOUT = " + workoutId);

        //Once the workout is deleted the user is returned to the mainActivity
        returnToMyWorkouts();
    }

    private void returnToMyWorkouts()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fragmentToLoad", MainActivity.MY_WORKOUT_FRAGMENT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void goToPerformWorkout(View v)
    {
        Intent intent = new Intent(this, PerformWorkoutActivity.class);
        intent.putExtra("workoutID", workoutId);
        startActivity(intent);
    }

    //Used to create dialog box to confirm that the user wants to delete a workout
    private AlertDialog AskOption()
    {
        AlertDialog deleteDialogBox = new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete Workout")
                .setMessage("Are you sure you want to delete this workout?")
                //TODO change this icon, it is just to test
                .setIcon(R.drawable.ic_menu_send)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteWorkout();
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return deleteDialogBox;
    }
}
