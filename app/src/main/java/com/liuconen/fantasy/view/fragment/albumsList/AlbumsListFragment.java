package com.liuconen.fantasy.view.fragment.albumsList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.adapter.AlbumsListFragmentRecyclerViewAdapter;
import com.liuconen.fantasy.model.AlbumInfo;
import com.liuconen.fantasy.model.Mp3Info;
import com.liuconen.fantasy.model.OnRecyclerViewItemClickListener;
import com.liuconen.fantasy.model.decoration.GridSpacingItemDecoration;
import com.liuconen.fantasy.util.MediaUtil;
import com.liuconen.fantasy.view.fragment.BaseFragment;
import com.liuconen.fantasy.view.fragment.DetailPageFragment;

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
            public void onItemCLick(View view, final Object data) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.add(R.id.main_layout, new DetailPageFragment() {
                    @Override
                    public ArrayList<Mp3Info> getBoundData() {
                        return MediaUtil.getMp3InfoByAlbumName(getContext(), (String)data);
                    }

                    @Override
                    public ListAdapter getListViewAdapter(final ArrayList<Mp3Info> mp3Infos) {
                        ListAdapter adapter = new ArrayAdapter<Mp3Info>(getContext(), android.R.layout
                                .simple_list_item_2,
                                android.R.id.text1, mp3Infos) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                                Mp3Info cMp3 = mp3Infos.get(position);
                                text1.setText(cMp3.getSongName());
                                text2.setText(cMp3.getArtist());
                                return view;
                            }
                        };
                        return adapter;
                    }

                    @Override
                    public String getToolbarTitle() {
                        return (String)data;
                    }
                });
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
