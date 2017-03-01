package com.liuconen.fantasy.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * store data of song
 * implements Parcelable interface to serialize
 * Created by liuconen on 2016/2/12.
 */
public class Mp3Info implements Parcelable {
    private long id; // 歌曲ID 3
    private String title; // 歌曲名称 0
    private String album; // 专辑 7
    private long albumId;//专辑ID 6
    private String displayName; //显示名称 4
    private String artist; // 歌手名称 2
    private long duration; // 歌曲时长 1
    private long size; // 歌曲大小 8
    private String url; // 歌曲路径 5
    private String lrcTitle; // 歌词名称
    private String lrcSize; // 歌词大小

    public Mp3Info(){
        super();
    }

    public Mp3Info(long id, String title, String album, long albumId,
                   String displayName, String artist, long duration, long size,
                   String url, String lrcTitle, String lrcSize) {
        super();
        this.id = id;
        this.title = title;
        this.album = album;
        this.albumId = albumId;
        this.displayName = displayName;
        this.artist = artist;
        this.duration = duration;
        this.size = size;
        this.url = url;
        this.lrcTitle = lrcTitle;
        this.lrcSize = lrcSize;
    }

    public String getSongName(){
        return title;
    }

    public String getLrcSize() {
        return lrcSize;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getArtist() {
        return artist;
    }

    public long getDuration() {
        return duration;
    }

    public long getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public String getLrcTitle() {
        return lrcTitle;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLrcTitle(String lrcTitle) {
        this.lrcTitle = lrcTitle;
    }

    public void setLrcSize(String lrcSize) {
        this.lrcSize = lrcSize;
    }

    protected Mp3Info(Parcel in) {
        id = in.readLong();
        title = in.readString();
        album = in.readString();
        albumId = in.readLong();
        displayName = in.readString();
        artist = in.readString();
        duration = in.readLong();
        size = in.readLong();
        url = in.readString();
        lrcTitle = in.readString();
        lrcSize = in.readString();
    }

    public static final Creator<Mp3Info> CREATOR = new Creator<Mp3Info>() {
        @Override
        public Mp3Info createFromParcel(Parcel in) {
            return new Mp3Info(in);
        }

        @Override
        public Mp3Info[] newArray(int size) {
            return new Mp3Info[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeLong(albumId);
        dest.writeString(displayName);
        dest.writeString(artist);
        dest.writeLong(duration);
        dest.writeLong(size);
        dest.writeString(url);
        dest.writeString(lrcTitle);
        dest.writeString(lrcSize);
    }
}
