package com.mattqunell.wowstats;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

public class CustomizeActivity extends AppCompatActivity {

    // SharedPreferences keys
    public static final String TOP_LEFT_ONE = "top_left_one",
            TOP_LEFT_TWO = "top_left_two",
            TOP_RIGHT = "top_right",
            BOTTOM_LEFT = "bottom_left",
            BOTTOM_RIGHT = "bottom_right";

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        setContentView(R.layout.activity_customize);

        // Spinners
        initializeSpinner((Spinner) findViewById(R.id.tl1), TOP_LEFT_ONE);
        initializeSpinner((Spinner) findViewById(R.id.tl2), TOP_LEFT_TWO);
        initializeSpinner((Spinner) findViewById(R.id.tr), TOP_RIGHT);
        initializeSpinner((Spinner) findViewById(R.id.bl), BOTTOM_LEFT);
        initializeSpinner((Spinner) findViewById(R.id.br), BOTTOM_RIGHT);

        // Switch
        Switch toggle = findViewById(R.id.switch_toggle);
        //toggle.setChecked(); todo
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo
            }
        });
    }

    private void initializeSpinner(Spinner spinner, final String sharedPrefsKey) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        // Set the Spinner to display the currently-selected option
        String current = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
                .getString(sharedPrefsKey, "");

        final String[] spinnerOptions = getResources().getStringArray(R.array.spinner_options);
        int index = -1;
        for (int i = 0; i < spinnerOptions.length; i++) {
            if (spinnerOptions[i].equals(current)) {
                index = i;
            }
        }
        spinner.setSelection(index);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = spinnerOptions[position];

                getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
                        .edit()
                        .putString(sharedPrefsKey, selected)
                        .apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Intentionally left blank
            }
        });
    }
}
