package com.liuconen.fantasy.view.activity;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.view.fragment.main.MainFragment;

import java.util.Map;

/**
 * Created by liuconen on 2016/4/13.
 */
public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //响应系统音量控制
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //取得启动页预加载的数据
        Map songInfo = (Map) getIntent().getSerializableExtra("SONGINFO");
        MainFragment mainFragment = MainFragment.newInstance(songInfo);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (fm.findFragmentByTag("MAIN_FRAGMENT") == null) {
            transaction.add(R.id.main_layout, mainFragment, "MAIN_FRAGMENT");
        } else {
            transaction.show(fm.findFragmentByTag("MAIN_FRAGMENT"));
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        //监听Play_fragment的返回键，避免销毁
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag("PLAY_FRAGMENT");
        if (fragment != null && fragment.isVisible()) {
            transaction.hide(fragment);
            transaction.commit();
        } else {
            super.onBackPressed();
        }
    }
}
  