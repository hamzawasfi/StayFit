package com.example.myapplication.DataBase.users;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.Activities.Home.Home;
import com.example.myapplication.Utils.Utils;

public class DaysDataBase extends DataBase {

    public DaysDataBase(@Nullable Context context) {
        super(context);
    }

    //insert on adding

    public boolean insertData(String username, String foodID, String foodName, double calories, int weight, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL9_USERNAME, username);
        contentValues.put(COL10_FOOD_ID, foodID);
        contentValues.put(COL11_FOOD_NAME, foodName);
        contentValues.put(COL12_FOOD_CAL, (int)calories);
        contentValues.put(COL13_FOOD_WEIGHT, weight);
        contentValues.put(COL14_DATE, date);
        long result = db.insert(DAY_TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public void deleteRow(String username, String foodID, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DAY_TABLE_NAME + " WHERE " + COL9_USERNAME + " = ? AND " + COL11_FOOD_NAME + " = ? AND " + COL14_DATE + " = ?", new String[] {username, foodID, date});
    }

    //

    //get on create

    public Cursor getAddData(String username, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + DAY_TABLE_NAME + " where " + COL14_DATE + " = ? and " + COL9_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[] {date, username});
        return cursor;
    }

    //

    //get on resume

    public Cursor getAddedData(String foodName, String username, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + DAY_TABLE_NAME + " WHERE " + COL11_FOOD_NAME + " = ? and " + COL9_USERNAME + " = ? and " + COL14_DATE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{foodName, username, date});
        return cursor;
    }

    //
}
