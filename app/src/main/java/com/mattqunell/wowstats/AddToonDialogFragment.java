package com.mattqunell.wowstats;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.mattqunell.wowstats.data.BattleNetConnection;

/**
 * A DialogFragment used for adding new Toons that passes the user-inputted name and realm to
 * BattleNetConnection.
 */
public class AddToonDialogFragment extends DialogFragment {

    // UI elements
    private EditText mName;
    private EditText mRealm;

    // Required empty public constructor
    public AddToonDialogFragment() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_add_toon, null);

        // UI elements
        mName = view.findViewById(R.id.add_name);
        mRealm = view.findViewById(R.id.add_realm);

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Retrieve the data
                        String name = mName.getText().toString();
                        String realm = mRealm.getText().toString();

                        // Replace " " in the realm name with "-"
                        // ex. "Bleeding Hollow" -> "Bleeding-Hollow"
                        if (realm.contains(" ")) {
                            int spaceLoc = realm.indexOf(" ");
                            realm = realm.substring(0, spaceLoc) + "-" +
                                    realm.substring(spaceLoc + 1);
                        }

                        // Pass the name and realm to BattleNetConnection
                        new BattleNetConnection(getActivity()).execute(name, realm);
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
