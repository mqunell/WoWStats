package com.mattqunell.wowstats;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ToonListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.container);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogFragment fragment = new AddToonDialogFragment();
                //fragment.show(getSupportFragmentManager(), "Add Character");

                startActivity(new Intent(ToonListActivity.this, AddToonActivity.class));
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);

        if (fragment == null) {
            fragment = new ToonListFragment();

            /*
             * Transactions add, remove, attach, detach, and replace fragments.
             * This creates a new fragment transaction, includes one operation, and commits it.
             * Adding the Fragment to the FragmentManager calls its onAttach, onCreate, and
             * onCreateView methods.
             */
            fm.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }
}
