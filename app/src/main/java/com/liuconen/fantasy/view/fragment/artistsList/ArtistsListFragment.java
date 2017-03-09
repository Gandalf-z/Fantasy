package com.liuconen.fantasy.view.fragment.artistsList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.adapter.ArtistsListFragmentRecyclerViewAdapter;
import com.liuconen.fantasy.model.ArtistInfo;
import com.liuconen.fantasy.model.OnRecyclerViewItemClickListener;
import com.liuconen.fantasy.model.decoration.SpacesItemDecoration;
import com.liuconen.fantasy.util.MediaUtil;
import com.liuconen.fantasy.view.fragment.BaseFragment;
import com.liuconen.fantasy.view.fragment.DetailPageFragment;

import java.util.ArrayList;

/**
 * Created by liuconen on 2016/8/11.
 */
public class ArtistsListFragment extends BaseFragment {
    private final int SPACING_IN_PIXEL = 8;
    private ArrayList<ArtistInfo> artistsList;

    private static ArtistsListFragment artistsListFragment;
    public static ArtistsListFragment newInstance(ArrayList<ArtistInfo> artistsList){
        if(artistsListFragment == null){
            artistsListFragment = new ArtistsListFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("ARTISTSLIST", artistsList);
            artistsListFragment.setArguments(bundle);

            return artistsListFragment;
        }else{
            return artistsListFragment;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistsList = (ArrayList<ArtistInfo>) getArguments().get("ARTISTSLIST");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ArtistsListFragmentRecyclerViewAdapter mAdapter = new ArtistsListFragmentRecyclerViewAdapter(this,
                artistsList);
        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemCLick(View view, final Object data) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.add(R.id.main_layout, DetailPageFragment.newInstance(MediaUtil.getMp3InfoByArtist(getContext(), (String) data), (String) data));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        setRecyclerViewAdapter(mAdapter);
        setRecyclerViewLayoutManager(new LinearLayoutManager(getContext()));
        addRecyclerViewItemDecoration(new SpacesItemDecoration(SPACING_IN_PIXEL));
        return view;
    }
}