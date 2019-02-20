package com.mattqunell.wowstats.toon_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mattqunell.wowstats.AddToonDialogFragment;
import com.mattqunell.wowstats.CustomizeActivity;
import com.mattqunell.wowstats.R;
import com.mattqunell.wowstats.data.BlizzardAsyncResponse;
import com.mattqunell.wowstats.data.BlizzardAuthAsyncResponse;
import com.mattqunell.wowstats.data.BlizzardAuthConnection;
import com.mattqunell.wowstats.data.BlizzardConnection;
import com.mattqunell.wowstats.data.RaiderAsyncResponse;
import com.mattqunell.wowstats.data.RaiderConnection;
import com.mattqunell.wowstats.data.Toon;
import com.mattqunell.wowstats.database.ToonDb;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.mattqunell.wowstats.AddToonDialogFragment.ATDF_BUNDLE_NAME;
import static com.mattqunell.wowstats.AddToonDialogFragment.ATDF_BUNDLE_REALM;
import static com.mattqunell.wowstats.AddToonDialogFragment.ATDF_REQUEST_CODE;

/**
 * Fragment that handles the RecyclerView and its components
 */
public class ToonListFragment extends Fragment
        implements BlizzardAuthAsyncResponse, BlizzardAsyncResponse, RaiderAsyncResponse {

    private RecyclerView mRecyclerView;
    private ToonAdapter mAdapter;

    // The OAuth token
    private String mAuthToken;

    // Mandatory empty constructor
    public ToonListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // FAB listener
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_toon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the DialogFragment and set this as the target for onActivityResult(...)
                DialogFragment fragment = new AddToonDialogFragment();
                fragment.setTargetFragment(ToonListFragment.this, ATDF_REQUEST_CODE);
                fragment.show(getActivity().getSupportFragmentManager(), "Add Character");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle inState) {
        View view = inflater.inflate(R.layout.fragment_toon_list, container, false);

        mRecyclerView = view.findViewById(R.id.toon_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUi();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Refresh - updates all Toons
            case R.id.refresh:
                List<Toon> toons = ToonDb.get(getContext()).getToons();

                Toast.makeText(getContext(), R.string.refreshing, Toast.LENGTH_SHORT).show();
                for (Toon t : toons) {
                    new BlizzardConnection(this, mAuthToken).execute(t.getName(), t.getRealm());
                }

                return true;

            // Customize
            case R.id.customize:
                startActivity(new Intent(getContext(), CustomizeActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Called from AddToonDialogFragment if successful
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ATDF_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String name = data.getExtras().getString(ATDF_BUNDLE_NAME);
                String realm = data.getExtras().getString(ATDF_BUNDLE_REALM);

                String output = getString(R.string.adding, name);
                Toast.makeText(getContext(), output, Toast.LENGTH_SHORT).show();

                new BlizzardConnection(this, mAuthToken).execute(name, realm);
            }
        }
    }

    // Called from BlizzardAuthConnection's onPostExecute
    @Override
    public void processBlizzardAuth(String token) {
        if (token != null) {
            mAuthToken = token;
        }
    }

    /*
     * Called from BlizzardConnection's onPostExecute
     *
     * Toon exists, = max level: Send to RaiderConnection for mythic data
     * Toon exists, < max level: Add to database and update
     * Toon does not exist: Toast
     */
    @Override
    public void processBlizzard(Toon toon) {
        if (toon != null) {
            if (toon.getLevel() == BlizzardConnection.MAX_LEVEL) {
                new RaiderConnection(this, toon).execute();
            }
            else {
                addProcessedToon(toon);
            }
        }
        else {
            Toast.makeText(getContext(), R.string.could_not_find, Toast.LENGTH_SHORT).show();
        }
    }

    // Called from RaiderConnection's onPostExecute
    @Override
    public void processRaider(Toon toon) {
        addProcessedToon(toon);
    }

    // Helper method that adds/updates a processed Toon and displays the proper Toast
    private void addProcessedToon(Toon toon) {
        String output = !ToonDb.get(getContext()).addToon(toon) ?
                getString(R.string.added, toon.getName()) :
                getString(R.string.updated, toon.getName());

        Toast.makeText(getContext(), output, Toast.LENGTH_SHORT).show();
        updateUi();
    }

    // Helper method that creates/sets or updates the Adapter and gets an OAuth token
    private void updateUi() {
        List<Toon> toons = ToonDb.get(getActivity()).getToons();

        // Create/refresh the adapter
        if (mAdapter == null) {
            mAdapter = new ToonAdapter(this, toons);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setToons(toons);
            mAdapter.notifyDataSetChanged();
        }

        // Get a new auth token
        new BlizzardAuthConnection(this).execute();
    }
}
