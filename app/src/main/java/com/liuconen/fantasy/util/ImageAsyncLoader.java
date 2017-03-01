package com.liuconen.fantasy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liuconen on 2017/2/24.
 */

public abstract class ImageAsyncLoader {

    private Context context;
    private RecyclerView recycView;
    private long[] imagePaths;

    //内存缓存,数据量太大且更新不是很快时建议使用磁盘缓存
    private LruCache<Long, Bitmap> cache;
    //task集合，用来管理当前正在执行中的task
    private Set<AsyncLoaderTask> tasks;

    public ImageAsyncLoader(Context context, RecyclerView recycView, long[] imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
        this.recycView = recycView;

        tasks = new HashSet<>();
        //分配大小为可用内存从八分之一
        cache = new LruCache<Long, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 8)) {
            @Override
            protected int sizeOf(Long key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void load(int startPos, int endPos) {
        //后续考虑使用线程池管理
        for (int index = startPos, visibleItemPos = startPos; index <= endPos; index++, visibleItemPos++) {
            AsyncLoaderTask task = new AsyncLoaderTask(imagePaths[index], visibleItemPos);
            tasks.add(task);
            task.execute();
        }
    }

    public void cancelAllLoadingTask() {
        for (AsyncLoaderTask task : tasks) {
            task.cancel(true);
        }
    }

    public void loadDefaultImage(int startPos, int endPos){
        for (int index = startPos; index <= endPos; index++) {
            setDefaultImage(index, recycView);
        }
    }

    class AsyncLoaderTask extends AsyncTask<Long, Void, Bitmap> {

        private Long imagePath;
        private int visibleItemPosition;


        public AsyncLoaderTask(Long imagePath, int visibleItemPosition) {
            this.imagePath = imagePath;
            this.visibleItemPosition = visibleItemPosition;
        }

        @Override
        protected Bitmap doInBackground(Long... params) {
            //模拟网络请求等耗时操作
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            //如果缓存存在直接取出
            if (cache.get(imagePath) != null) {
                return cache.get(imagePath);
            } else {
                Bitmap bitmap = MediaUtil.getAlbumArtByAlbumId(context, imagePath);
                if (bitmap != null) {
                    cache.put(imagePath, bitmap);
                    return bitmap;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if(bitmap != null){
                setImageBitmap(bitmap, visibleItemPosition, recycView);
            }else{

            }
            //从tasks中移除已完成的task
            tasks.remove(this);
        }
    }
    protected abstract void setImageBitmap(Bitmap bitmap, int visibleItemPos, RecyclerView recycView);
    protected abstract void setDefaultImage(int visibleItemPos, RecyclerView recycView);
}
