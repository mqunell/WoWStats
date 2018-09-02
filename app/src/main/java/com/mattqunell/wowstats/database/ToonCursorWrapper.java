package com.mattqunell.wowstats.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.mattqunell.wowstats.data.Toon;

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
        // todo: set up Toon with a UUID
        String uuid = getString(getColumnIndex(ToonDbSchema.Cols.UUID));
        String name = getString(getColumnIndex(ToonDbSchema.Cols.NAME));
        String realm = getString(getColumnIndex(ToonDbSchema.Cols.REALM));
        String race = getString(getColumnIndex(ToonDbSchema.Cols.RACE));
        String _class = getString(getColumnIndex(ToonDbSchema.Cols._CLASS));
        int level = getInt(getColumnIndex(ToonDbSchema.Cols.LEVEL));
        int itemLevel = getInt(getColumnIndex(ToonDbSchema.Cols.ITEMLEVEL));

        return new Toon(name, realm, race, _class, level, itemLevel);
    }
}
