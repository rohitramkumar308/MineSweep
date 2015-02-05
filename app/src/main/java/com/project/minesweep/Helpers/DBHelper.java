package com.project.minesweep.Helpers;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String TABLE_NAME = "highscore";
    public static final String COLUMN_GRIDSIZE = "gridsize";
    public static final String COLUMN_PLACE = "place";
    public static final String COLUMN_TIME = "time";


    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE "
                        + TABLE_NAME
                        + "("
                        + COLUMN_GRIDSIZE + " integer not null, "
                        + COLUMN_PLACE + " integer not null, "
                        + COLUMN_TIME + " integer not null,"
                        + "PRIMARY KEY (" + COLUMN_GRIDSIZE + " , " + COLUMN_PLACE + " ) );"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertHighScore(int gridsize, int place, int time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_GRIDSIZE, gridsize);
        contentValues.put(COLUMN_PLACE, place);
        contentValues.put(COLUMN_TIME, time);

        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int gridsize) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where " + COLUMN_GRIDSIZE + "=" + gridsize, null);
        return res;
    }

    public Cursor getData(int gridsize, int place) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where " + COLUMN_GRIDSIZE + "=" + gridsize +
                " AND " + COLUMN_PLACE + " = " + place, null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateHighScore(int gridsize, int place, int time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GRIDSIZE, gridsize);
        contentValues.put(COLUMN_PLACE, place);
        contentValues.put(COLUMN_TIME, time);
        db.update(TABLE_NAME, contentValues, COLUMN_GRIDSIZE + " = ? and " + COLUMN_PLACE + "=?",
                new String[]{Integer.toString(gridsize), Integer.toString(place)});

        return true;
    }


}