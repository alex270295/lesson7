package com.example.RSSReader_v3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChannelsDataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dbchannels";
    public static final int DATABASE_VERSION = 1;
    public static final String ID = "_id";
    public static final String URL = "url";
    public static final String NAME = "name";
    public static final String CREATE_DATABASE = "CREATE TABLE " + DATABASE_NAME
            + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NAME + " TEXT," + URL + " TEXT);";
    public static final String DROP_DATABASE = "DROP TABLE IF EXISTS " + DATABASE_NAME;


    public ChannelsDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL(DROP_DATABASE);
            onCreate(db);
        }
    }
}
