package com.liuconen.fantasy.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.util.MediaUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuconen on 2017/3/7.
 */

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //隐藏ActionBar
        getSupportActionBar().hide();

        //全屏与导航栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        new AsyncTask<Void, Void, Map>() {
            //设置最短等待时间
            private long minTimeMillisForWait = 3000;

            private long timeMillisPreExecute;
            private long timeMillisAfterExecute;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                timeMillisPreExecute = System.currentTimeMillis();
            }

            //预加载数据
            @Override
            protected Map doInBackground(Void... params) {
                Map<String, List> songInfoMap = new HashMap<>();
                songInfoMap.put("MP3INFO", MediaUtil.getMp3Infos(SplashScreenActivity.this));
                songInfoMap.put("ARTIINFO", MediaUtil.getArtistInfos(SplashScreenActivity.this));
                songInfoMap.put("ALBUMINFO", MediaUtil.getAlbumInfos(SplashScreenActivity.this));
                return songInfoMap;
            }

            @Override
            protected void onPostExecute(Map map) {
                super.onPostExecute(map);
                timeMillisAfterExecute = System.currentTimeMillis();
                long timeLag;

                //当doInBackground方法的执行时间少于最小显示时间时，睡眠以补足时间至最少显示时间；当doInBackground方法的执行时间多于最小显示时间时，实际显示时间为doInBackground方法的执行时间
                if((timeLag = timeMillisAfterExecute - timeMillisPreExecute) < minTimeMillisForWait){
                    try {
                        Thread.sleep(minTimeMillisForWait - timeLag);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //启动主界面
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                intent.putExtra("SONGINFO", (HashMap)map);
                startActivity(intent);
                finish();
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(0, R.anim.zoomin);
    }
}

