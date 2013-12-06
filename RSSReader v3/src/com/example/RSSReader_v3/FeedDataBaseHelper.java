package com.example.RSSReader_v3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Alexei
 * Date: 05.12.13
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */
public class FeedDataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dbchannels";
    public static final int DATABASE_VERSION = 1;
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";
    public static final String DATE = "date";

    String name;


    static String prepair(String source) {
        String result = "";
        source = source.toLowerCase();
        for (int i = 0; i < source.length(); i++)
            if (Character.isLetter(source.charAt(i)))
                result += source.charAt(i);
        return result;
    }

    public String createDatabase() {
        return "CREATE TABLE " + DATABASE_NAME + name + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TITLE + " TEXT," + DESCRIPTION + " TEXT," + DATE + " TEXT," + LINK + " TEXT);";
    }

    public String dropDatabase() {
        return "DROP TABLE IF EXISTS " + DATABASE_NAME + name;
    }

    public String dataBaseName() {
        return DATABASE_NAME + name;
    }


    public FeedDataBaseHelper(Context context, String name) {
        super(context, DATABASE_NAME + prepair(name), null, DATABASE_VERSION);
        this.name = prepair(name);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createDatabase());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL(dropDatabase());
            onCreate(db);
        }
    }

}
