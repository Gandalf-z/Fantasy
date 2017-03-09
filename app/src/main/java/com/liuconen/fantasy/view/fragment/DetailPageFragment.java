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
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.model.Mp3Info;
import com.liuconen.fantasy.service.PlayService;

import java.util.ArrayList;

/**
 * Created by liuconen on 2016/10/17.
 */

public class DetailPageFragment extends ListFragment {

    private ArrayList<Mp3Info> mp3Infos;
    private String toolbarTitle;

    public static DetailPageFragment newInstance(ArrayList<Mp3Info> mp3Infos, String toolbarTitle) {
        DetailPageFragment fragment = new DetailPageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MP3INFOS", mp3Infos);
        bundle.putString("TITLE", toolbarTitle);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mp3Infos = (ArrayList<Mp3Info>) bundle.getSerializable("MP3INFOS");
        toolbarTitle = bundle.getString("TITLE");
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_page, container, false);

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.tb_detail_page_fragment_toolbar);
        mToolbar.setTitle(toolbarTitle);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        setListAdapter(getListViewAdapter());
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

    private ListAdapter getListViewAdapter() {
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
                text2.setText(cMp3.getArtist() + " | " + cMp3.getAlbum());
                return view;
            }
        };
        return adapter;
    }
}
