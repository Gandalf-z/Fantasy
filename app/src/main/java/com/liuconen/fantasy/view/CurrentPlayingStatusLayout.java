package com.liuconen.fantasy.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuconen.fantasy.R;

/**
 * Created by liuconen on 2016/9/27.
 */
public class CurrentPlayingStatusLayout extends RelativeLayout {

    private ImageView album;
    private TextView songName;
    private TextView artistAndAlbumName;

    public CurrentPlayingStatusLayout(Context context) {
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.current_playing_status, this);
        initWiget(v);
    }

    public CurrentPlayingStatusLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View v = LayoutInflater.from(context).inflate(R.layout.current_playing_status, this);
        initWiget(v);
    }

    void initWiget(View v){
        album = (ImageView) v.findViewById(R.id.iv_play_status_bar_album);
        songName = (TextView) v.findViewById(R.id.tv_play_status_bar_songName);
        artistAndAlbumName = (TextView) v.findViewById(R.id.tv_play_status_bar_artistAndAlbumName);
    }
    public void setAlbumPic(Bitmap albumPic){
        this.album.setImageBitmap(albumPic);
    }

    public void setSongName(String songName){
        this.songName.setText(songName);
    }

    public void setArtistAndAlbumName(String artistAndAlbumName){
        this.artistAndAlbumName.setText(artistAndAlbumName);
    }
}
