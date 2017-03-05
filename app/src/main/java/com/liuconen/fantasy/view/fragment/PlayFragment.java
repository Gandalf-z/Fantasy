package com.liuconen.fantasy.view.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.model.Mp3Info;
import com.liuconen.fantasy.service.PlayService;
import com.liuconen.fantasy.util.MediaUtil;

/**
 * 播放界面
 */
public class PlayFragment extends Fragment implements View.OnClickListener {
    final static int MSG_PAUSE = 1;
    final static int MSG_PLAY = 2;
    final static int UPDATE_SEEK_BAR = 3;

    private ImageView songPic;
    private TextView songName;
    private TextView artist;

    private SeekBar seekBar;
    private ImageButton previousSong = null;
    private ImageButton play = null;
    private ImageButton nextSong = null;

    public PlayService mService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((PlayService.ServiceBinder) service).getService();
            //首次启动初始化界面
            mService.sendPlayStatusChangedBroadcast("fantasy.action.PLAY_STATUS_CHANGED");
            mHandler.sendEmptyMessage(mService.getMediaPlayer().isPlaying() ? MSG_PLAY : MSG_PAUSE);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PAUSE:
                    play.setImageResource(R.drawable.image_button_play);
                    break;
                case MSG_PLAY:
                    play.setImageResource(R.drawable.image_button_pause);
                    break;
                case UPDATE_SEEK_BAR:
                    seekBar.setProgress(mService.getMediaPlayer().getCurrentPosition());
                    break;
                default:
                    break;

            }
            super.handleMessage(msg);
        }
    };
    private PlayStatusUpdateReceiver mReceiver = new PlayStatusUpdateReceiver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //如果是安卓5.0以上透明状态栏
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getActivity().getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

        getActivity().bindService(new Intent(getContext(), PlayService.class), connection, Context
                .BIND_AUTO_CREATE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("fantasy.action.PAUSE");
        intentFilter.addAction("fantasy.action.PLAY");
        intentFilter.addAction("fantasy.action.PLAY_STATUS_CHANGED");
        getActivity().registerReceiver(mReceiver, intentFilter);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        bindWidget(view);
        addButtonOnClickListener();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_or_pause:
                mService.playOrPause();
                break;

            case R.id.btn_previous:
                mService.playPreviousSong();
                break;

            case R.id.btn_next:
                mService.playNextSong();
                break;
            default:
                break;
        }
    }

    public void bindWidget(View view) {
        songPic = (ImageView) view.findViewById(R.id.iv_album);
        songName = (TextView) view.findViewById(R.id.tv_songName);
        artist = (TextView) view.findViewById(R.id.tv_artist);

        seekBar = (SeekBar) view.findViewById(R.id.sb_play_fragment_seek_bar);
        previousSong = (ImageButton) view.findViewById(R.id.btn_previous);
        play = (ImageButton) view.findViewById(R.id.btn_play_or_pause);
        nextSong = (ImageButton) view.findViewById(R.id.btn_next);
    }

    private void addButtonOnClickListener() {
        play.setOnClickListener(this);
        nextSong.setOnClickListener(this);
        previousSong.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            //响应进度条拖动
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mService != null) {
                    int i = seekBar.getMax();
                    int j = seekBar.getProgress();
                    mService.getMediaPlayer().seekTo(seekBar.getProgress());
                }
            }
        });
    }

    class PlayStatusUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "fantasy.action.PAUSE":
                case "fantasy.action.PLAY":
                    updateWidgetStatus(intent.getAction());
                    break;
                case "fantasy.action.PLAY_STATUS_CHANGED":
                    refreshWidgetData((Mp3Info) intent.getExtras().get("fantasy.PLAYING_SONG"));
                    break;
                default:
                    break;
            }
        }
    }

    void refreshWidgetData(final Mp3Info mp3Info) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        songPic.setImageBitmap(MediaUtil.getAlbumArt(getContext(), mp3Info.getAlbumId()));
                        songName.setText(mp3Info.getSongName());
                        artist.setText(mp3Info.getArtist());
                    }
                });
                if(mService != null){
                    seekBar.setMax(mService.getMediaPlayer().getDuration());
                }
            }
        }).start();

        //更新seekbar
        if(mService != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        mHandler.sendEmptyMessage(UPDATE_SEEK_BAR);
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    void updateWidgetStatus(String broadcast) {
        if (broadcast.equals("fantasy.action.PAUSE")) {
            mHandler.sendEmptyMessage(MSG_PAUSE);
        } else if (broadcast.equals("fantasy.action.PLAY")) {
            mHandler.sendEmptyMessage(MSG_PLAY);
        }
    }
}



