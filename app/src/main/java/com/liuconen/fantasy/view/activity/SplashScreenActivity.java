package com.liuconen.fantasy.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.util.MediaUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by liuconen on 2017/3/7.
 */

public class SplashScreenActivity extends AppCompatActivity {
    private List<String> permissions = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //隐藏ActionBar
        getSupportActionBar().hide();
        //全屏与导航栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        //安卓M或以上版本申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.add(Manifest.permission.RECORD_AUDIO);
            permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            requestPermissions();
        } else {
            start();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions() {
        //筛选出未被授予的权限
        Iterator<String> it = permissions.iterator();
        while (it.hasNext()) {
            String permission = it.next();
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                it.remove();
            }
        }
        //如果权限都已被授予，则返回
        if (permissions.isEmpty()) {
            start();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.tip)
                    .setMessage(R.string.request_permission)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String[] per = new String[permissions.size()];
                            for(int index=0; index<permissions.size(); index++){
                                per[index] = permissions.get(index);
                            }
                            ActivityCompat.requestPermissions(SplashScreenActivity.this, per, 0);
                        }
                    }).create().show();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int index=0; index<grantResults.length; index++){
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                //由于移除后list 长度相应改变，所以此处每次都移除第一个
                SplashScreenActivity.this.permissions.remove(0);
                if (SplashScreenActivity.this.permissions.isEmpty()) {
                    start();
                }
            } else if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                if (!shouldShowRequestPermissionRationale(SplashScreenActivity.this.permissions.get(index))) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.tip)
                            .setMessage(R.string.cannot_request_permission)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SplashScreenActivity.this.finish();
                                }
                            }).create().show();
                }
            }
        }
    }

    private void start() {
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
                if ((timeLag = timeMillisAfterExecute - timeMillisPreExecute) < minTimeMillisForWait) {
                    try {
                        Thread.sleep(minTimeMillisForWait - timeLag);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //启动主界面
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                intent.putExtra("SONGINFO", (HashMap) map);
                startActivity(intent);
                finish();
            }
        }.execute();
    }
}




