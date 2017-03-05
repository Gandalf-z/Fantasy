package com.liuconen.fantasy.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.model.Mp3Info;
import com.liuconen.fantasy.service.PlayService;

import java.util.ArrayList;

/**
 * Created by liuconen on 2016/10/17.
 */

public abstract class DetailPageFragment extends ListFragment {
    private ArrayList<Mp3Info> mp3Infos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_page, container, false);

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.tb_detail_page_fragment_toolbar);
        mToolbar.setTitle(getToolbarTitle());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        mp3Infos = getBoundData();
        setListAdapter(getListViewAdapter(mp3Infos));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PlayService.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("fantasy.PLAY_LIST", mp3Infos);
                bundle.putInt("fantasy.SONG_INDEX", position);
                intent.putExtras(bundle);
                intent.setPackage(getContext().getPackageName());
                getContext().startService(intent);
            }
        });
    }

    public abstract ArrayList<Mp3Info> getBoundData();
    public abstract ListAdapter getListViewAdapter(ArrayList<Mp3Info> mp3Infos);
    public abstract String getToolbarTitle();
}
