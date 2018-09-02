package com.mattqunell.wowstats.database;

/**
 * Represents the database's schema.
 */
public final class ToonDbSchema {

    // Table name
    public static final String NAME = "toons";

    // Column names
    public static final class Cols {
        public static final String UUID = "uuid";
        public static final String NAME = "name";
        public static final String REALM = "realm";
        public static final String RACE = "race";
        public static final String _CLASS = "_class";
        public static final String LEVEL = "level";
        public static final String ITEMLEVEL = "itemlevel";
    }
}
