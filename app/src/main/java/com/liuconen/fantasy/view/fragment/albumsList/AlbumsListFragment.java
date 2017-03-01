package com.liuconen.fantasy.view.fragment.albumsList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuconen.fantasy.adapter.AlbumsListFragmentRecyclerViewAdapter;
import com.liuconen.fantasy.model.AlbumInfo;
import com.liuconen.fantasy.model.OnRecyclerViewItemClickListener;
import com.liuconen.fantasy.model.decoration.GridSpacingItemDecoration;
import com.liuconen.fantasy.util.MediaUtil;
import com.liuconen.fantasy.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuconen on 2016/8/11.
 */
public class AlbumsListFragment extends BaseFragment {
    private final int SPACING_IN_PIXEL = 20;
    private List<AlbumInfo> albumsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        albumsList = MediaUtil.getAlbumInfos(getContext());

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        AlbumsListFragmentRecyclerViewAdapter mAdapter = new AlbumsListFragmentRecyclerViewAdapter(getContext(),
                getRecyclerView(), manager,
                albumsList);


        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemCLick(View view, Object data) {

            }
        });

        setRecyclerViewAdapter(mAdapter);
        setRecyclerViewLayoutManager(manager);
        addRecyclerViewItemDecoration(new GridSpacingItemDecoration(2, SPACING_IN_PIXEL, true));
        return view;
    }
}
