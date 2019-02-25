package com.example.cliff.fitnessapp;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyWorkoutsFragment extends Fragment {


    public MyWorkoutsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_workouts, container, false);
    }

    @Override
    public void onStart()
    {
        displayWorkouts();
        super.onStart();
    }

    //TODO remove this
    //this method is just a way to make sure the database is working properly
    private void displayWorkouts()
    {
        String text = "";

        SQLiteOpenHelper helper = new FitnessAppHelper(getActivity());

        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();

            Cursor workoutCursor = db.rawQuery("SELECT * FROM WORKOUT ORDER BY _id", null);
            workoutCursor.moveToFirst();

            do
            {
                int workoutID = workoutCursor.getInt(0);

                text += workoutCursor.getString(workoutCursor.getColumnIndex("NAME"));

                Cursor exerciseCursor = db.rawQuery("SELECT * FROM EXERCISE WHERE WORKOUT = " + workoutID + "", null);
                exerciseCursor.moveToFirst();

                do
                {
                    text += " " + exerciseCursor.getString(exerciseCursor.getColumnIndex("NAME"));
                }
                while(exerciseCursor.moveToNext());

                text += "\n";
            }
            while (workoutCursor.moveToNext());
        }
        catch(SQLiteException e)
        {
            Toast.makeText(getActivity(), "DATABASE UNAVAILABLE", Toast.LENGTH_SHORT).show();
        }

        TextView workoutText = getView().findViewById(R.id.my_workouts_text);
        workoutText.setText(text);
    }

}
