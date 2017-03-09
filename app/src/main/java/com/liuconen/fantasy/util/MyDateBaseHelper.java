package com.liuconen.fantasy.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liuconen on 2017/3/8.
 */

public class MyDateBaseHelper extends SQLiteOpenHelper {

    private static final String PERSONAL_PLAY_INFO_TABLE = "create table PersonalPlayInfo(" +
            "id integer primary key autoincrement," +
            "songId integer," +            //歌曲id， 歌曲身份的标识
            "favorite integer," +          //是否添加为红心歌曲，1为是，0为否
            "times integer)";             //播放次数
    private static final String RECENT_PLAY_RECORD_TABLE = "create table RecentPlayRecord(songId integer)";
    private String doTAble;

    public MyDateBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        doTAble = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (doTAble.equals("PersonalPlayInfo.db")) {
            db.execSQL(PERSONAL_PLAY_INFO_TABLE);
        }else if(doTAble.equals("RecentPlayRecord.db")){
            db.execSQL(RECENT_PLAY_RECORD_TABLE);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
