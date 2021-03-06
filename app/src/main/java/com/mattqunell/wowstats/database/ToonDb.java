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

    /*
     * Adds a new Toon or updates an existing one.
     * Returns a boolean signifying whether an existing Toon was updated.
     *
     * If the Toon already exists in the database, update the necessary info.
     * ex. update toons set level=120, itemlevel=350, mythicscore=500, highestmythic=10
     *     where name="Blyskyn" and realm="Shadowsong";
     *
     */
    public boolean addToon(Toon toon) {
        boolean wasUpdated = false;

        // Attempt to find and update a Toon
        for (Toon t : getToons()) {
            if (t.getName().equals(toon.getName()) && t.getRealm().equals(toon.getRealm())) {
                String updateSql = String.format(mContext.getString(R.string.sql_update),
                        ToonDbSchema.NAME,
                        ToonDbSchema.Cols.LEVEL, toon.getLevel(),
                        ToonDbSchema.Cols.ITEMLEVEL, toon.getItemLevel(),
                        ToonDbSchema.Cols.MYTHICSCORE, toon.getMythicScore(),
                        ToonDbSchema.Cols.HIGHESTMYTHIC, toon.getHighestMythic(),
                        ToonDbSchema.Cols.NAME, toon.getName(),
                        ToonDbSchema.Cols.REALM, toon.getRealm());
                mDatabase.execSQL(updateSql);
                wasUpdated = true;
            }
        }

        // Add the Toon if not previously found
        if (!wasUpdated) {
            ContentValues values = getContentValues(toon);
            mDatabase.insert(ToonDbSchema.NAME, null, values);
        }

        return wasUpdated;
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

    // Converts a Toon to a ContentValues (key-value class specifically designed for SQLite data)
    private static ContentValues getContentValues(Toon toon) {
        ContentValues values = new ContentValues();

        values.put(ToonDbSchema.Cols.NAME, toon.getName());
        values.put(ToonDbSchema.Cols.REALM, toon.getRealm());
        values.put(ToonDbSchema.Cols.FACTION, toon.getFaction());
        values.put(ToonDbSchema.Cols.RACE, toon.getRace());
        values.put(ToonDbSchema.Cols._CLASS, toon.get_Class());
        values.put(ToonDbSchema.Cols.LEVEL, toon.getLevel());
        values.put(ToonDbSchema.Cols.ITEMLEVEL, toon.getItemLevel());
        values.put(ToonDbSchema.Cols.MYTHICSCORE, toon.getMythicScore());
        values.put(ToonDbSchema.Cols.HIGHESTMYTHIC, toon.getHighestMythic());

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