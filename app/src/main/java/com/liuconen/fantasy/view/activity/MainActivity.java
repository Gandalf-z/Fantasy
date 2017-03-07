package com.liuconen.fantasy.view.activity;

import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.widget.Toast;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.view.fragment.main.MainFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuconen on 2016/4/13.
 */
public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //申请权限
        List<String> permissons = new ArrayList<>();
        permissons.add("android.permission.READ_EXTERNAL_STORAGE");
        permissons.add("android.permission.WRITE_EXTERNAL_STORAGE");
        permissons.add("android.permission.RECORD_AUDIO");
        while(true){
            if(!permissons.isEmpty()){
                requestPermissons(permissons);
            }else{
                break;
            }
        }

        //响应系统音量控制
        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (fm.findFragmentByTag("MAIN_FRAGMENT") == null) {
            transaction.add(R.id.main_layout, new MainFragment(), "MAIN_FRAGMENT");
        } else {
            transaction.show(fm.findFragmentByTag("MAIN_FRAGMENT"));
        }
        transaction.commit();
    }

    private void requestPermissons(List<String> permissions) {
        for (int index = 0; index < permissions.size(); index++) {
            if (ContextCompat.checkSelfPermission(this, permissions.get(index)) !=
                    PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  //仅在api23或以后
                    //当用户选择拒绝且勾选 不再提醒 选项时，该方法返回false
                    if (!shouldShowRequestPermissionRationale(permissions.get(index))) {
                        Toast.makeText(this, "你已选择不再提醒，请手动到设置中开启权限", Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{permissions.get(index)}, 1);
                    }
                }
            }else{
                permissions.remove(index);
            }
        }
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
  