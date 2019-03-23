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

//TODO fix crash when user tries to load an exercise for which there are no recorded values
//TODO fix graph only showing first 8 values recorded
public class StatsFragment extends Fragment {

    private ArrayList<Integer> weightList = new ArrayList<Integer>();
    private GraphView graph;

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

        initializeGraph();
        retrieveDatabaseInformationForExerciseAndUpdateGraph("Deadlift");
    }

    private void initializeGraph()
    {
        graph = (GraphView) getView().findViewById(R.id.graph);
    }

    private void retrieveDatabaseInformationForExerciseAndUpdateGraph(String exerciseName)
    {
        SQLiteDatabase db = getDatabase();

        int exerciseID = getIdOfExerciseFromName(db, exerciseName);

        populateExerciseInformationArrays(getCursorFromExerciseId(db, exerciseID));

        updateGraph();
    }

    private void updateGraph()
    {
        DataPoint[] pointArray = generateDataPointArrayFromArrayList(weightList);
        graph.addSeries(createLineGraphSeriesFromPointArray(pointArray));
    }

    private DataPoint[] generateDataPointArrayFromArrayList(ArrayList<Integer> list)
    {
            DataPoint[] pointArray = new DataPoint[list.size()];

            for (int i = 0; i < pointArray.length; i ++)
            {
                pointArray[i] = new DataPoint(i, list.get(i));
            }

            return pointArray;
    }

    private LineGraphSeries<DataPoint> createLineGraphSeriesFromPointArray(DataPoint[] pointArray)
    {
        return new LineGraphSeries<>(pointArray);
    }

    private SQLiteDatabase getDatabase()
    {
        FitnessAppHelper helper = new FitnessAppHelper(getActivity());
        return helper.getReadableDatabase();
    }

    private int getIdOfExerciseFromName(SQLiteDatabase db, String exerciseName)
    {
        Cursor cursor = db.rawQuery("SELECT * FROM EXERCISERESULTSTABLE WHERE NAME = ?", new String[] {exerciseName});
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    private Cursor getCursorFromExerciseId(SQLiteDatabase db, int id)
    {
        Cursor cursor = db.rawQuery("SELECT * FROM EXERCISERESULTS WHERE EXERCISERESULTSTABLEID = " + id,null);
        cursor.moveToFirst();
        return cursor;
    }

    private void populateExerciseInformationArrays(Cursor cursor)
    {
        do {
            int weight = getWeightFromCursor(cursor);
            int sets = getSetsFromCursor(cursor);
            weightList.add(weight);

        }while(cursor.moveToNext());
    }

    private int getWeightFromCursor(Cursor cursor)
    {
        return cursor.getInt(cursor.getColumnIndex("WEIGHT"));
    }

    private int getSetsFromCursor(Cursor cursor)
    {
        return cursor.getInt(cursor.getColumnIndex("SETS"));
    }


}
