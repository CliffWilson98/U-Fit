package com.example.cliff.fitnessapp;

import android.database.Cursor;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;

public class Graph {
    private ArrayList<Integer> dataPoints = new ArrayList<>();
    private String databaseColumn;
    private GraphView view;

    public Graph(GraphView graphView, String databaseColumn) {
        this.view = graphView;
        view.getViewport().setXAxisBoundsManual(true);
        view.getViewport().setYAxisBoundsManual(true);

        this.databaseColumn = databaseColumn;
    }

    public void clear() {
        dataPoints.clear();
    }

    public void updateGraph() {
        view.removeAllSeries();
        DataPoint[] pointArray = generateDataPointArrayFromArrayList(dataPoints);
        view.addSeries(createLineGraphSeriesFromPointArray(pointArray));
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

    private void setGraphXAxisBounds(ArrayList<Integer> list)
    {
        view.getViewport().setMinX(0);
        view.getViewport().setMaxX(list.size() - 1);
    }

    private void setGraphYAxisBounds(ArrayList<Integer> list)
    {
        view.getViewport().setMinY(0);
        view.getViewport().setMaxY(getMaxValueFromList(list));
    }

    private int getMaxValueFromList(ArrayList<Integer> list)
    {
        if (!list.isEmpty())
        {
            return Collections.max(list);
        }
        else
        {
            return 0;
        }
    }

    public void addDataFromCursor(Cursor cursor) {
        int data = cursor.getInt(cursor.getColumnIndex(databaseColumn));
        dataPoints.add(data);
    }
}
