package com.smartlockinc.smartlocks;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SunnySingh on 7/16/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    private static final int version = 1;
    private static final String Database_Name = "GCM Manager";
    private static final String Table_Name = "GCM Messages";

    //private static final String Date = "date";
    //private static final String Time = "time";
    private static final String Message = "message";
    public DatabaseHandler(Context context) {
        super(context, Database_Name, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE GCMMessage  (_id INTEGER PRIMARY KEY AUTOINCREMENT, Message TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);

        // Create tables again
        onCreate(db);
    }
    void addMessage(String Msg) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("Message",Msg);
        db.insert(Table_Name, null, cv);

        db.close();
    }



}
