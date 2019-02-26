package com.mattqunell.wowstats;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class CustomizeActivity extends AppCompatActivity {

    // Reference the SharedPreferences and Resources directly to reduce and simplify overall code
    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        setContentView(R.layout.activity_customize);

        mSharedPrefs = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        // Spinners
        initializeSpinner((Spinner) findViewById(R.id.tl1), ToonFormatter.TOP_LEFT_ONE);
        initializeSpinner((Spinner) findViewById(R.id.tl2), ToonFormatter.TOP_LEFT_TWO);
        initializeSpinner((Spinner) findViewById(R.id.tr), ToonFormatter.TOP_RIGHT);
        initializeSpinner((Spinner) findViewById(R.id.bl), ToonFormatter.BOTTOM_LEFT);
        initializeSpinner((Spinner) findViewById(R.id.br), ToonFormatter.BOTTOM_RIGHT);

        // Switch
        initializeSwitch((Switch) findViewById(R.id.switch_toggle));
    }

    // Initializes a Spinner
    private void initializeSpinner(Spinner spinner, final String sharedPrefsKey) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        // Set the Spinner to display the pre-selected option
        String current = mSharedPrefs.getString(sharedPrefsKey, "");

        final String[] spinnerOptions = getResources().getStringArray(R.array.spinner_options);
        int index = -1;
        for (int i = 0; i < spinnerOptions.length; i++) {
            if (spinnerOptions[i].equals(current)) {
                index = i;
            }
        }
        spinner.setSelection(index);

        // Listener; saves the selection to SharedPreferences
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSharedPrefs.edit().putString(sharedPrefsKey, spinnerOptions[position]).apply();

                setDemoTextview(sharedPrefsKey);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Intentionally left blank
            }
        });
    }

    // Initializes a Switch
    private void initializeSwitch(Switch toggleColors) {

        // Set the Switch to display the current toggled status
        Boolean toggled = mSharedPrefs.getBoolean(ToonFormatter.FLIPPED_COLORS, false);
        toggleColors.setChecked(toggled);
        setIconAndColor(toggled);

        // Listener; saves the toggled status to SharedPreferences
        toggleColors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSharedPrefs.edit().putBoolean(ToonFormatter.FLIPPED_COLORS, isChecked).apply();

                setIconAndColor(isChecked);
            }
        });
    }

    // Sets the appropriate TextView for the demo Toon to the SharedPrefs value
    private void setDemoTextview(String sharedPrefsKey) {
        TextView outputTextview;

        switch (sharedPrefsKey) {
            case ToonFormatter.TOP_LEFT_ONE:
                outputTextview = findViewById(R.id.demo_tl1);
                break;

            case ToonFormatter.TOP_LEFT_TWO:
                outputTextview = findViewById(R.id.demo_tl2);
                break;

            case ToonFormatter.TOP_RIGHT:
                outputTextview = findViewById(R.id.demo_tr);
                break;

            case ToonFormatter.BOTTOM_LEFT:
                outputTextview = findViewById(R.id.demo_bl);
                break;

            // case ToonFormatter.BOTTOM_RIGHT:
            default:
                outputTextview = findViewById(R.id.demo_br);
                break;
        }

        // Replace "--Empty--" with ""
        String outputText = mSharedPrefs.getString(sharedPrefsKey, "");
        if (outputText.equals("--Empty--"))
            outputText = "";

        outputTextview.setText(outputText);
    }

    // Sets the icon and background color for the demo Toon based on toggle status
    private void setIconAndColor(Boolean toggleStatus) {
        if (!toggleStatus) {
            ((ImageView) findViewById(R.id.demo_icon))
                    .setImageDrawable(getResources().getDrawable(R.drawable.mage));

            findViewById(R.id.demo_layout)
                    .setBackgroundColor(getResources().getColor(R.color.alliance));
        }
        else {
            ((ImageView) findViewById(R.id.demo_icon))
                    .setImageDrawable(getResources().getDrawable(R.drawable.alliance));

            findViewById(R.id.demo_layout)
                    .setBackgroundColor(getResources().getColor(R.color.mage));

        }
    }
}
