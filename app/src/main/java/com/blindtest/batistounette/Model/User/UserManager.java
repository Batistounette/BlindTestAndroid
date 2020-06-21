package com.blindtest.batistounette.Model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blindtest.batistounette.Model.Database.Database;

/**
 * Created by Batistounette on 15/05/2020.
 */

public class UserManager {

    private static final String TABLE_NAME = "Utilisateurs";
    public static final String USER_ID = "ID";
    public static final String USER_FULL_NAME = "NOM";
    public static final String USER_NAME = "IDENTIFIANT";
    public static final String USER_EMAIL = "EMAIL";
    public static final String USER_PASSWORD = "MOT_DE_PASSE";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" +
            " " + USER_ID + " INTEGER PRIMARY KEY," +
            " " + USER_FULL_NAME + " TEXT," +
            " " + USER_NAME + " TEXT," +
            " " + USER_EMAIL + " TEXT," +
            " " + USER_PASSWORD + " TEXT" +
            ");";
    private Database mSQLiteBase;
    private SQLiteDatabase mDB;

    public UserManager(Context context) {
        mSQLiteBase = Database.getInstance(context);
    }

    public void openDatabase() {
        mDB = mSQLiteBase.getWritableDatabase();
    }

    public void closeDataBase() {
        mDB.close();
    }

    public long addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(USER_FULL_NAME, user.getFullname());
        values.put(USER_NAME, user.getUsername());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PASSWORD, user.getPassword());

        return mDB.insert(TABLE_NAME, null, values);
    }

    public int editUser(User user) {
        ContentValues values = new ContentValues();
        values.put(USER_FULL_NAME, user.getFullname());
        values.put(USER_NAME, user.getUsername());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PASSWORD, user.getPassword());

        String position = USER_ID + " = ?";
        String[] positionArgs = {user.getID() + ""};

        return mDB.update(TABLE_NAME, values, position, positionArgs);
    }

    public int removeUser(User user) {
        String position = USER_ID + " = ?";
        String[] positionArgs = {user.getID() + ""};

        return mDB.delete(TABLE_NAME, position, positionArgs);
    }

    public User getUser(int id) {
        User user = new User(0, "", "", "", "");

        Cursor c = mDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + USER_ID + " = " + id, null);

        if (c.moveToFirst()) {
            user.setID(c.getInt(c.getColumnIndex(USER_ID)));
            user.setFullname(c.getString(c.getColumnIndex(USER_FULL_NAME)));
            user.setUsername(c.getString(c.getColumnIndex(USER_NAME)));
            user.setEmail(c.getString(c.getColumnIndex(USER_EMAIL)));
            user.setPassword(c.getString(c.getColumnIndex(USER_PASSWORD)));
            c.close();
        }

        return user;
    }

    public Cursor getTable() {
        return mDB.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}