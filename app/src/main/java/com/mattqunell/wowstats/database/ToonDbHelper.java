package com.mattqunell.wowstats.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * When an instance of ToonDbHelper is created, the super(...) call:
 *      1. Creates the database and sets the version number (onCreate)
 *      2. Upgrades the database (onUpgrade)
 *      3. Opens the database
 */
public class ToonDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "toonDB.db";

    public ToonDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the table
        db.execSQL("create table " + ToonDbSchema.NAME + "(" +
                " _id integer primary key autoincrement, "
                + ToonDbSchema.Cols.UUID   + ", "
                + ToonDbSchema.Cols.NAME   + ", "
                + ToonDbSchema.Cols.REALM  + ", "
                + ToonDbSchema.Cols.RACE   + ", "
                + ToonDbSchema.Cols._CLASS + ", "
                + ToonDbSchema.Cols.LEVEL  + ", "
                + ToonDbSchema.Cols.ITEMLEVEL
                + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Intentionally left blank
    }
}
