package com.mattqunell.wowstats;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mattqunell.wowstats.data.Toon;
import com.mattqunell.wowstats.database.ToonDb;

import java.util.List;

/**
 * Fragment that handles the RecyclerView and its components
 */
public class ToonListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ToonAdapter mAdapter;

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
                DialogFragment fragment = new AddToonDialogFragment();
                fragment.show(getActivity().getSupportFragmentManager(), "Add Character");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle inState) {

        // Inflate the layout file
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

            // Refresh
            case R.id.refresh:
                updateUi();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Helper method that creates/updates the Adapter
    private void updateUi() {

        // Get the list of Toons
        List<Toon> toons = ToonDb.get(getActivity()).getToons();

        // Create/refresh the adapter
        if (mAdapter == null) {
            mAdapter = new ToonAdapter(toons);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setToons(toons);
            mAdapter.notifyDataSetChanged();
        }
    }


    /**
     * ToonHolder: The ViewHolder
     * Inflates and owns each individual layout (fragment_toon_item) within the RecyclerView.
     * The bind(Toon) method is called each time a new Toon should be displayed.
     */
    private class ToonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The specific Toon
        private Toon mToon;

        // UI elements
        private TextView mToonName;
        private TextView mToonRealm;
        private TextView mToonRace;
        private TextView mToonClass;
        private TextView mToonLevel;
        private TextView mToonItemLevel;

        public ToonHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_toon_item, parent, false));
            itemView.setOnClickListener(this);

            // UI elements
            mToonName = itemView.findViewById(R.id.toon_name);
            mToonRealm = itemView.findViewById(R.id.toon_realm);
            mToonRace = itemView.findViewById(R.id.toon_race);
            mToonClass = itemView.findViewById(R.id.toon_class);
            mToonLevel = itemView.findViewById(R.id.toon_level);
            mToonItemLevel = itemView.findViewById(R.id.toon_item_level);
        }

        // Sets the individual layout's elements
        public void bind(Toon toon) {
            mToon = toon;

            mToonName.setText(mToon.getName());
            mToonRealm.setText(mToon.getRealm());
            mToonRace.setText(getString(R.string.name_realm, mToon.getRace(), mToon.get_Class()));
            //mToonClass.setText(mToon.get_Class());
            mToonLevel.setText(getString(R.string.level, String.valueOf(mToon.getLevel())));
            mToonItemLevel.setText(getString(R.string.item_level, String.valueOf(mToon.getItemLevel())));
        }

        @Override
        public void onClick(View view) {
            // Start an activity for that specific character
        }
    }


    /**
     * ToonAdapter: The Adapter
     * Connects the ViewHolder and Toons by knowing how Toons and ToonDb are implemented.
     * The overridden methods are all required and called by the RecyclerView itself.
     */
    private class ToonAdapter extends RecyclerView.Adapter<ToonHolder> {

        private List<Toon> mToons;

        public ToonAdapter(List<Toon> toons) {
            mToons = toons;
        }

        @Override
        public ToonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new ToonHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ToonHolder holder, int position) {
            Toon toon = mToons.get(position);
            holder.bind(toon);
        }

        @Override
        public int getItemCount() {
            return mToons.size();
        }

        public void setToons(List<Toon> toons) {
            mToons = toons;
        }
    }
}
