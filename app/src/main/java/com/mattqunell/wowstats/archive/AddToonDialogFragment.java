package com.mattqunell.wowstats.archive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.mattqunell.wowstats.R;
import com.mattqunell.wowstats.data.BattleNetConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddToonDialogFragment extends DialogFragment {

    private EditText mName;
    private EditText mServer;

    public AddToonDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_add_toon, null);

        mName = view.findViewById(R.id.add_name);
        mServer = view.findViewById(R.id.add_server);

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Add the toon
                        new BattleNetConnection(getActivity()).execute(mName.getText().toString(), mServer.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }
}
