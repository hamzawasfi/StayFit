package com.example.myapplication.DataBase.users;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.myapplication.Utils.Utils;

public class UsersDataBase extends DataBase{

    private static int userID;
    private static String userCalories, userPassword;

    public UsersDataBase(@Nullable Context context) {
        super(context);
    }

    public boolean insertData(String username, String password, int calories){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1_USERNAME, username);
        contentValues.put(COL2_PASSWORD, password);
        contentValues.put(COL3_USER_GOAL_CAL, calories);
        long result = db.insert(USERS_TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateData(String username, String userCal){
        SQLiteDatabase db = this.getWritableDatabase();
        /*ContentValues contentValues = new ContentValues();
        contentValues.put(COL1_USERNAME, username);
        contentValues.put(COL2_PASSWORD, getPassword(username));
        contentValues.put(COL3_USER_GOAL_CAL, getCalories(username));
        contentValues.put(COL4_USERCAL, goalCalories);
        db.update(USERS_TABLE_NAME, contentValues, COL3_USER_GOAL_CAL + " = ? " + username, new String[]{goalCalories});*/
        String password = getPassword(username);
        String goalCalories = getCalories(username);
        String query = "UPDATE " + USERS_TABLE_NAME + " SET " + COL1_USERNAME + " = " + username + ", SET " + COL2_PASSWORD + " = " + password + ", SET " + COL3_USER_GOAL_CAL + " = " + goalCalories + ", SET " + " = " + userCal + ", WHERE " + COL1_USERNAME + " = " + username;
        db.rawQuery(query, new String[]{});
        return true;
    }


    public Cursor getData(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] data = {COL1_USERNAME, COL2_PASSWORD, COL3_USER_GOAL_CAL};
        String selection = COL1_USERNAME + " LIKE ?";
        String[] selection_args = {username};
        Cursor cursor = db.query(USERS_TABLE_NAME, data, selection, selection_args, null, null, null);
        return cursor;
    }

    public String getCalories(String username){
        String calories = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor  = getData(username);
        if(cursor.moveToFirst()){
            calories = cursor.getString(2);
        }
        userCalories = calories;
        return calories;
    }

    public int getCurrentCalories(String username){
        int calories = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor  = getData(username);
        if(cursor.moveToFirst()){
            calories = Integer.parseInt(cursor.getString(3));
        }
        userCalories = String.valueOf(calories);
        return calories;
    }

    public String getPassword(String username){
        String password = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor  = getData(username);
        if(cursor.moveToFirst()){
            password = cursor.getString(1);
        }
        userPassword = password;
        return password;
    }

    public boolean match(String username, String password){
        String userPassword = getPassword(username);
        if(userPassword.equals(password)){
            return true;
        }else{
            return false;
        }
    }

    public boolean usernameExist(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] data = {COL1_USERNAME, COL2_PASSWORD, COL3_USER_GOAL_CAL};
        String selection = COL1_USERNAME + " LIKE ?";
        String[] selection_args = {username};
        Cursor cursor = db.query(USERS_TABLE_NAME, data, selection, selection_args, null, null, null);
        if(cursor.moveToFirst()){
            return true;
        }else {
            return false;
        }
    }
}
