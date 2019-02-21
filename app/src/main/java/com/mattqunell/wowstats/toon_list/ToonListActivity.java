package com.mattqunell.wowstats.toon_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.mattqunell.wowstats.R;
import com.mattqunell.wowstats.ToonFormatter;

/**
 * Starts ToonListFragment.
 */
public class ToonListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        initSharedPrefs();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);

        if (fragment == null) {
            fragment = new ToonListFragment();

            /*
             * Transactions add, remove, attach, detach, and replace fragments. This creates a new
             * fragment transaction, includes one operation, and commits it. Adding the Fragment to
             * the FragmentManager calls its onAttach, onCreate, and onCreateView methods.
             */
            fm.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    // Initializes the customization SharedPreferences with default values
    private void initSharedPrefs() {
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.app_name),
                Context.MODE_PRIVATE);

        if (!sharedPrefs.contains("initialized")) {
            SharedPreferences.Editor editor = sharedPrefs.edit();

            editor.putBoolean("initialized", true);
            editor.putString(ToonFormatter.TOP_LEFT_ONE, "Name");
            editor.putString(ToonFormatter.TOP_LEFT_TWO, "Realm");
            editor.putString(ToonFormatter.TOP_RIGHT, "Level/iLevel");
            editor.putString(ToonFormatter.BOTTOM_LEFT, "Race");
            editor.putString(ToonFormatter.BOTTOM_RIGHT, "Mythic+");
            editor.putBoolean(ToonFormatter.FLIPPED_COLORS, false);

            editor.apply();
        }
    }
}
