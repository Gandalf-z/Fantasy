package com.liuconen.fantasy.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by liuconen on 2016/9/7.
 */
public class MainFragmentViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;
    List<String> pagerTitles;

    public MainFragmentViewPagerAdapter(FragmentManager fm, List<String> pagerTitles, List<Fragment> fragments) {
        super(fm);
        this.pagerTitles = pagerTitles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagerTitles.get(position);
    }
}
