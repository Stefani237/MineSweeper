package com.example.dell.minesweeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "records.db";
    private static final String TABLE_NAME = "record_table";
    private static final String col_1 = "ID";
    private static final String col_2 = "NAME";
    private static final String col_3 = "TIME";
    private static final String col_4 = "LAT";
    private static final String col_5 = "LNG";
    private static final String col_6 = "DIFFICULTY";
    private int difficulty;
    private Cursor cursor;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 14);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT , TIME INTEGER , LAT DOUBLE , LNG DOUBLE , DIFFICULTY INTEGER)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, int time, double lat, double lng, int difficulty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2, name);
        contentValues.put(col_3, time);
        contentValues.put(col_4, lat);
        contentValues.put(col_5, lng);
        contentValues.put(col_6, difficulty);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(int difficulty) {
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery("select * from "+ TABLE_NAME + " where " + col_6  +
                 " = " + difficulty + " order by " + col_3 + " asc", null);
        return cursor;
    }



    public Integer deleteData (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, col_1 + " = " + id, null);
    }



    public boolean isEmpty() {
        cursor = this.getAllData(difficulty);
        if (cursor.getCount() == 0) {
            return true;
        }
        return false;
    }

}
