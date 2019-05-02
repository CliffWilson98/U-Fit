package com.example.cliff.fitnessapp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AndroidDatabase extends Database {
    private SQLiteDatabase sqliteDatabase = null;

    @Override
    public DatabaseValues newValues() {
        return new AndroidDatabaseValues();
    }

    private AndroidDatabase(SQLiteOpenHelper sqLiteOpenHelper) {
        sqliteDatabase = sqLiteOpenHelper.getReadableDatabase();
    }

    public static void initializeDatabase(SQLiteOpenHelper sqLiteOpenHelper) {
        theDatabase = new AndroidDatabase(sqLiteOpenHelper);
    }

    @Override
    public void update(String table, DatabaseValues values, String whereClause, String[] whereArgs) {
        sqliteDatabase.update(table, AndroidDatabaseValues.toContentValues(values), whereClause, whereArgs);
    }

    @Override
    public QueryResult query(String query) {
        return new AndroidQueryResult(sqliteDatabase.rawQuery(query, null));
    }

    @Override
    public QueryResult query(String query, String[] selectionArgs) {
        return new AndroidQueryResult(sqliteDatabase.rawQuery(query, selectionArgs));
    }

    @Override
    public void insert(String column, DatabaseValues values) {
        sqliteDatabase.insert(column, null, AndroidDatabaseValues.toContentValues(values));
    }

    @Override
    public void delete(String table, String whereClause) {
        sqliteDatabase.delete(table, whereClause, null);
    }
}