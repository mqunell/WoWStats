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
    private int mMythicScore;
    private int mHighestMythic;

    public Toon(String name, String realm, int faction, String race, String _class, int level, int itemLevel, int mythicScore, int highestMythic) {
        mName = name;
        mRealm = realm;
        mFaction = faction;
        mRace = race;
        m_Class = _class;
        mLevel = level;
        mItemLevel = itemLevel;
        mMythicScore = mythicScore;
        mHighestMythic = highestMythic;
    }

    public String getName() {
        return mName;
    }

    public String getRealm() {
        return mRealm;
    }

    public int getFaction() {
        return mFaction;
    }

    public String getRace() {
        return mRace;
    }

    public String get_Class() {
        return m_Class;
    }

    public int getLevel() {
        return mLevel;
    }

    public int getItemLevel() {
        return mItemLevel;
    }

    public int getMythicScore() {
        return mMythicScore;
    }

    public void setMythicScore(int mythicScore) {
        mMythicScore = mythicScore;
    }

    public int getHighestMythic() {
        return mHighestMythic;
    }

    public void setHighestMythic(int highestMythic) {
        mHighestMythic = highestMythic;
    }

    public String toString() {
        return "Name: " + mName
                + " Realm: " + mRealm
                + " Faction: " + mFaction
                + " Race: " + mRace
                + " Class: " + m_Class
                + " Level: " + mLevel
                + " iLevel: " + mItemLevel
                + " MythicScore: " + mMythicScore
                + " HighestMythic: " + mHighestMythic;
    }
}
