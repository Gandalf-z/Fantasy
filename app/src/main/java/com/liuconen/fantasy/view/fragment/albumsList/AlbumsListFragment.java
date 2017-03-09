package com.liuconen.fantasy.view.fragment.albumsList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.adapter.AlbumsListFragmentRecyclerViewAdapter;
import com.liuconen.fantasy.model.AlbumInfo;
import com.liuconen.fantasy.model.OnRecyclerViewItemClickListener;
import com.liuconen.fantasy.model.decoration.GridSpacingItemDecoration;
import com.liuconen.fantasy.util.MediaUtil;
import com.liuconen.fantasy.view.fragment.BaseFragment;
import com.liuconen.fantasy.view.fragment.DetailPageFragment;

import java.util.ArrayList;

/**
 * Created by liuconen on 2016/8/11.
 */
public class AlbumsListFragment extends BaseFragment {
    private final int SPACING_IN_PIXEL = 20;
    private ArrayList<AlbumInfo> albumsList;

    private static AlbumsListFragment albumsListFragment;
    public static AlbumsListFragment newInstance(ArrayList<AlbumInfo> albumsList){
        if(albumsListFragment == null){
            albumsListFragment = new AlbumsListFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("ALBUMSLIST", albumsList);
            albumsListFragment.setArguments(bundle);

            return albumsListFragment;
        }else{
            return albumsListFragment;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumsList = (ArrayList<AlbumInfo>) getArguments().get("ALBUMSLIST");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        AlbumsListFragmentRecyclerViewAdapter mAdapter = new AlbumsListFragmentRecyclerViewAdapter(getContext(),
                getRecyclerView(), manager,
                albumsList);


        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemCLick(View view, final Object data) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.add(R.id.main_layout, DetailPageFragment.newInstance(MediaUtil.getMp3InfoByAlbumName(getContext(), (String)data), (String)data));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        setRecyclerViewAdapter(mAdapter);
        setRecyclerViewLayoutManager(manager);
        addRecyclerViewItemDecoration(new GridSpacingItemDecoration(2, SPACING_IN_PIXEL, true));
        return view;
    }
}
