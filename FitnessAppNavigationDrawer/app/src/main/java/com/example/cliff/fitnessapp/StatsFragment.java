package com.example.cliff.fitnessapp;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {

    private ArrayList<Integer> weightList = new ArrayList<Integer>();

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        /*GraphView graph = (GraphView) getView().findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);*/
        retrieveDatabaseCursorForExercise("Deadlift");
    }

    private void retrieveDatabaseCursorForExercise(String exerciseName)
    {
        FitnessAppHelper helper = new FitnessAppHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        //getting ID of exercise from Name
        Cursor cursor = db.rawQuery("SELECT * FROM EXERCISERESULTSTABLE WHERE NAME = ?", new String[] {exerciseName});
        cursor.moveToFirst();
        int exerciseResultsTableID = cursor.getInt(0);
        System.out.println(String.format("ID FOR %s = %d", exerciseName, exerciseResultsTableID));

        //cursor = db.rawQuery("SELECT * FROM EXERCISERESULTS WHERE EXERCISERESULTSTABLEID = " + exerciseResultsTableID,null);
        cursor = db.rawQuery("SELECT * FROM EXERCISERESULTS WHERE EXERCISERESULTSTABLEID = " + exerciseResultsTableID,null);
        cursor.moveToFirst();
        int weight = cursor.getInt(cursor.getColumnIndex("WEIGHT"));
        int sets = cursor.getInt(cursor.getColumnIndex("SETS"));
        System.out.println(String.format("Weight: %d, Sets: %d", weight, sets));
        //populating the exercise ArrayList
        /*cursor = db.rawQuery("SELECT * FROM EXERCISE WHERE WORKOUT = " + workoutID, null);
        cursor.moveToFirst();
        do
        {
            String exerciseName = cursor.getString(cursor.getColumnIndex("NAME"));
            int reps = cursor.getInt(cursor.getColumnIndex("REPS"));
            int sets = cursor.getInt(cursor.getColumnIndex("SETS"));
            int weight = cursor.getInt(cursor.getColumnIndex("WEIGHT"));

            Exercise exercise = new Exercise(exerciseName, reps, sets, weight);
            exerciseList.add(exercise);
        }while (cursor.moveToNext());*/
    }

}
