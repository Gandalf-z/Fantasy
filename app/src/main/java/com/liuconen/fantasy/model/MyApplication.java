package com.liuconen.fantasy.model;

import android.app.Application;

/**
 * Created by liuconen on 2016/3/30.
 * don't store public data anymore
 */
public class MyApplication extends Application {
//    List<Mp3Info> mp3Infos = null;
//
//    public void setMp3Infos(List<Mp3Info> mp3Infos) {
//        this.mp3Infos = mp3Infos;
//    }
//
//    public List<Mp3Info> getMp3Infos() {
//        return mp3Infos;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
//        mp3Infos = MediaUtil.getMp3Infos(this);
    }
}
