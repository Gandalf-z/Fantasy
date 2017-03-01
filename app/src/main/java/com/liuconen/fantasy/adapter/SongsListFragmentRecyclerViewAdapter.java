package com.liuconen.fantasy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.model.AsyncLoadOnScrollListener;
import com.liuconen.fantasy.model.Mp3Info;
import com.liuconen.fantasy.model.OnRecyclerViewItemClickListener;
import com.liuconen.fantasy.util.ImageAsyncLoader;

import java.util.List;

/**
 * Created by liuconen on 2016/9/7.
 */
public class SongsListFragmentRecyclerViewAdapter extends RecyclerView.Adapter<SongsListFragmentRecyclerViewAdapter
        .MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<Mp3Info> mp3InfoList;
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private ImageAsyncLoader asyncLoader;
    private Boolean firstLoad;

    public SongsListFragmentRecyclerViewAdapter(Context context, List<Mp3Info> mp3InfoList, RecyclerView mRecycView,
                                                final LinearLayoutManager manager) {
        this.context = context;
        this.mp3InfoList = mp3InfoList;

        //取出所有albumId
        long[] albumIds = new long[mp3InfoList.size()];
        for (int index = 0, n = mp3InfoList.size(); index < n; index++) {
            albumIds[index] = mp3InfoList.get(index).getAlbumId();
        }
        asyncLoader = new ImageAsyncLoader(context, mRecycView, albumIds) {
            @Override
            protected void setImageBitmap(Bitmap bitmap, int visibleItemPos, RecyclerView recycView) {
                MyViewHolder viewHolder = (MyViewHolder) recycView.findViewHolderForLayoutPosition(visibleItemPos);
                if (viewHolder != null) {
                    viewHolder.albumPic.setImageBitmap(bitmap);
                }
            }

            @Override
            protected void setDefaultImage(int visibleItemPos, RecyclerView recycView) {
                MyViewHolder viewHolder = (MyViewHolder) recycView.findViewHolderForLayoutPosition(visibleItemPos);
                if (viewHolder != null) {
                    viewHolder.albumPic.setImageResource(R.drawable.default_album);
                }
            }
        };
        firstLoad = true;

        mRecycView.addOnScrollListener(new AsyncLoadOnScrollListener(asyncLoader, manager, firstLoad) {
            @Override
            public int findFirstVisibleItemPos(RecyclerView.LayoutManager manager) {
                return ((LinearLayoutManager)manager).findFirstVisibleItemPosition();
            }

            @Override
            public int findLastVisibleItemPosition(RecyclerView.LayoutManager manager) {
                return ((LinearLayoutManager)manager).findLastVisibleItemPosition();
            }
        });
    }

    @Override
    public SongsListFragmentRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_list_item, parent, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongsListFragmentRecyclerViewAdapter.MyViewHolder holder, int position) {
        Mp3Info currentMp3Info = mp3InfoList.get(position);
        //存了tag以便RecyclerView能找到该view
//        holder.albumPic.setTag(currentMp3Info.getAlbumId());
        holder.songName.setText(currentMp3Info.getSongName());
        holder.artistAndAlbumName.setText(currentMp3Info.getArtist() + " | " + currentMp3Info.getAlbum());
        //保存item位置
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mp3InfoList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemCLick(v, v.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView albumPic;
        TextView songName;
        TextView artistAndAlbumName;

        public MyViewHolder(View itemView) {
            super(itemView);
            albumPic = (ImageView) itemView.findViewById(R.id.albumPic);
            songName = (TextView) itemView.findViewById(R.id.songName);
            artistAndAlbumName = (TextView) itemView.findViewById(R.id.artistAndAlbumName);
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
