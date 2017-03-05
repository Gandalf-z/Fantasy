package com.liuconen.fantasy.view.fragment.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.adapter.MainFragmentViewPagerAdapter;
import com.liuconen.fantasy.model.Mp3Info;
import com.liuconen.fantasy.util.MediaUtil;
import com.liuconen.fantasy.view.CurrentPlayingStatusLayout;
import com.liuconen.fantasy.view.fragment.PlayFragment;
import com.liuconen.fantasy.view.fragment.albumsList.AlbumsListFragment;
import com.liuconen.fantasy.view.fragment.artistsList.ArtistsListFragment;
import com.liuconen.fantasy.view.fragment.personCenter.PersonalCenterFragment;
import com.liuconen.fantasy.view.fragment.songsList.SongsListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liuconen on 2016/4/13.
 */
public class MainFragment extends Fragment {

    final int NUMBER_OF_OFFSCREEN_PAGE = 4;
    final int DEFAULT_PAGE_INDEX = 1;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private CurrentPlayingStatusLayout playStatusBar;

    private PlayStatusUpdateReceiver mReceiver = new PlayStatusUpdateReceiver();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        bindWidget(view);
        addListener();


        ArrayList<String> pagerTitles = new ArrayList<>(Arrays.asList("我的", "歌曲", "艺术家", "专辑"));
        List<Fragment> fragments = new ArrayList<>();
        MainFragmentViewPagerAdapter adapter = new MainFragmentViewPagerAdapter(getActivity()
                .getSupportFragmentManager(), pagerTitles, fragments);

        fragments.add(new PersonalCenterFragment());
        fragments.add(new SongsListFragment());
        fragments.add(new ArtistsListFragment());
        fragments.add(new AlbumsListFragment());

        mViewPager.setOffscreenPageLimit(NUMBER_OF_OFFSCREEN_PAGE);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(DEFAULT_PAGE_INDEX);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("fantasy.action.PLAY_STATUS_CHANGED");
        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    void bindWidget(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.vp_view_pager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tl_tab_layout);
        playStatusBar = (CurrentPlayingStatusLayout) view.findViewById(R.id.cpsl_main_activity_play_status_bar);
    }

    private void addListener() {
        playStatusBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                if (fm.findFragmentByTag("PLAY_FRAGMENT") == null) {
                    transaction.add(R.id.main_layout, new PlayFragment(), "PLAY_FRAGMENT");
                } else {
                    transaction.show(fm.findFragmentByTag("PLAY_FRAGMENT"));
                }
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    void updateWidget(final Mp3Info mp3Info) {
        final Handler mHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap album = MediaUtil.getAlbumArt(getContext(), mp3Info.getAlbumId());
                final String artistAndAlbum = mp3Info.getArtist() + " | " + mp3Info.getAlbum();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        playStatusBar.setAlbumPic(album);
                        playStatusBar.setArtistAndAlbumName(artistAndAlbum);
                        playStatusBar.setSongName(mp3Info.getSongName());
                    }
                });
            }
        }).start();
    }

    class PlayStatusUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Mp3Info mp3Info = (Mp3Info) intent.getExtras().get("fantasy.PLAYING_SONG");
            updateWidget(mp3Info);
        }
    }
}
