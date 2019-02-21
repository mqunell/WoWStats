package com.mattqunell.wowstats;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.mattqunell.wowstats.data.BlizzardConnection;
import com.mattqunell.wowstats.data.Toon;

import java.util.HashMap;

/*
 * Helper class for displaying a Toon's data based on SharedPreferences or specific attributes.
 *
 * ToonFormatter is implemented as a singleton, which is a class that allows only one instance of
 * itself to be created. It exists as long as the application is in memory and is available through
 * lifecycle changes in activities and fragments. Singletons allow data to be easily passed between
 * controller classes, but should not be used for everything or as long-term storage solutions.
 */
public class ToonFormatter {

    // SharedPreferences keys
    public static final String TOP_LEFT_ONE = "top_left_one",
            TOP_LEFT_TWO = "top_left_two",
            TOP_RIGHT = "top_right",
            BOTTOM_LEFT = "bottom_left",
            BOTTOM_RIGHT = "bottom_right",
            FLIPPED_COLORS = "flipped_colors";

    // Static variables, including the one and only instance of this class
    private static ToonFormatter sToonFormatter;
    private static HashMap<String, Drawable> sClassIcons;
    private static HashMap<String, Integer> sClassColors;

    // Direct references to SharedPreferences and Resources to reduce/simplify overall code
    private SharedPreferences mSharedPrefs;
    private Resources mResources;

    /**
     * Static getter that (creates if necessary and) returns the single instance of ToonFormatter
     *
     * @param parentFragment A reference to ToonListFragment
     * @return sToonFormatter
     */
    public static ToonFormatter get(Fragment parentFragment) {
        if (sToonFormatter == null) {
            sToonFormatter = new ToonFormatter(parentFragment);
        }

        return sToonFormatter;
    }

    /**
     * Returns the appropriate output based on SharedPreferences data.
     * ex: sharedPrefsKey == "top_left_one"
     *     if "top_left_one" is set to "Name", return the Toon's name
     *
     * @param toon The Toon whose layout is currently being set
     * @param sharedPrefsKey The key to the value needing to be retrieved
     * @return The value to be set into a TextView
     */
    public String getTextviewOutput(Toon toon, String sharedPrefsKey) {
        String spValue = mSharedPrefs.getString(sharedPrefsKey, "");

        switch (spValue) {
            case "Class":
                return toon.get_Class();

            case "Name":
                return toon.getName();

            case "Race":
                return toon.getRace();

            case "Realm":
                return toon.getRealm();

            case "Faction":
                return toon.getFaction() == 0 ? "Alliance" : "Horde";

            case "Level/iLevel":
                return mResources.getString(R.string.level_ilevel,
                        String.valueOf(toon.getLevel()), String.valueOf(toon.getItemLevel()));

            case "Mythic+":
                return toon.getLevel() == BlizzardConnection.MAX_LEVEL ?
                        mResources.getString(R.string.mythicscore_highestmythic, toon.getMythicScore(), toon.getHighestMythic()) : "Not max level";

            default: // "--Empty--" case
                return "";
        }
    }

    // Returns the Color (int) for the Toon's class
    public int getClassColor(String _class) {
        return sClassColors.get(_class);
    }

    // Returns the Drawable for the Toon's class
    public Drawable getClassIcon(String _class) {
        return sClassIcons.get(_class);
    }

    // Returns the Color (int) for a faction (Alliance = 0, Horde = 1)
    public int getFactionColor(int faction) {
        return faction == 0 ?
                mResources.getColor(R.color.alliance) :
                mResources.getColor(R.color.horde);
    }

    // Returns the Drawable for a faction (Alliance = 0, Horde = 1)
    public Drawable getFactionIcon(int faction) {
        return faction == 0 ?
                mResources.getDrawable(R.drawable.alliance) :
                mResources.getDrawable(R.drawable.horde);
    }

    // Private constructor to inhibit instantiation
    private ToonFormatter(Fragment parentFragment) {
        mSharedPrefs = parentFragment.getContext().getSharedPreferences(
                parentFragment.getString(R.string.app_name), Context.MODE_PRIVATE);

        mResources = parentFragment.getResources();

        sClassIcons = new HashMap<>();
        sClassIcons.put("Death Knight", mResources.getDrawable(R.drawable.death_knight));
        sClassIcons.put("Demon Hunter", mResources.getDrawable(R.drawable.demon_hunter));
        sClassIcons.put("Druid", mResources.getDrawable(R.drawable.druid));
        sClassIcons.put("Hunter", mResources.getDrawable(R.drawable.hunter));
        sClassIcons.put("Mage", mResources.getDrawable(R.drawable.mage));
        sClassIcons.put("Monk", mResources.getDrawable(R.drawable.monk));
        sClassIcons.put("Paladin", mResources.getDrawable(R.drawable.paladin));
        sClassIcons.put("Priest", mResources.getDrawable(R.drawable.priest));
        sClassIcons.put("Rogue", mResources.getDrawable(R.drawable.rogue));
        sClassIcons.put("Shaman", mResources.getDrawable(R.drawable.shaman));
        sClassIcons.put("Warlock", mResources.getDrawable(R.drawable.warlock));
        sClassIcons.put("Warrior", mResources.getDrawable(R.drawable.warrior));

        sClassColors = new HashMap<>();
        sClassColors.put("Death Knight", mResources.getColor(R.color.death_knight));
        sClassColors.put("Demon Hunter", mResources.getColor(R.color.demon_hunter));
        sClassColors.put("Druid", mResources.getColor(R.color.druid));
        sClassColors.put("Hunter", mResources.getColor(R.color.hunter));
        sClassColors.put("Mage", mResources.getColor(R.color.mage));
        sClassColors.put("Monk", mResources.getColor(R.color.monk));
        sClassColors.put("Paladin", mResources.getColor(R.color.paladin));
        sClassColors.put("Priest", mResources.getColor(R.color.priest));
        sClassColors.put("Rogue", mResources.getColor(R.color.rogue));
        sClassColors.put("Shaman", mResources.getColor(R.color.shaman));
        sClassColors.put("Warlock", mResources.getColor(R.color.warlock));
        sClassColors.put("Warrior", mResources.getColor(R.color.warrior));
    }
}
