package com.liuconen.fantasy.model;

import android.support.v7.widget.RecyclerView;

import com.liuconen.fantasy.util.ImageAsyncLoader;

/**
 * Created by liuconen on 2017/2/28.
 */

public abstract class AsyncLoadOnScrollListener extends RecyclerView.OnScrollListener {

    private int oldfirstItem, oldLastItem;

    private ImageAsyncLoader asyncLoader;
    private RecyclerView.LayoutManager manager;
    private Boolean firstLoad;

    public AsyncLoadOnScrollListener(ImageAsyncLoader asyncLoader, RecyclerView.LayoutManager manager, Boolean
            firstLoad) {
        this.asyncLoader = asyncLoader;
        this.manager = manager;
        this.firstLoad = firstLoad;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        //停止滑动时加载所有可见项
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            asyncLoader.load(findFirstVisibleItemPos(manager), findLastVisibleItemPosition(manager));
            updatePositon();
        } else {
            //正在滑动时取消加载任务
            asyncLoader.cancelAllLoadingTask();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //首次启动初始化可见的item
        if (firstLoad) {
            updatePositon();
            asyncLoader.load(oldfirstItem, oldLastItem);
            firstLoad = Boolean.valueOf(false);
        }
        //上滑过程中加载默认图片
        if (dy < 0) {
            asyncLoader.loadDefaultImage(findFirstVisibleItemPos(manager), oldfirstItem);
        }
        //下滑
        else if (dy > 0) {
            asyncLoader.loadDefaultImage(oldLastItem, findLastVisibleItemPosition(manager));
        }
    }

    private void updatePositon(){
        oldfirstItem = findFirstVisibleItemPos(manager);
        oldLastItem = findLastVisibleItemPosition(manager);
    }

    //由于LayoutManager不具有findVisibleItem等系列方法，让调用处根据情况实现
    public abstract int findFirstVisibleItemPos(RecyclerView.LayoutManager manager);
    public abstract int findLastVisibleItemPosition(RecyclerView.LayoutManager manager);
}
