package com.example.cliff.fitnessapp;

public abstract class QueryResult {
        public abstract int getInt(String column);
        public abstract String getString(String column);
        public abstract void moveToNext();
        public abstract void moveToFirst();
}
