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

        String output;
        switch (sharedPrefsValue) {
            case "Class":
                output = mToon.get_Class();
                break;

            case "Faction":
                output = mToon.getFaction() == 0 ? "Alliance" : "Horde";
                break;

            case "Level/iLevel":
                output = mParentFragment.getString(R.string.level_ilevel,
                        String.valueOf(mToon.getLevel()), String.valueOf(mToon.getItemLevel()));
                break;

            case "Mythic+":
                if (mToon.getLevel() == BlizzardConnection.MAX_LEVEL) {
                    output = mParentFragment.getString(R.string.mythicscore_highestmythic,
                            mToon.getMythicScore(), mToon.getHighestMythic());
                }
                else {
                    output = "Not max level";
                }
                break;

            case "Name":
                output = mToon.getName();
                break;

            case "Race":
                output = mToon.getRace();
                break;

            case "Realm":
                output = mToon.getRealm();
                break;

            default:
                output = "";
                break;
        }

        return output;
    }

    // Helper method that gets the correct Drawable based on the Toon's class
    private Drawable getClassIcon(String _class) {
        Drawable icon = null;
        Resources res = mParentFragment.getResources();

        switch (_class) {
            case "Death Knight":
                icon = res.getDrawable(R.drawable.death_knight);
                break;

            case "Demon Hunter":
                icon = res.getDrawable(R.drawable.demon_hunter);
                break;

            case "Druid":
                icon = res.getDrawable(R.drawable.druid);
                break;

            case "Hunter":
                icon = res.getDrawable(R.drawable.hunter);
                break;

            case "Mage":
                icon = res.getDrawable(R.drawable.mage);
                break;

            case "Monk":
                icon = res.getDrawable(R.drawable.monk);
                break;

            case "Paladin":
                icon = res.getDrawable(R.drawable.paladin);
                break;

            case "Priest":
                icon = res.getDrawable(R.drawable.priest);
                break;

            case "Rogue":
                icon = res.getDrawable(R.drawable.rogue);
                break;

            case "Shaman":
                icon = res.getDrawable(R.drawable.shaman);
                break;

            case "Warlock":
                icon = res.getDrawable(R.drawable.warlock);
                break;

            case "Warrior":
                icon = res.getDrawable(R.drawable.warrior);
                break;
        }

        return icon;
    }

    // Helper method that gets the correct Color (int) based on the Toon's class
    private int getClassColor(String _class) {
        int color = -1;
        Resources res = mParentFragment.getResources();

        switch (_class) {
            case "Death Knight":
                color = res.getColor(R.color.death_knight);
                break;

            case "Demon Hunter":
                color = res.getColor(R.color.demon_hunter);
                break;

            case "Druid":
                color = res.getColor(R.color.druid);
                break;

            case "Hunter":
                color = res.getColor(R.color.hunter);
                break;

            case "Mage":
                color = res.getColor(R.color.mage);
                break;

            case "Monk":
                color = res.getColor(R.color.monk);
                break;

            case "Paladin":
                color = res.getColor(R.color.paladin);
                break;

            case "Priest":
                color = res.getColor(R.color.priest);
                break;

            case "Rogue":
                color = res.getColor(R.color.rogue);
                break;

            case "Shaman":
                color = res.getColor(R.color.shaman);
                break;

            case "Warlock":
                color = res.getColor(R.color.warlock);
                break;

            case "Warrior":
                color = res.getColor(R.color.warrior);
                break;
        }

        return color;
    }
}