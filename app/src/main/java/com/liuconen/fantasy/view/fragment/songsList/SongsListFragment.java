package com.liuconen.fantasy.view.fragment.songsList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuconen.fantasy.adapter.SongsListFragmentRecyclerViewAdapter;
import com.liuconen.fantasy.model.Mp3Info;
import com.liuconen.fantasy.model.OnRecyclerViewItemClickListener;
import com.liuconen.fantasy.model.decoration.SpacesItemDecoration;
import com.liuconen.fantasy.service.PlayService;
import com.liuconen.fantasy.util.MediaUtil;
import com.liuconen.fantasy.view.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by liuconen on 2016/8/11.
 */
public class SongsListFragment extends BaseFragment {

    private ArrayList<Mp3Info> mp3Infos;
    private final int SPACING_IN_PIXEL = 8;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mp3Infos = MediaUtil.getMp3Infos(getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        SongsListFragmentRecyclerViewAdapter mAdapter = new SongsListFragmentRecyclerViewAdapter(getContext(),
                mp3Infos, getRecyclerView(), manager);


        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemCLick(View view, Object data) {
                Intent intent = new Intent(getContext(), PlayService.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("fantasy.PLAY_LIST", mp3Infos);
                bundle.putInt("fantasy.SONG_INDEX", (Integer) data);
                intent.putExtras(bundle);
                intent.setPackage(getContext().getPackageName());
                getContext().startService(intent);
            }
        });

        setRecyclerViewAdapter(mAdapter);
        setRecyclerViewLayoutManager(manager);
        addRecyclerViewItemDecoration(new SpacesItemDecoration(SPACING_IN_PIXEL));
        return view;
    }
}
