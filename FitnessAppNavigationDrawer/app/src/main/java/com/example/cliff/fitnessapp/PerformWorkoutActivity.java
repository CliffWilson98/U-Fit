package com.example.cliff.fitnessapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PerformWorkoutActivity extends AppCompatActivity {

    private String workoutName;
    private ArrayList<Exercise> exerciseList = new ArrayList<>();
    private int currentExerciseIndex;

    private int repCounter;
    private int setCounter;

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

        repCounter = 0;
        setCounter = 0;
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
            int sets = cursor.getInt(cursor.getColumnIndex("SETS"));
            int weight = cursor.getInt(cursor.getColumnIndex("WEIGHT"));

            Exercise exercise = new Exercise(exerciseName, reps, sets, weight);
            exerciseList.add(exercise);
        }while (cursor.moveToNext());
    }

    private void updateEditText()
    {
        TextView nameTextView = findViewById(R.id.workout_name);
        nameTextView.setText(workoutName);

        Exercise currentExercise = exerciseList.get(currentExerciseIndex);

        TextView exerciseTextView = findViewById(R.id.exercise_instructions);
        String exerciseInstructions = String.format("%dx%d %s at %d lbs", currentExercise.getSets(), currentExercise.getReps(), currentExercise.getName(), currentExercise.getWeight());
        exerciseTextView.setText(exerciseInstructions);
    }

    public void incrementCounter(View v)
    {
        Exercise currentExercise = exerciseList.get(currentExerciseIndex);

        Button button = (Button) findViewById(R.id.counter_button);

        if (repCounter == currentExercise.getReps() - 1)
        {
            repCounter = 0;
            setCounter ++;
        }
        else
        {
            repCounter ++;
        }

        String buttonText = String.format("%dx%d", setCounter, repCounter);
        button.setText(buttonText);
    }

    //when an exercise is complete the next one needs to be obtained
    public void goToNextExercise()
    {
        if (currentExerciseIndex < (exerciseList.size() - 1))
        {
            currentExerciseIndex++;
            updateEditText();
        }
    }

    //called whenever the skip exercise button is pressed. It will need to take note
    //that an exercise was skipped and not finished
    public void skipExercise(View v)
    {
        goToNextExercise();
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
