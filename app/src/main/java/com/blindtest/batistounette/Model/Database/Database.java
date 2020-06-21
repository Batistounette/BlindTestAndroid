package com.blindtest.batistounette.Model.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blindtest.batistounette.Model.User.UserManager;

/**
 * Created by Batistounette on 15/05/2020.
 */

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BlindTest.db";
    private static final int DATABASE_VERSION = 1;
    private static Database sInstance;

    private Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized Database getInstance(Context context) {
        if (sInstance == null) {sInstance = new Database(context);}
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserManager.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}