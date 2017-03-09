package com.liuconen.fantasy.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.model.AlbumInfo;
import com.liuconen.fantasy.model.ArtistInfo;
import com.liuconen.fantasy.model.Mp3Info;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 从数据库获取数据的相关方法类
 * Created by liuconen on 2016/3/18.
 */
public class MediaUtil {
    //获取专辑封面的Uri
    private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");

    /**
     * 用于从数据库中查询歌曲的信息，保存在List当中
     *
     * @return Mp3Info集合
     */
    public static ArrayList<Mp3Info> getMp3Infos(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        ArrayList<Mp3Info> mp3Infos = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            Mp3Info mp3Info = new Mp3Info();
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));    //音乐id
            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE))); // 音乐标题
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST)); // 艺术家
            String album = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM));    //专辑
            String displayName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            long albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // 是否为音乐
            if (isMusic != 0) { // 只把音乐添加到集合当中
                mp3Info.setId(id);
                mp3Info.setTitle(title);
                mp3Info.setArtist(artist);
                mp3Info.setAlbum(album);
                mp3Info.setDisplayName(displayName);
                mp3Info.setAlbumId(albumId);
                mp3Info.setDuration(duration);
                mp3Info.setSize(size);
                mp3Info.setUrl(url);
                mp3Infos.add(mp3Info);
            }
        }
        return mp3Infos;
    }

    public static ArrayList<ArtistInfo> getArtistInfos(Context context) {
        ArrayList<ArtistInfo> artistInfos = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
        int artistCount = cursor.getCount();
        for (int i = 0; i < artistCount; i++) {
            cursor.moveToNext();
            int artistId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
            String artistName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
            int albumNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists
                    .NUMBER_OF_ALBUMS));
            int songNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists
                    .NUMBER_OF_ALBUMS));
            artistInfos.add(new ArtistInfo(artistId, artistName, albumNumber, songNumber));
        }
        return artistInfos;
    }

    public static ArrayList<AlbumInfo> getAlbumInfos(Context context) {
        ArrayList<AlbumInfo> albumInfos = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        int albumCount = cursor.getCount();
        for (int i = 0; i < albumCount; i++) {
            cursor.moveToNext();
            int albumId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
            String albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
            int songNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
            albumInfos.add(new AlbumInfo(albumId, albumName, songNumber));
        }
        return albumInfos;
    }

    /**
     * 获取专辑封面图片
     *
     * @param context
     * @param songID  歌曲ID
     * @return
     */
    public static Bitmap getAlbumArt(Context context, long songID) {
        Uri uri = ContentUris.withAppendedId(albumArtUri, songID);
        ContentResolver res = context.getContentResolver();
        InputStream in = null;
        try {
            in = res.openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap artwork;
        if (in != null) {
            artwork = BitmapFactory.decodeStream(in);
        } else {
            artwork = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_album);
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //未查询到封面则设置为默认封面
        return artwork;
    }

    public static Bitmap getAlbumArtByAlbumId(Context context, Long albumId) {
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);
        ContentResolver res = context.getContentResolver();
        InputStream in = null;
        try {
            in = res.openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap artwork = BitmapFactory.decodeStream(in);
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return artwork;
    }

    public static ArrayList<Mp3Info> getMp3InfoByArtist(Context context, String artist) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.ARTIST + "=?",
                new String[]{artist},
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        ArrayList<Mp3Info> mp3Infos = new ArrayList<>();
        int songNumberCount = cursor.getCount();
        for (int i = 0; i < songNumberCount; i++) {
            cursor.moveToNext();
            Mp3Info mp3Info = new Mp3Info();
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));    //音乐id
            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE))); // 音乐标题
            String album = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM));    //专辑
            String displayName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            long albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // 是否为音乐
            if (isMusic != 0) { // 只把音乐添加到集合当中
                mp3Info.setId(id);
                mp3Info.setTitle(title);
                mp3Info.setArtist(artist);
                mp3Info.setAlbum(album);
                mp3Info.setDisplayName(displayName);
                mp3Info.setAlbumId(albumId);
                mp3Info.setDuration(duration);
                mp3Info.setSize(size);
                mp3Info.setUrl(url);
                mp3Infos.add(mp3Info);
            }
        }
        return mp3Infos;
    }

    public static List<Mp3Info> getMp3InfoByAlbumId(Context context, long albumId) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.ALBUM_ID + "=?",
                new String[]{Long.toString(albumId)},
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        List<Mp3Info> mp3Infos = new ArrayList<>();
        int songNumberCount = cursor.getCount();
        for (int i = 0; i < songNumberCount; i++) {
            cursor.moveToNext();
            Mp3Info mp3Info = new Mp3Info();
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));    //音乐id
            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE))); // 音乐标题
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST)); // 艺术家
            String album = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM));    //专辑
            String displayName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // 是否为音乐
            if (isMusic != 0) { // 只把音乐添加到集合当中
                mp3Info.setId(id);
                mp3Info.setTitle(title);
                mp3Info.setArtist(artist);
                mp3Info.setAlbum(album);
                mp3Info.setDisplayName(displayName);
                mp3Info.setAlbumId(albumId);
                mp3Info.setDuration(duration);
                mp3Info.setSize(size);
                mp3Info.setUrl(url);
                mp3Infos.add(mp3Info);
            }
        }
        return mp3Infos;
    }

    public static ArrayList<Mp3Info> getMp3InfoByAlbumName(Context context, String albumName) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.ALBUM + "=?",
                new String[]{albumName},
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        ArrayList<Mp3Info> mp3Infos = new ArrayList<>();
        int songNumberCount = cursor.getCount();
        for (int i = 0; i < songNumberCount; i++) {
            cursor.moveToNext();
            Mp3Info mp3Info = new Mp3Info();
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));    //音乐id
            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE))); // 音乐标题
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST)); // 艺术家
//            String album = cursor.getString(cursor
//                    .getColumnIndex(MediaStore.Audio.Media.ALBUM));    //专辑
            String displayName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // 是否为音乐
            if (isMusic != 0) { // 只把音乐添加到集合当中
                mp3Info.setId(id);
                mp3Info.setTitle(title);
                mp3Info.setArtist(artist);
                mp3Info.setAlbum(albumName);
                mp3Info.setDisplayName(displayName);
                mp3Info.setAlbumId(id);
                mp3Info.setDuration(duration);
                mp3Info.setSize(size);
                mp3Info.setUrl(url);
                mp3Infos.add(mp3Info);
            }
        }
        return mp3Infos;
    }

    public static ArrayList<Mp3Info> getMp3InfoBysongIds(Context context, long[] ids) {
        ArrayList<Mp3Info> mp3Infos = new ArrayList<>();
        for (int index = 0; index < ids.length; index++) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null,
                    MediaStore.Audio.Media._ID + "=?",
                    new String[]{Long.toString(ids[index])},
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

            if (cursor.moveToFirst()) {
                Mp3Info mp3Info = new Mp3Info();
                long id = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Audio.Media._ID));    //音乐id
                String title = cursor.getString((cursor
                        .getColumnIndex(MediaStore.Audio.Media.TITLE))); // 音乐标题
                String artist = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.ARTIST)); // 艺术家
                String album = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.ALBUM));    //专辑
                String displayName = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                long duration = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
                long size = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
                String url = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
                int isMusic = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // 是否为音乐
                if (isMusic != 0) { // 只把音乐添加到集合当中
                    mp3Info.setId(id);
                    mp3Info.setTitle(title);
                    mp3Info.setArtist(artist);
                    mp3Info.setAlbum(album);
                    mp3Info.setDisplayName(displayName);
                    mp3Info.setAlbumId(id);
                    mp3Info.setDuration(duration);
                    mp3Info.setSize(size);
                    mp3Info.setUrl(url);
                    mp3Infos.add(mp3Info);
                }
            }
        }
        return mp3Infos;
    }
}
