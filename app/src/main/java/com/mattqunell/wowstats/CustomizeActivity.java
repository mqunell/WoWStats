package com.mattqunell.wowstats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CustomizeActivity extends AppCompatActivity {

    // UI elements
    /*private Spinner mTopLeftOne;
    private Spinner mTopLeftTwo;
    private Spinner mTopRight;
    private Spinner mBottomLeft;
    private Spinner mBottomRight;*/

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        setContentView(R.layout.activity_customize);

        Spinner[] spinners = new Spinner[5];
        spinners[0] = findViewById(R.id.tl1);
        spinners[1] = findViewById(R.id.tl2);
        spinners[2] = findViewById(R.id.tr);
        spinners[3] = findViewById(R.id.bl);
        spinners[4] = findViewById(R.id.br);

        for (Spinner s : spinners) {
            initializeSpinner(s);
        }

        /*mTopLeftOne = findViewById(R.id.tl1);
        mTopLeftTwo = findViewById(R.id.tl2);
        mTopRight = findViewById(R.id.tr);
        mBottomLeft = findViewById(R.id.bl);
        mBottomRight = findViewById(R.id.br);*/
    }

    private void initializeSpinner(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        // todo check shared prefs for default value
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // todo save choice to shared prefs
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Intentionally left blank
            }
        });
    }
}
