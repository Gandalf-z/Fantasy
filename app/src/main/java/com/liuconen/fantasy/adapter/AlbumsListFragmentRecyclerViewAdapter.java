package com.liuconen.fantasy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.model.AlbumInfo;
import com.liuconen.fantasy.model.AsyncLoadOnScrollListener;
import com.liuconen.fantasy.model.OnRecyclerViewItemClickListener;
import com.liuconen.fantasy.util.ImageAsyncLoader;

import java.util.List;

/**
 * Created by liuconen on 2016/9/7.
 */
public class AlbumsListFragmentRecyclerViewAdapter extends RecyclerView.Adapter<AlbumsListFragmentRecyclerViewAdapter
        .MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<AlbumInfo> albumInfos;
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private ImageAsyncLoader asyncLoader;
    private Boolean firstLoad;

    public AlbumsListFragmentRecyclerViewAdapter(Context context, final RecyclerView mRecycView, final GridLayoutManager
            manager, List<AlbumInfo> albumInfos) {
        this.context = context;
        this.albumInfos = albumInfos;

        long[] albumIds = new long[albumInfos.size()];
        for (int index = 0, n = albumInfos.size(); index < n; index++) {
            albumIds[index] = albumInfos.get(index).getAlbumId();
        }
        asyncLoader = new ImageAsyncLoader(context, mRecycView, albumIds) {
            @Override
            protected void setImageBitmap(Bitmap bitmap, int visibleItemPos, RecyclerView recycView) {
                MyViewHolder viewHolder = (MyViewHolder) recycView.findViewHolderForLayoutPosition(visibleItemPos);
                if(viewHolder != null) {
                    viewHolder.albumArt.setImageBitmap(bitmap);
                }
            }

            @Override
            protected void setDefaultImage(int visibleItemPos, RecyclerView recycView) {
                MyViewHolder viewHolder = (MyViewHolder) recycView.findViewHolderForLayoutPosition(visibleItemPos);
                if(viewHolder != null) {
                    viewHolder.albumArt.setImageResource(R.drawable.default_album);
                }
            }
        };
        firstLoad = Boolean.valueOf(true);

        mRecycView.addOnScrollListener(new AsyncLoadOnScrollListener(asyncLoader, manager, firstLoad) {
            @Override
            public int findFirstVisibleItemPos(RecyclerView.LayoutManager manager) {
                return ((GridLayoutManager)manager).findFirstVisibleItemPosition();
            }

            @Override
            public int findLastVisibleItemPosition(RecyclerView.LayoutManager manager) {
                return ((GridLayoutManager)manager).findLastVisibleItemPosition();
            }
        });
    }

    @Override
    public AlbumsListFragmentRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_fragment_recycler_view_list_item,
                parent, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumsListFragmentRecyclerViewAdapter.MyViewHolder holder, int position) {
//        holder.albumArt.setTag(currentAlbumInfo.getAlbumId());
        //保存item位置
        AlbumInfo albumInfo = albumInfos.get(position);
        holder.itemView.setTag(albumInfo.getAlbumName());
    }

    @Override
    public int getItemCount() {
        return albumInfos.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemCLick(v, v.getTag());
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView albumArt;

        public MyViewHolder(View itemView) {
            super(itemView);
            albumArt = (ImageView) itemView.findViewById(R.id.iv_album_fragment_recycler_view_item_albumArt);
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
