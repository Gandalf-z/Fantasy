package com.liuconen.fantasy.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.liuconen.fantasy.model.Mp3Info;

import java.io.IOException;
import java.util.List;

/**
 * Created by liuconen on 2016/9/24.
 */
public class PlayService extends Service {
    private static MediaPlayer mp3 = new MediaPlayer();
    private List<Mp3Info> mp3InfoList;
    private int currentSongIndex;
    private IBinder mBinder = new ServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextSong();
            }
        });
        mp3.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                playNextSong();
                return true;
            }
        });
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        //get current playlist
        List<Mp3Info> cPlayList = (List<Mp3Info>) bundle.get("fantasy.PLAY_LIST");
        if (mp3InfoList != cPlayList) {
            mp3InfoList = cPlayList;
        }
        //get song index played
        currentSongIndex = bundle.getInt("fantasy.SONG_INDEX");
        Mp3Info cSong = mp3InfoList.get(currentSongIndex);
        playSong(cSong, true);

        sendPlayStatusChangedBroadcast("fantasy.action.PLAY_STATUS_CHANGED");

        return super.onStartCommand(intent, flags, startId);
    }

    public void sendPlayStatusChangedBroadcast(String broadCastText) {
        if(mp3InfoList != null){
            Intent intent = new Intent(broadCastText);
            Bundle bundle = new Bundle();
            bundle.putParcelable("fantasy.PLAYING_SONG", mp3InfoList.get(currentSongIndex));
            intent.putExtras(bundle);
            sendBroadcast(intent);
        }
    }

    /**
     * @param currentSong 当前播放歌曲对象
     * @param playNow     设置是否立即播放
     */
    private Mp3Info playSong(Mp3Info currentSong, boolean playNow) {  //play设置是否立即播放
        try {
            mp3.reset();
            mp3.setDataSource(currentSong.getUrl());
            mp3.prepare();
            if (playNow) {
                mp3.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentSong;
    }

    public void playOrPause() {
        if (mp3.isPlaying()) {
            mp3.pause();
            sendPlayStatusChangedBroadcast("fantasy.action.PAUSE");
        } else {
            mp3.start();
            sendPlayStatusChangedBroadcast("fantasy.action.PLAY");
        }
    }


    public void playPreviousSong() {
        if (getPreviousSong() != null) {
            playSong(getPreviousSong(), mp3.isPlaying());
            //传递isPlaying判断当前播放状态，如果为播放那么切换后为继续播放
            sendPlayStatusChangedBroadcast("fantasy.action.PLAY_STATUS_CHANGED");
        }
    }

    public void playNextSong() {
        if (getNextSong() != null) {
            playSong(getNextSong(), true);
            sendPlayStatusChangedBroadcast("fantasy.action.PLAY_STATUS_CHANGED");
        }
    }

    private Mp3Info getPreviousSong() {
        Mp3Info mp3Info = null;
        if (mp3InfoList != null) {
            if (currentSongIndex > 0) {
                mp3Info = mp3InfoList.get(--currentSongIndex);
            }
        }
        return mp3Info;
    }

    private Mp3Info getNextSong() {
        Mp3Info mp3Info = null;
        if(mp3InfoList != null){
            if (currentSongIndex < mp3InfoList.size() - 1) {
                mp3Info = mp3InfoList.get(++currentSongIndex);
            }
        }
        return mp3Info;
    }

    public class ServiceBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    public MediaPlayer getMediaPlayer(){
        return mp3;
    }
}
