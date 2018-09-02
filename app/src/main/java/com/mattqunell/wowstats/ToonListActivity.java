package com.mattqunell.wowstats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Starts ToonListFragment.
 */
public class ToonListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.container);

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
}
