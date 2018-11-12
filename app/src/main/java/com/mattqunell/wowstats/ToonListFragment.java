package com.mattqunell.wowstats;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mattqunell.wowstats.data.BlizzardAsyncResponse;
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
        implements BlizzardAsyncResponse, RaiderAsyncResponse {

    private RecyclerView mRecyclerView;
    private ToonAdapter mAdapter;

    // Mandatory empty constructor
    public ToonListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Reference to this Fragment for each instance of BlizzardConnection
        final ToonListFragment tlf = this;

        // FAB listener
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_toon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the DialogFragment and set this as the target for onActivityResult(...)
                DialogFragment fragment = new AddToonDialogFragment();
                fragment.setTargetFragment(tlf, ATDF_REQUEST_CODE);
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

            // Refresh - updates all Toons
            case R.id.refresh:
                List<Toon> toons = ToonDb.get(getContext()).getToons();

                Toast.makeText(getContext(), R.string.refreshing, Toast.LENGTH_SHORT).show();
                for (Toon t : toons) {
                    new BlizzardConnection(this).execute(t.getName(), t.getRealm());
                }

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
                new BlizzardConnection(this).execute(name, realm);
            }
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

    // Helper method that creates/sets or updates the Adapter
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


    /**
     * ToonHolder: The ViewHolder
     * Inflates and owns each individual layout (fragment_toon_item) within the RecyclerView.
     * The bind(Toon) method is called each time a new Toon should be displayed.
     */
    private class ToonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The specific Toon
        private Toon mToon;

        // UI elements
        private ConstraintLayout mLayout;
        private ImageView mClassIcon;
        private TextView mToonTopLeftOne;
        private TextView mToonTopLeftTwo;
        private TextView mToonBottomLeft;
        private TextView mToonTopRight;
        private TextView mToonBottomRight;

        public ToonHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_toon_item, parent, false));
            itemView.setOnClickListener(this);

            // UI elements
            mLayout = itemView.findViewById(R.id.toon_item);
            mClassIcon = itemView.findViewById(R.id.class_icon);
            mToonTopLeftOne = itemView.findViewById(R.id.toon_top_left_one);
            mToonTopLeftTwo = itemView.findViewById(R.id.toon_top_left_two);
            mToonBottomLeft = itemView.findViewById(R.id.toon_bottom_left_one);
            mToonTopRight = itemView.findViewById(R.id.toon_top_right);
            mToonBottomRight = itemView.findViewById(R.id.toon_bottom_right);
        }

        // Sets the individual layout's elements
        public void bind(Toon toon) {
            mToon = toon;

            mClassIcon.setImageDrawable(getClassIcon(mToon.get_Class()));
            mToonTopLeftOne.setText(mToon.getName());
            mToonTopLeftTwo.setText(mToon.getRealm());
            mToonBottomLeft.setText(mToon.getRace());
            mToonTopRight.setText(getString(R.string.level_ilevel,
                    String.valueOf(mToon.getLevel()), String.valueOf(mToon.getItemLevel())));

            // Show mythic data if the Toon is max level
            if (mToon.getLevel() == BlizzardConnection.MAX_LEVEL) {
                mToonBottomRight.setText(getString(R.string.mythicscore_highestmythic,
                        mToon.getMythicScore(), mToon.getHighestMythic()));
            }

            // Set background color based on faction
            mLayout.setBackgroundColor(mToon.getFaction() == 0 ?
                    getResources().getColor(R.color.colorAlliance) :
                    getResources().getColor(R.color.colorHorde));
        }

        @Override
        public void onClick(View view) {
            // Start an activity for that specific character
        }

        // Helper method that gets the correct Drawable based on the Toon's class
        private Drawable getClassIcon(String _class) {
            Drawable icon = null;
            switch (_class) {
                case "Death Knight":
                    icon = getResources().getDrawable(R.drawable.death_knight);
                    break;

                case "Demon Hunter":
                    icon = getResources().getDrawable(R.drawable.demon_hunter);
                    break;

                case "Druid":
                    icon = getResources().getDrawable(R.drawable.druid);
                    break;

                case "Hunter":
                    icon = getResources().getDrawable(R.drawable.hunter);
                    break;

                case "Mage":
                    icon = getResources().getDrawable(R.drawable.mage);
                    break;

                case "Monk":
                    icon = getResources().getDrawable(R.drawable.monk);
                    break;

                case "Paladin":
                    icon = getResources().getDrawable(R.drawable.paladin);
                    break;

                case "Priest":
                    icon = getResources().getDrawable(R.drawable.priest);
                    break;

                case "Rogue":
                    icon = getResources().getDrawable(R.drawable.rogue);
                    break;

                case "Shaman":
                    icon = getResources().getDrawable(R.drawable.shaman);
                    break;

                case "Warlock":
                    icon = getResources().getDrawable(R.drawable.warlock);
                    break;

                case "Warrior":
                    icon = getResources().getDrawable(R.drawable.warrior);
                    break;
            }

            return icon;
        }
    }
}
