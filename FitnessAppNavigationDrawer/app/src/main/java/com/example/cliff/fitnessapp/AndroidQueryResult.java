package com.example.cliff.fitnessapp;

import android.database.Cursor;

public class AndroidQueryResult implements QueryResult {
    private Cursor cursor;

    public AndroidQueryResult(Cursor cursor) {
        this.cursor = cursor;
        moveToFirst();
    }

    @Override
    public int getInt(String columnName)
    {
        return getInt(cursor.getColumnIndex(columnName));
    }

    @Override
    public int getInt(int columnIndex) {
        return cursor.getInt(columnIndex);
    }

    @Override
    public String getString(String columnName) {
        return getString(cursor.getColumnIndex(columnName));
    }

    @Override
    public String getString(int columnIndex) {
        return cursor.getString(columnIndex);
    }

    @Override
    public boolean moveToNext() {
        return cursor.moveToNext();
    }

    @Override
    public boolean moveToFirst() {
        return cursor.moveToFirst();
    }
}
