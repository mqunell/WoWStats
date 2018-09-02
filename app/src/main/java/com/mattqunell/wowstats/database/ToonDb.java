package com.mattqunell.wowstats.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mattqunell.wowstats.data.Toon;

import java.util.ArrayList;
import java.util.List;

// SEE RECEIPTS - ReceiptBook for the actual DB implementation guidelines
public class ToonDb {

    // The one class to use throughout the app
    private static ToonDb sToonDB;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    // Static getter that creates sToonDB if it doesn't exist and returns it
    public static ToonDb get(Context context) {
        if (sToonDB == null) {
            sToonDB = new ToonDb(context);
        }

        return sToonDB;
    }

    // Private constructor to limit instantiation
    private ToonDb(Context context) {
        mContext = context;
        mDatabase = new ToonDbHelper(mContext).getWritableDatabase();
    }

    // Adds a new Toon
    public void addToon(Toon toon) {
        ContentValues values = getContentValues(toon);
        mDatabase.insert(ToonDbSchema.NAME, null, values);
    }

    // Gets an ArrayList of all Toons
    public List<Toon> getToons() {
        List<Toon> toons = new ArrayList<>();

        ToonCursorWrapper cursor = queryToons(null, null);

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                toons.add(cursor.getToon());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return toons;
    }

    // Helper method that converts a Toon to a ContentValues
    private static ContentValues getContentValues(Toon toon) {

        // A ContentValues is a key-value class specifically designed for SQLite data
        ContentValues values = new ContentValues();

        //values.put(ToonDbSchema.Cols.UUID, toon.getUuid().toString());
        values.put(ToonDbSchema.Cols.NAME, toon.getName());
        values.put(ToonDbSchema.Cols.REALM, toon.getRealm());
        values.put(ToonDbSchema.Cols.RACE, toon.getRace());
        values.put(ToonDbSchema.Cols._CLASS, toon.get_Class());
        values.put(ToonDbSchema.Cols.LEVEL, toon.getLevel());
        values.put(ToonDbSchema.Cols.ITEMLEVEL, toon.getItemLevel());

        return values;
    }

    // Helper method that searches for a Toon in the database
    private ToonCursorWrapper queryToons(String whereClause, String[] whereArgs) {

        // Args: table, columns, where, whereArgs, groupBy, having, orderBy
        Cursor cursor = mDatabase.query(
                ToonDbSchema.NAME,
                null,  // null selects all
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ToonCursorWrapper(cursor);
    }
}