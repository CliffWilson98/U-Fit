package com.example.cliff.fitnessapp;

public abstract class Database {
    protected static Database theDatabase = null;

    public static Database getDatabase() {
        return theDatabase;
    }
    public abstract QueryResult query(String query);
    public abstract QueryResult query(String query, String[] selectionArgs);
    public abstract void insert(String column, DatabaseValues values);
    public abstract void update(String table, DatabaseValues values, String whereClause, String[] whereArgs);
    public abstract void delete(String table, String whereClause);
    public abstract DatabaseValues newValues();
}