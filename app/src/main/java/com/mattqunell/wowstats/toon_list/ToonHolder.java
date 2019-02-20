package com.mattqunell.wowstats.toon_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mattqunell.wowstats.CustomizeActivity;
import com.mattqunell.wowstats.R;
import com.mattqunell.wowstats.data.BlizzardConnection;
import com.mattqunell.wowstats.data.Toon;

import java.util.HashMap;
import java.util.Map;

/**
 * ToonHolder: The ViewHolder
 * Inflates and owns each individual layout (fragment_toon_item) within the RecyclerView.
 * The bind(Toon) method is called each time a new Toon should be displayed.
 */
public class ToonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Reference the SharedPreferences and Resources directly to reduce and simplify overall code
    private SharedPreferences mSharedPrefs;
    private Resources mResourses;

    // The specific Toon
    private Toon mToon;

    // UI elements
    private ConstraintLayout mLayout;
    private ImageView mIcon;
    private TextView mToonTopLeftOne;
    private TextView mToonTopLeftTwo;
    private TextView mToonTopRight;
    private TextView mToonBottomLeft;
    private TextView mToonBottomRight;

    public ToonHolder(Fragment parentFragment, LayoutInflater inflater, ViewGroup parentView) {
        super(inflater.inflate(R.layout.fragment_toon_item, parentView, false));

        // Set mSharedPrefs and mResources using parentFragment
        mSharedPrefs = parentFragment.getContext().getSharedPreferences(
                parentFragment.getString(R.string.app_name), Context.MODE_PRIVATE);
        mResourses = parentFragment.getResources();

        itemView.setOnClickListener(this);

        // UI elements
        mLayout = itemView.findViewById(R.id.toon_item);
        mIcon = itemView.findViewById(R.id.icon);
        mToonTopLeftOne = itemView.findViewById(R.id.toon_top_left_one);
        mToonTopLeftTwo = itemView.findViewById(R.id.toon_top_left_two);
        mToonTopRight = itemView.findViewById(R.id.toon_top_right);
        mToonBottomLeft = itemView.findViewById(R.id.toon_bottom_left);
        mToonBottomRight = itemView.findViewById(R.id.toon_bottom_right);
    }

    // Sets the individual layout's elements
    public void bind(Toon toon) {
        mToon = toon;

        // Set the TextViews
        mToonTopLeftOne.setText(getOutput(CustomizeActivity.TOP_LEFT_ONE));
        mToonTopLeftTwo.setText(getOutput(CustomizeActivity.TOP_LEFT_TWO));
        mToonTopRight.setText(getOutput(CustomizeActivity.TOP_RIGHT));
        mToonBottomLeft.setText(getOutput(CustomizeActivity.BOTTOM_LEFT));
        mToonBottomRight.setText(getOutput(CustomizeActivity.BOTTOM_RIGHT));

        // Set the ImageView and background color
        Boolean flippedColors = mSharedPrefs.getBoolean(CustomizeActivity.FLIPPED_COLORS, false);

        if (!flippedColors) {
            mIcon.setImageDrawable(getClassIcon(mToon.get_Class()));

            mLayout.setBackgroundColor(mToon.getFaction() == 0 ?
                    mResourses.getColor(R.color.alliance) :
                    mResourses.getColor(R.color.horde));
        }
        else {
            mIcon.setImageDrawable(mToon.getFaction() == 0 ?
                    mResourses.getDrawable(R.drawable.alliance) :
                    mResourses.getDrawable(R.drawable.horde));

            mLayout.setBackgroundColor(getClassColor(mToon.get_Class()));
        }
    }

    @Override
    public void onClick(View view) {
        // Start an activity for that specific character
    }

    // Helper method that gets the appropriate output based on SharedPreferences data
    private String getOutput(String sharedPrefsKey) {
        Map<String, String> output = new HashMap<>();
        output.put("Class", mToon.get_Class());
        output.put("Name", mToon.getName());
        output.put("Race", mToon.getRace());
        output.put("Realm", mToon.getRealm());

        output.put("Faction", mToon.getFaction() == 0 ? "Alliance" : "Horde");

        output.put("Level/iLevel", mResourses.getString(R.string.level_ilevel,
                String.valueOf(mToon.getLevel()), String.valueOf(mToon.getItemLevel())));

        output.put("Mythic+", mToon.getLevel() == BlizzardConnection.MAX_LEVEL ?
                mResourses.getString(R.string.mythicscore_highestmythic, mToon.getMythicScore(), mToon.getHighestMythic()) : "Not max level");

        String sharedPrefsValue = mSharedPrefs.getString(sharedPrefsKey, "");
        return output.get(sharedPrefsValue);
    }

    // Helper method that gets the correct Drawable based on the Toon's class
    private Drawable getClassIcon(String _class) {
        Map<String, Drawable> icons = new HashMap<>();
        icons.put("Death Knight", mResourses.getDrawable(R.drawable.death_knight));
        icons.put("Demon Hunter", mResourses.getDrawable(R.drawable.demon_hunter));
        icons.put("Druid", mResourses.getDrawable(R.drawable.druid));
        icons.put("Hunter", mResourses.getDrawable(R.drawable.hunter));
        icons.put("Mage", mResourses.getDrawable(R.drawable.mage));
        icons.put("Monk", mResourses.getDrawable(R.drawable.monk));
        icons.put("Paladin", mResourses.getDrawable(R.drawable.paladin));
        icons.put("Priest", mResourses.getDrawable(R.drawable.priest));
        icons.put("Rogue", mResourses.getDrawable(R.drawable.rogue));
        icons.put("Shaman", mResourses.getDrawable(R.drawable.shaman));
        icons.put("Warlock", mResourses.getDrawable(R.drawable.warlock));
        icons.put("Warrior", mResourses.getDrawable(R.drawable.warrior));

        return icons.get(_class);
    }

    // Helper method that gets the correct Color (int) based on the Toon's class
    private int getClassColor(String _class) {
        Map<String, Integer> colors = new HashMap<>();
        colors.put("Death Knight", mResourses.getColor(R.color.death_knight));
        colors.put("Demon Hunter", mResourses.getColor(R.color.demon_hunter));
        colors.put("Druid", mResourses.getColor(R.color.druid));
        colors.put("Hunter", mResourses.getColor(R.color.hunter));
        colors.put("Mage", mResourses.getColor(R.color.mage));
        colors.put("Monk", mResourses.getColor(R.color.monk));
        colors.put("Paladin", mResourses.getColor(R.color.paladin));
        colors.put("Priest", mResourses.getColor(R.color.priest));
        colors.put("Rogue", mResourses.getColor(R.color.rogue));
        colors.put("Shaman", mResourses.getColor(R.color.shaman));
        colors.put("Warlock", mResourses.getColor(R.color.warlock));
        colors.put("Warrior", mResourses.getColor(R.color.warrior));

        return colors.get(_class);
    }
}