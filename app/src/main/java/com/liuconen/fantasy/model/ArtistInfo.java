package com.liuconen.fantasy.model;

import java.io.Serializable;

/**
 * Created by liuconen on 2016/10/24.
 */

public class ArtistInfo implements Serializable{
    private int aritistId;
    private String artistName;
    private int albumNumber;
    private int songNumber;

    public ArtistInfo(int aritistId, String artistName, int albumNumber, int songNumber) {
        this.aritistId = aritistId;
        this.artistName = artistName;
        this.albumNumber = albumNumber;
        this.songNumber = songNumber;
    }

    public int getAritistId() {
        return aritistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getAlbumNumber() {
        return albumNumber;
    }

    public int getSongNumber() {
        return songNumber;
    }
}
