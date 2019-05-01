package com.example.cliff.fitnessapp;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyWorkoutsFragment extends Fragment {

    private ListView workoutList;
    private ArrayList<String> workoutNameList = new ArrayList<>();
    private ArrayList<Integer> workoutIDList = new ArrayList<>();
    private WhiteTextAdapter whiteTextAdapter;

    public MyWorkoutsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_my_workouts, container, false);

        whiteTextAdapter = new WhiteTextAdapter(getActivity(), R.layout.white_text_listview, workoutNameList);
        workoutList = (ListView)(v.findViewById(R.id.workout_listview));
        workoutList.setAdapter(whiteTextAdapter);

        workoutList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String workoutName = (String) parent.getItemAtPosition(position);
                int workoutID = workoutIDList.get(position);

                System.out.println("Item " + workoutName + "ID " + workoutID);

                Intent intent = new Intent(getActivity(), WorkoutDetailActivity.class);
                intent.putExtra("WorkoutID", workoutID);
                intent.putExtra("WorkoutName", workoutName);
                startActivity(intent);
            }
        });

        populateListView();
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onStart()
    {
        //displayWorkouts();
        super.onStart();
    }

    private void populateListView()
    {
        FitnessAppHelper helper = new FitnessAppHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM WORKOUT", null);
        cursor.moveToFirst();

        int workoutCount = cursor.getCount();

        System.out.println("NUMBER OF WORKOUTS: " + workoutCount);

        if (cursor.getCount() != 0)
        {
            do
            {
                int workoutID = cursor.getInt(0);
                workoutIDList.add(workoutID);
                String workoutName = cursor.getString(1);
                workoutNameList.add(workoutName);
            }while (cursor.moveToNext());
        }

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

            //TODO delete this!
            Cursor testCursor = db.rawQuery("SELECT * FROM EXERCISE", null);
            testCursor.moveToFirst();
            System.out.println("Total exercises " + testCursor.getCount());

            do
            {
                int workoutID = workoutCursor.getInt(0);

                text += workoutCursor.getString(workoutCursor.getColumnIndex("NAME"));

                Cursor exerciseCursor = db.rawQuery("SELECT * FROM EXERCISE WHERE WORKOUT = " + workoutID + "", null);
                exerciseCursor.moveToFirst();

                System.out.println("Exercise Count: " + exerciseCursor.getCount());

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

        //TODO delete this, not needed?
        //TextView workoutText = (TextView)(getView().findViewById(R.id.my_workouts_text));
        //System.out.println("WORKOUT " + workoutText);
        //workoutText.setText(text);
    }

}
