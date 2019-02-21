package com.mattqunell.wowstats.toon_list;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mattqunell.wowstats.R;
import com.mattqunell.wowstats.ToonFormatter;
import com.mattqunell.wowstats.data.Toon;

/**
 * ToonHolder: The ViewHolder
 * Inflates and owns each individual layout (fragment_toon_item) within the RecyclerView.
 * The bind(Toon) method is called each time a new Toon should be displayed.
 */
public class ToonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // A reference to ToonListFragment
    private Fragment mParentFragment;

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
        itemView.setOnClickListener(this);

        mParentFragment = parentFragment;

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
        ToonFormatter tf = ToonFormatter.get(mParentFragment);

        // Set the TextViews
        mToonTopLeftOne.setText(tf.getTextviewOutput(toon, ToonFormatter.TOP_LEFT_ONE));
        mToonTopLeftTwo.setText(tf.getTextviewOutput(toon, ToonFormatter.TOP_LEFT_TWO));
        mToonTopRight.setText(tf.getTextviewOutput(toon, ToonFormatter.TOP_RIGHT));
        mToonBottomLeft.setText(tf.getTextviewOutput(toon, ToonFormatter.BOTTOM_LEFT));
        mToonBottomRight.setText(tf.getTextviewOutput(toon, ToonFormatter.BOTTOM_RIGHT));

        // Set the ImageView and background color
        Boolean flippedColors = mParentFragment.getContext().getSharedPreferences(
                mParentFragment.getString(R.string.app_name), Context.MODE_PRIVATE)
                .getBoolean(ToonFormatter.FLIPPED_COLORS, false);

        if (!flippedColors) {
            mIcon.setImageDrawable(tf.getClassIcon(toon.get_Class()));
            mLayout.setBackgroundColor(tf.getFactionColor(toon.getFaction()));
        }
        else {
            mIcon.setImageDrawable(tf.getFactionIcon(toon.getFaction()));
            mLayout.setBackgroundColor(tf.getClassColor(toon.get_Class()));
        }
    }

    @Override
    public void onClick(View view) {
        // Start an activity for that specific character
    }
}