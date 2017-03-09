package com.liuconen.fantasy.model;

import java.io.Serializable;

/**
 * Created by liuconen on 2016/10/24.
 */

public class AlbumInfo implements Serializable{
    private int albumId;
    private String albumName;
    private int songNumber;

    public int getAlbumId() {
        return albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public int getSongNumber() {
        return songNumber;
    }

    public AlbumInfo(int albumId, String albumName, int songNumber) {

        this.albumId = albumId;
        this.albumName = albumName;
        this.songNumber = songNumber;
    }
}
