package com.liuconen.fantasy.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.model.ArtistInfo;
import com.liuconen.fantasy.model.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by liuconen on 2016/9/7.
 */
public class ArtistsListFragmentRecyclerViewAdapter extends RecyclerView.Adapter<ArtistsListFragmentRecyclerViewAdapter
        .MyViewHolder> implements View.OnClickListener{
    Fragment fragment;
    List<ArtistInfo> artistInfos = null;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public ArtistsListFragmentRecyclerViewAdapter(Fragment fragment, List<ArtistInfo> artistInfos) {
        this.fragment = fragment;
        this.artistInfos = artistInfos;
    }

    @Override
    public ArtistsListFragmentRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.artist_fragment_recycler_view_list_item,
                parent, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArtistsListFragmentRecyclerViewAdapter.MyViewHolder holder, int position) {
        ArtistInfo currentArtistInfo = artistInfos.get(position);
        holder.artistName.setText(currentArtistInfo.getArtistName());
        holder.albumsAndSongs.setText(currentArtistInfo.getAlbumNumber() + "张专辑"+ " | " + currentArtistInfo
                .getSongNumber() + "首歌曲");
        //保存item位置艺术家id
        holder.itemView.setTag(currentArtistInfo.getArtistName());
    }

    @Override
    public int getItemCount() {
        return artistInfos.size();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemCLick(v, v.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;
        TextView albumsAndSongs;

        public MyViewHolder(View itemView) {
            super(itemView);
            artistName = (TextView) itemView.findViewById(R.id.artistName);
            albumsAndSongs = (TextView) itemView.findViewById(R.id.albumsAndSongs);
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener ){
        this.mOnItemClickListener = listener;
    }
}
