package com.mattqunell.wowstats.toon_list;

import android.content.Context;
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

    // The parent Fragment (ToonListFragment)
    private Fragment mParentFragment;

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
        mParentFragment = parentFragment;

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
        Boolean flippedColors = mParentFragment.getContext().getSharedPreferences(
                mParentFragment.getString(R.string.app_name), Context.MODE_PRIVATE)
                .getBoolean(CustomizeActivity.FLIPPED_COLORS, false);

        if (!flippedColors) {
            mIcon.setImageDrawable(getClassIcon(mToon.get_Class()));

            mLayout.setBackgroundColor(mToon.getFaction() == 0 ?
                    mParentFragment.getResources().getColor(R.color.alliance) :
                    mParentFragment.getResources().getColor(R.color.horde));
        }
        else {
            mIcon.setImageDrawable(mToon.getFaction() == 0 ?
                    mParentFragment.getResources().getDrawable(R.drawable.alliance) :
                    mParentFragment.getResources().getDrawable(R.drawable.horde));

            mLayout.setBackgroundColor(getClassColor(mToon.get_Class()));
        }
    }

    @Override
    public void onClick(View view) {
        // Start an activity for that specific character
    }

    // Helper method that gets the appropriate output based on SharedPreferences data
    private String getOutput(String sharedPrefsKey) {
        String sharedPrefsValue = mParentFragment.getContext().getSharedPreferences(
                mParentFragment.getString(R.string.app_name), Context.MODE_PRIVATE)
                .getString(sharedPrefsKey, "");

        Map<String, String> output = new HashMap<>();
        output.put("Class", mToon.get_Class());
        output.put("Faction", mToon.getFaction() == 0 ? "Alliance" : "Horde");
        output.put("Level/iLevel", mParentFragment.getString(R.string.level_ilevel,
                String.valueOf(mToon.getLevel()), String.valueOf(mToon.getItemLevel())));
        output.put("Mythic+", mToon.getLevel() == BlizzardConnection.MAX_LEVEL ?
                mParentFragment.getString(R.string.mythicscore_highestmythic, mToon.getMythicScore(), mToon.getHighestMythic()) : "Not max level");
        output.put("Name", mToon.getName());
        output.put("Race", mToon.getRace());
        output.put("Realm", mToon.getRealm());

        return output.get(sharedPrefsValue);
    }

    // Helper method that gets the correct Drawable based on the Toon's class
    private Drawable getClassIcon(String _class) {
        Resources res = mParentFragment.getResources();

        Map<String, Drawable> icons = new HashMap<>();
        icons.put("Death Knight", res.getDrawable(R.drawable.death_knight));
        icons.put("Demon Hunter", res.getDrawable(R.drawable.demon_hunter));
        icons.put("Druid", res.getDrawable(R.drawable.druid));
        icons.put("Hunter", res.getDrawable(R.drawable.hunter));
        icons.put("Mage", res.getDrawable(R.drawable.mage));
        icons.put("Monk", res.getDrawable(R.drawable.monk));
        icons.put("Paladin", res.getDrawable(R.drawable.paladin));
        icons.put("Priest", res.getDrawable(R.drawable.priest));
        icons.put("Rogue", res.getDrawable(R.drawable.rogue));
        icons.put("Shaman", res.getDrawable(R.drawable.shaman));
        icons.put("Warlock", res.getDrawable(R.drawable.warlock));
        icons.put("Warrior", res.getDrawable(R.drawable.warrior));

        return icons.get(_class);
    }

    // Helper method that gets the correct Color (int) based on the Toon's class
    private int getClassColor(String _class) {
        Resources res = mParentFragment.getResources();

        Map<String, Integer> colors = new HashMap<>();
        colors.put("Death Knight", res.getColor(R.color.death_knight));
        colors.put("Demon Hunter", res.getColor(R.color.demon_hunter));
        colors.put("Druid", res.getColor(R.color.druid));
        colors.put("Hunter", res.getColor(R.color.hunter));
        colors.put("Mage", res.getColor(R.color.mage));
        colors.put("Monk", res.getColor(R.color.monk));
        colors.put("Paladin", res.getColor(R.color.paladin));
        colors.put("Priest", res.getColor(R.color.priest));
        colors.put("Rogue", res.getColor(R.color.rogue));
        colors.put("Shaman", res.getColor(R.color.shaman));
        colors.put("Warlock", res.getColor(R.color.warlock));
        colors.put("Warrior", res.getColor(R.color.warrior));

        return colors.get(_class);
    }
}