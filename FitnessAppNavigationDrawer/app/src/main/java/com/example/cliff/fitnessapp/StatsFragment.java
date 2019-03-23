package com.example.cliff.fitnessapp;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
    private ArrayList<Integer> setList = new ArrayList<Integer>();

    private GraphView graph;
    private Spinner exerciseNamesSpinner;

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stats, container, false);
        exerciseNamesSpinner = v.findViewById(R.id.stats_screen_exercise_spinner);

        setUpExerciseNamesSpinnerAdapter();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        initializeGraph();
    }

    private void setUpExerciseNamesSpinnerAdapter()
    {
        populateSpinnerWithExerciseNames();
        addListenerToSpinner();
    }

    private void populateSpinnerWithExerciseNames()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                getArrayListOfExerciseNames()
        );

        exerciseNamesSpinner.setAdapter(adapter);
    }

    private ArrayList<String> getArrayListOfExerciseNames()
    {
        FitnessAppHelper helper = new FitnessAppHelper(getActivity());
        return helper.getDefinedExerciseNames();
    }

    private void addListenerToSpinner()
    {
        exerciseNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                retrieveDatabaseInformationForExerciseAndUpdateGraph(convertSpinnerPositionToDatabaseId(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

            private int convertSpinnerPositionToDatabaseId(int position)
            {
                return position + 1;
            }


        });
    }

    private void initializeGraph()
    {
        graph = (GraphView) getView().findViewById(R.id.weight_graph);
        graph.getViewport().setXAxisBoundsManual(true);
    }

    private void retrieveDatabaseInformationForExerciseAndUpdateGraph(int exerciseID)
    {
        clearValuesFromInformationArrays();
        populateExerciseInformationArrays(getCursorFromExerciseId(getDatabase(), exerciseID));
        updateGraph();
    }

    private void updateGraph()
    {
        graph.removeAllSeries();
        DataPoint[] pointArray = generateDataPointArrayFromArrayList(weightList);
        graph.addSeries(createLineGraphSeriesFromPointArray(pointArray));
        setGraphXAxisBounds();
    }

    private void setGraphXAxisBounds()
    {
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(weightList.size() - 1);
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

    //For now this method does not need to be used.
    /*private int getIdOfExerciseFromName(SQLiteDatabase db, String exerciseName)
    {
        Cursor cursor = db.rawQuery("SELECT * FROM DEFINEDEXERCISE WHERE NAME = ?", new String[] {exerciseName});
        cursor.moveToFirst();
        return cursor.getInt(0);
    }*/

    private Cursor getCursorFromExerciseId(SQLiteDatabase db, int id)
    {
        return db.rawQuery("SELECT * FROM EXERCISERESULTS WHERE DEFINEDEXERCISEID = " + id,null);
    }

    private void populateExerciseInformationArrays(Cursor cursor)
    {
        if (cursor.getCount() != 0)
        {
            cursor.moveToFirst();
            do {
                weightList.add(getWeightFromCursor(cursor));
                setList.add(getSetsFromCursor(cursor));
            }while(cursor.moveToNext());
        }
    }

    private void clearValuesFromInformationArrays()
    {
        weightList.clear();
        setList.clear();
    }

    private int getWeightFromCursor(Cursor cursor)
    {
        return cursor.getInt(cursor.getColumnIndex("WEIGHT"));
    }

    private int getSetsFromCursor(Cursor cursor)
    {
        return cursor.getInt(cursor.getColumnIndex("SETS"));
    }

    private String getNameFromCursor(Cursor cursor)
    {
        return cursor.getString(cursor.getColumnIndex("NAME"));
    }


}
