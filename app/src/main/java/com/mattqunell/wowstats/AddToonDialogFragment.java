package com.mattqunell.wowstats;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * A DialogFragment used for adding new Toons that passes the user-inputted name and realm back to
 * ToonListFragment.
 */
public class AddToonDialogFragment extends DialogFragment {

    // Encapsulated request code and Bundle keys
    public static final int ATDF_REQUEST_CODE = 1;
    public static final String ATDF_BUNDLE_NAME = "ATDF_NAME";
    public static final String ATDF_BUNDLE_REALM = "ATDF_REALM";

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

                        // Format realm names with " " (ex. "Bleeding Hollow" -> "Bleeding-Hollow")
                        if (realm.contains(" ")) {
                            int spaceLoc = realm.indexOf(" ");
                            realm = realm.substring(0, spaceLoc) + "-" +
                                    realm.substring(spaceLoc + 1);
                        }

                        // Return the name and realm to ToonListFragment.onActivityResult(...)
                        Bundle bundle = new Bundle();
                        bundle.putString(ATDF_BUNDLE_NAME, name);
                        bundle.putString(ATDF_BUNDLE_REALM, realm);

                        getTargetFragment().onActivityResult(getTargetRequestCode(),
                                Activity.RESULT_OK,
                                new Intent().putExtras(bundle));
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
