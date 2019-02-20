package com.mattqunell.wowstats.toon_list;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mattqunell.wowstats.data.Toon;

import java.util.List;

/**
 * ToonAdapter: The Adapter
 * Connects the ViewHolder and Toons by knowing how Toons and ToonDb are implemented.
 * The overridden methods are all required and called by the RecyclerView itself.
 */
public class ToonAdapter extends RecyclerView.Adapter<ToonHolder> {

    private Fragment mParentFragment;
    private List<Toon> mToons;

    public ToonAdapter(Fragment parentFragment, List<Toon> toons) {
        mParentFragment = parentFragment;
        mToons = toons;
    }

    @Override
    public ToonHolder onCreateViewHolder(ViewGroup parentView, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mParentFragment.getActivity());

        return new ToonHolder(mParentFragment, layoutInflater, parentView);
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