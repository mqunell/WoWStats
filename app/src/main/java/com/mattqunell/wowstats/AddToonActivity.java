package com.mattqunell.wowstats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mattqunell.wowstats.data.BattleNetConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddToonActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mServer;
    private Button mAdd;

    public AddToonActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_add_toon);

        mName = findViewById(R.id.add_name);
        mServer = findViewById(R.id.add_server);
        mAdd = findViewById(R.id.add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BattleNetConnection(getApplicationContext()).execute(mName.getText().toString(), mServer.getText().toString());
            }
        });
    }

}
