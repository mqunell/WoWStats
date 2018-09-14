package com.mattqunell.wowstats.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mattqunell.wowstats.R;
import com.mattqunell.wowstats.data.Toon;

import java.util.ArrayList;
import java.util.List;

/**
 * ToonDb connects Activities/Fragments to the database. Only one instance of it can be created.
 */
public class ToonDb {

    // The one class to use throughout the app
    private static ToonDb sToonDb;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    // Static getter that creates sToonDb if it doesn't exist and returns it
    public static ToonDb get(Context context) {
        if (sToonDb == null) {
            sToonDb = new ToonDb(context);
        }

        return sToonDb;
    }

    // Private constructor to limit instantiation
    private ToonDb(Context context) {
        mContext = context;
        mDatabase = new ToonDbHelper(mContext).getWritableDatabase();
    }

    // Adds a new Toon or updates an existing one
    public void addToon(Toon toon) {
        Boolean updated = false;

        // If the Toon is already in the database, update its level and item level
        for (Toon t : getToons()) {
            if (t.getName().equals(toon.getName()) && t.getRealm().equals(toon.getRealm())) {
                // ex. update toons set level=120, itemlevel=350 where name="Blyskyn" and realm="Shadowsong";
                String updateSql = String.format(mContext.getString(R.string.sql_update),
                        ToonDbSchema.NAME,
                        ToonDbSchema.Cols.LEVEL, toon.getLevel(),
                        ToonDbSchema.Cols.ITEMLEVEL, toon.getItemLevel(),
                        ToonDbSchema.Cols.NAME, toon.getName(),
                        ToonDbSchema.Cols.REALM, toon.getRealm());
                mDatabase.execSQL(updateSql);
                updated = true;
            }
        }

        // If it wasn't found, add it
        if (!updated) {
            ContentValues values = getContentValues(toon);
            mDatabase.insert(ToonDbSchema.NAME, null, values);
        }
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