package com.example.cliff.fitnessapp;

public interface QueryResult {
        public int getInt(int columnIndex);
        public int getInt(String columnName);
        public String getString(int columnIndex);
        public String getString(String columnName);
        public int getCount();
        public boolean moveToNext();
        public boolean moveToFirst();
}
