package com.liuconen.fantasy.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.liuconen.fantasy.model.Mp3Info;
import com.liuconen.fantasy.util.MyDateBaseHelper;

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
                Toast.makeText(getApplicationContext(), "播放错误", Toast.LENGTH_SHORT);
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
        if (mp3InfoList != null) {
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
                insertPlayRecord(currentSong.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentSong;
    }

    //记录歌曲播放信息
    private void insertPlayRecord(final long id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDateBaseHelper mDataBaseHelper = new MyDateBaseHelper(getApplicationContext(), "PersonalPlayInfo.db", null, 1);
                SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
                String strId = Long.toString(id);
                /**
                 * 更新歌曲播放次数
                 */
                //首先判断歌曲是否已有播放记录，有直接增加播放次数，没有添加记录并播放次数为1
                Cursor cursor = db.rawQuery("select * from PersonalPlayInfo where songId = ?", new String[]{strId});
                if (cursor.moveToFirst()) {     //如果当前歌曲播放记录已存在
                    db.execSQL("update PersonalPlayInfo set times = times + 1 where songId = ?", new String[]{strId});
                } else {    //如果歌曲播放记录还从未添加
                    db.execSQL("insert into PersonalPlayInfo(songId, favorite, times) values(?, ?, ?)",
                            new String[]{strId, "0", "1"});
                }
                //关闭数据库
                db.close();
                mDataBaseHelper.close();

                /**
                 * 加入最近播放记录，默认保存20条记录
                 */
                mDataBaseHelper = new MyDateBaseHelper(getApplicationContext(), "RecentPlayRecord.db", null, 1);
                db = mDataBaseHelper.getWritableDatabase();
                //先判断记录是否已存在
                cursor = db.rawQuery("select * from RecentPlayRecord where songId = ?", new String[]{strId});
                if(cursor.moveToFirst()){
                    db.execSQL("delete from RecentPlayRecord where songId = ?", new String[]{strId});
                    db.execSQL("insert into RecentPlayRecord values(?)", new String[]{strId});
                }else{
                    //再判断当前记录数
                    cursor = db.rawQuery("select count(*) from RecentPlayRecord", null);
                    int count = cursor.getCount();
                    if (count < 20) {
                        //直接插入当前记录
                        db.execSQL("insert into RecentPlayRecord(songId) values(?)", new String[]{strId});
                    } else {
                        db.execSQL("insert into RecentPlayRecord(songId) values(?)", new String[]{strId});
                        //删除第一条记录已维持总记录树为20
                        db.execSQL("delete from RecentPlayRecord where songId in(select songId from RecentPlayRecord limit ?)", new String[]{"1"});
                    }
                }
                db.close();
                mDataBaseHelper.close();
            }
        }).start();

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
        if (mp3InfoList != null) {
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

    public MediaPlayer getMediaPlayer() {
        return mp3;
    }
}
