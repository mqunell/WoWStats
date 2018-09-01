package com.mattqunell.wowstats.data;

import java.util.HashMap;
import java.util.Map;

public class Toon {

    private String mName;
    private String mRealm;
    private String mRaceName;
    private String mClassName;
    private int mLevel;
    private int mItemLevel;

    private static final Map<Integer, String> RACES;
    private static final Map<Integer, String> CLASSES;

    static {
        RACES = new HashMap<>();
        RACES.put(1, "Human");
        RACES.put(2, "Orc");
        RACES.put(3, "Dwarf");
        RACES.put(4, "Night Elf");
        RACES.put(5, "Undead");
        RACES.put(6, "Tauren");
        RACES.put(7, "Gnome");
        RACES.put(8, "Troll");
        RACES.put(9, "Goblin");
        RACES.put(10, "Blood Elf");
        RACES.put(11, "Draenei");
        RACES.put(22, "Worgen");
        RACES.put(25, "A. Pandaren");
        RACES.put(26, "H. Pandaren");

        CLASSES = new HashMap<>();
        CLASSES.put(1, "Warrior");
        CLASSES.put(2, "Paladin");
        CLASSES.put(3, "Hunter");
        CLASSES.put(4, "Rogue");
        CLASSES.put(5, "Priest");
        CLASSES.put(6, "Death Knight");
        CLASSES.put(7, "Shaman");
        CLASSES.put(8, "Mage");
        CLASSES.put(9, "Warlock");
        CLASSES.put(10, "Monk");
        CLASSES.put(11, "Druid");
        CLASSES.put(12, "Demon Hunter");
    }

    public Toon(String name, String realm, int raceNum, int classNum, int level, int itemLevel) {
        mName = name;
        mRealm = realm;
        mRaceName = RACES.get(raceNum);
        mClassName = CLASSES.get(classNum);
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

    public String getRaceName() {
        return mRaceName;
    }

    public void setRaceName(String raceName) {
        mRaceName = raceName;
    }

    public String getClassName() {
        return mClassName;
    }

    public void setClass(String className) {
        mClassName = className;
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
        return mName + "-" + mRealm + ". Level " + mLevel + " " + mRaceName + " " + mClassName +
                ". Item level " + mItemLevel + ".";
    }
}
