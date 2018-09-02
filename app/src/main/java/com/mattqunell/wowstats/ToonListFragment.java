package com.mattqunell.wowstats;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mattqunell.wowstats.data.Toon;
import com.mattqunell.wowstats.database.ToonDb;

import java.util.List;

/**
 * todo
 */
public class ToonListFragment extends Fragment {

    //private OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private ToonAdapter mAdapter;

    /**
     * Mandatory empty constructor
     */
    public ToonListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    // todo
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
     * todo
     */
    private class ToonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Toon mToon;

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

        public void bind(Toon toon) {
            mToon = toon;

            // todo
            mToonName.setText(mToon.getName());
            mToonRealm.setText(mToon.getRealm());
            mToonRace.setText(mToon.getRace() + " " + mToon.get_Class());
            //mToonClass.setText(mToon.getClassName());
            mToonLevel.setText("Level " + String.valueOf(mToon.getLevel()));
            mToonItemLevel.setText("iLevel " + String.valueOf(mToon.getItemLevel()));
        }

        @Override
        public void onClick(View view) {

        }
    }


    /**
     * todo
     */
    private class ToonAdapter extends RecyclerView.Adapter<ToonHolder> {

        private List<Toon> mToons;

        public ToonAdapter(List<Toon> toons) {
            mToons = toons;
        }

        @NonNull
        @Override
        public ToonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new ToonHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ToonHolder holder, int position) {
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
