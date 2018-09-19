package com.mattqunell.wowstats.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.mattqunell.wowstats.data.Toon;

/**
 * ToonCursorWrapper is used to traverse the database through CursorWrapper's functionality.
 * getToon() parses and returns the Toon at TCW's current location in the database.
 */
public class ToonCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ToonCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    // Parses a Toon from the database
    public Toon getToon() {
        String name = getString(getColumnIndex(ToonDbSchema.Cols.NAME));
        String realm = getString(getColumnIndex(ToonDbSchema.Cols.REALM));
        int faction = getInt(getColumnIndex(ToonDbSchema.Cols.FACTION));
        String race = getString(getColumnIndex(ToonDbSchema.Cols.RACE));
        String _class = getString(getColumnIndex(ToonDbSchema.Cols._CLASS));
        int level = getInt(getColumnIndex(ToonDbSchema.Cols.LEVEL));
        int itemLevel = getInt(getColumnIndex(ToonDbSchema.Cols.ITEMLEVEL));
        int mythicScore = getInt(getColumnIndex(ToonDbSchema.Cols.MYTHICSCORE));
        int highestMythic = getInt(getColumnIndex(ToonDbSchema.Cols.HIGHESTMYTHIC));

        return new Toon(name, realm, faction, race, _class, level, itemLevel, mythicScore, highestMythic);
    }
}
