package com.liuconen.fantasy.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuconen.fantasy.R;

/**
 * Created by liuconen on 2016/12/26.
 */

public class BaseFragment extends Fragment {
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_base, container, false);
        return mRecyclerView;
    }

    protected void setRecyclerViewAdapter(RecyclerView.Adapter mAdapter) {
        mRecyclerView.setAdapter(mAdapter);
    }

    protected void setRecyclerViewLayoutManager(RecyclerView.LayoutManager mLayoutManager) {
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    protected void addRecyclerViewItemDecoration(RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor);
    }

    protected RecyclerView getRecyclerView(){
        return mRecyclerView;
    }
}
