package com.mattqunell.wowstats.data;

/**
 * Represents a Toon.
 */
public class Toon {

    private String mName;
    private String mRealm;
    private int mFaction;  // Alliance = 0, Horde = 1
    private String mRace;
    private String m_Class;
    private int mLevel;
    private int mItemLevel;

    public Toon(String name, String realm, int faction, String race, String _class, int level, int itemLevel) {
        mName = name;
        mRealm = realm;
        mFaction = faction;
        mRace = race;
        m_Class = _class;
        mLevel = level;
        mItemLevel = itemLevel;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getRealm() {
        return mRealm;
    }

    public void setRealm(String realm) {
        mRealm = realm;
    }

    public int getFaction() {
        return mFaction;
    }

    public void setFaction(int faction) {
        mFaction = faction;
    }

    public String getRace() {
        return mRace;
    }

    public void setRace(String raceName) {
        mRace = raceName;
    }

    public String get_Class() {
        return m_Class;
    }

    public void setClass(String className) {
        m_Class = className;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public int getItemLevel() {
        return mItemLevel;
    }

    public void setItemLevel(int itemLevel) {
        mItemLevel = itemLevel;
    }

    public String toString() {
        return mName + "-" + mRealm + ". Level " + mLevel + " " + mRace + " " + m_Class +
                ". Item level " + mItemLevel + ".";
    }
}
