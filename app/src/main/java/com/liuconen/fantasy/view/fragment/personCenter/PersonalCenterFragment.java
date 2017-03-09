package com.liuconen.fantasy.view.fragment.personCenter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.adapter.PersonCenterFragmentRecyclerViewAdapter;
import com.liuconen.fantasy.model.OnRecyclerViewItemClickListener;
import com.liuconen.fantasy.util.MediaUtil;
import com.liuconen.fantasy.util.MyDateBaseHelper;
import com.liuconen.fantasy.view.fragment.BaseFragment;
import com.liuconen.fantasy.view.fragment.DetailPageFragment;


/**
 * Created by liuconen on 2016/8/11.
 */
public class PersonalCenterFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ;

        int[] items = new int[]{R.string.person_center_favorite, R.string.person_center_my_hot_songs, R.string.person_center_recent_play};
        PersonCenterFragmentRecyclerViewAdapter mAdapter = new PersonCenterFragmentRecyclerViewAdapter(getContext(), items);
        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemCLick(View view, Object data) {
                switch ((int) data) {
                    case R.string.person_center_favorite:{
                        //从数据库查找到标注为喜爱的歌曲
                        MyDateBaseHelper helper = new MyDateBaseHelper(getContext(), "PersonalPlayInfo.db", null, 1);
                        SQLiteDatabase database = helper.getReadableDatabase();
                        Cursor cursor = database.rawQuery("select songId from PersonalPlayInfo where favorite = ?", new String[]{"1"});
                        int count = cursor.getCount();
                        long[] ids = new long[count];
                        for (int index = 0; index < count; index++) {
                            cursor.moveToNext();
                            ids[index] = cursor.getLong(cursor.getColumnIndex("songId"));
                        }
                        //判断查询结果是否为空
                        if (ids.length <= 0) {
                            Toast.makeText(getContext(), R.string.no_favorite_song, Toast.LENGTH_SHORT).show();
                        } else {
                            DetailPageFragment fragment = DetailPageFragment.newInstance(MediaUtil.getMp3InfoBysongIds(getContext(), ids),
                                    getContext().getString(R.string.person_center_favorite));
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.add(R.id.main_layout, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                        break;
                    }
                    case R.string.person_center_my_hot_songs: {
                        //默认统计前20首
                        MyDateBaseHelper helper = new MyDateBaseHelper(getContext(), "PersonalPlayInfo.db", null, 1);
                        SQLiteDatabase database = helper.getReadableDatabase();
                        Cursor cursor = database.rawQuery("select songId from PersonalPlayInfo order by times desc", null);
                        int count = cursor.getCount();
                        long[] ids;
                        if(count < 20){
                            ids = new long[count];
                            for (int index = 0; index < count; index++) {
                                cursor.moveToNext();
                                ids[index] = cursor.getLong(cursor.getColumnIndex("songId"));
                            }
                        }else{
                            ids = new long[20];
                            for (int index = 0; index < 20; index++) {
                                cursor.moveToNext();
                                ids[index] = cursor.getLong(cursor.getColumnIndex("songId"));
                            }
                        }
                        //判断查询结果是否为空
                        if (count > 0) {
                            DetailPageFragment fragment = DetailPageFragment.newInstance(MediaUtil.getMp3InfoBysongIds(getContext(), ids),
                                    getContext().getString(R.string.person_center_my_hot_songs));
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.add(R.id.main_layout, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }else{
                            Toast.makeText(getContext(), R.string.no_hot_song, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case R.string.person_center_recent_play: {
                        MyDateBaseHelper helper = new MyDateBaseHelper(getContext(), "RecentPlayRecord.db", null, 1);
                        SQLiteDatabase database = helper.getReadableDatabase();
                        Cursor cursor = database.rawQuery("select songId from RecentPlayRecord", null);
                        int count = cursor.getCount();
                        long[] ids;
                        if(count < 20){
                            ids = new long[count];
                            for (int index = 0; index < count; index++) {
                                cursor.moveToNext();
                                ids[index] = cursor.getLong(cursor.getColumnIndex("songId"));
                            }
                        }else{
                            ids = new long[20];
                            for (int index = 0; index < 20; index++) {
                                cursor.moveToNext();
                                ids[index] = cursor.getLong(cursor.getColumnIndex("songId"));
                            }
                        }
                        //判断查询结果是否为空
                        if (count > 0) {
                            DetailPageFragment fragment = DetailPageFragment.newInstance(MediaUtil.getMp3InfoBysongIds(getContext(), ids),
                                    getContext().getString(R.string.person_center_recent_play));
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.add(R.id.main_layout, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }else{
                            Toast.makeText(getContext(), R.string.no_recent_song, Toast.LENGTH_SHORT).show();                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        });
        setRecyclerViewAdapter(mAdapter);
        setRecyclerViewLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
}
