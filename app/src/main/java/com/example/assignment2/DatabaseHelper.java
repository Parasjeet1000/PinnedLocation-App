package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "assignment2.db";
    private static final int DB_VERSION = 1;
     static final String TABLE_NAME = "location";
     static final String ID_COL = "id";
     static final String ADDRESS_COL = "address";
     static final String LAT_COL = "latitude";
     static final String LONG_COL = "longitude";




    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating an sqlite query and we are setting our column names along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ADDRESS_COL + " TEXT,"
                + LAT_COL + " REAL,"
                + LONG_COL + " REAL)";
        // method to execute above sql query
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
public Boolean insert(String address, double latitude, double longitude){
    SQLiteDatabase db = this.getWritableDatabase();
    // on below line we are creating a variable for content values.
    ContentValues values = new ContentValues();

    values.put(ADDRESS_COL, address);
    values.put(LAT_COL, latitude);
    values.put(LONG_COL, longitude);
    long result = db.insert(TABLE_NAME, null, values);
    db.close();
    if(result == -1){
        return false;
    }
    else{
        return true;
    }
}
    public Boolean deleteLocation(double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = LAT_COL + " = ? AND " + LONG_COL + " = ?";
        String[] selectionArgs = {String.valueOf(latitude), String.valueOf(longitude)};
        long deletedRows = db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();
        if(deletedRows == -1){
            return false;
        }
        else{
            return true;
        }
    }



}
