/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demo.camera;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.util.Log;

import com.demo.camera.utils.LogUtils;
import com.demo.camera.utils.Storage;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * Service for saving images in the background thread.
 */
public class MediaSaveService extends Service {
    public static final String VIDEO_BASE_URI = "content://media/external/video/media";

    // The memory limit for unsaved image is 20MB.
    private static final int SAVE_TASK_MEMORY_LIMIT = 20 * 1024 * 1024;
    private static final String TAG = "CAM_" + MediaSaveService.class.getSimpleName();

    private final IBinder mBinder = new LocalBinder();
    private Listener mListener;
    // Memory used by the total queued save request, in bytes.
    private long mMemoryUse;
    ThreadPoolExecutor threadPool;
    public interface Listener {
        public void onQueueStatus(boolean full);
    }

    public interface OnMediaSavedListener {
        public void onMediaSaved(String filePath);
    }

    class LocalBinder extends Binder {
        public MediaSaveService getService() {
            return MediaSaveService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onCreate() {
        mMemoryUse = 0;
        Storage.ensureOSXCompatible();
        CreateThreadPool();
    }
    private void CreateThreadPool(){
        threadPool = new ThreadPoolExecutor(2, 5, 3,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.DiscardOldestPolicy());

    }
    public boolean isQueueFull() {
        return (mMemoryUse >= SAVE_TASK_MEMORY_LIMIT);
    }

    public void addImage(final byte[] data, String title, long date, Location loc,
                         int width, int height, int orientation,
                         OnMediaSavedListener l, ContentResolver resolver) {


        if (isQueueFull()) {
            ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memInfo=new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memInfo);
            Log.v(TAG, "availMem:"+memInfo.availMem/1024+" kb");
            Log.v(TAG, "threshold:"+memInfo.threshold/1024+" kb");//low memory threshold
            Log.v(TAG, "totalMem:"+memInfo.totalMem/1024+" kb");
            Log.v(TAG, "lowMemory:"+memInfo.lowMemory);  //if current is in low memory
            Log.e(TAG, "Cannot add image when the queue is full");
            return;
        }
        ImageSaveTask t = new ImageSaveTask(data, title, date,
                (loc == null) ? null : new Location(loc),
                width, height, orientation, resolver, l);

        mMemoryUse += data.length;
        LogUtils.e(TAG,"mMemoryUse "+mMemoryUse);
//        t.execute();
        t.executeOnExecutor(threadPool);
    }

    public void addImage(final byte[] data, String title, long date, Location loc,
                         int orientation,
                         OnMediaSavedListener l, ContentResolver resolver) {
        // When dimensions are unknown, pass 0 as width and height,
        // and decode image for width and height later in a background thread
        addImage(data, title, date, loc, 0, 0, orientation, l, resolver);
    }
    public void addImage(final byte[] data, String title, Location loc,
                         int width, int height, int orientation,
                         OnMediaSavedListener l, ContentResolver resolver) {
        addImage(data, title, System.currentTimeMillis(), loc, width, height,
                orientation, l, resolver);
    }

    public void addVideo(String path, long duration, ContentValues values,
                         OnMediaSavedListener l, ContentResolver resolver) {
        // We don't set a queue limit for video saving because the file
        // is already in the storage. Only updating the database.
        new VideoSaveTask(path, duration, values, l, resolver).execute();
    }

    public void setListener(Listener l) {
        mListener = l;
        if (l == null) return;
        l.onQueueStatus(isQueueFull());
    }

    private void onQueueFull() {
        if (mListener != null) mListener.onQueueStatus(true);
    }

    private void onQueueAvailable() {
        if (mListener != null) mListener.onQueueStatus(false);
    }

    private class ImageSaveTask extends AsyncTask<Void, Void, String> {
        private byte[] data;
        private String title;
        private long date;
        private Location loc;
        private int width, height;
        private int orientation;
        private ContentResolver resolver;
        private OnMediaSavedListener listener;

        public ImageSaveTask(byte[] data, String title, long date, Location loc,
                             int width, int height, int orientation,
                             ContentResolver resolver, OnMediaSavedListener listener) {
            this.data = data;
            this.title = title;
            this.date = date;
            this.loc = loc;
            this.width = width;
            this.height = height;
            this.orientation = orientation;
            this.resolver = resolver;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            // do nothing.
        }

        @Override
        protected String doInBackground(Void... v) {
            String filePath =null;
            if (width == 0 || height == 0) {
                // Decode bounds
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(data, 0, data.length, options);
                width = options.outWidth;
                height = options.outHeight;
            }
            Uri uri = Storage.addImage(
                    resolver, title, date, loc, orientation, data, width, height);
            //上传oneDrive
//            if (uri!=null){
//                sendBroadcastUploadPhoto(uri);
//            }
            if(uri != null)
                filePath = title;
            return filePath;
        }

        @Override
        protected void onPostExecute(String filePath) {
            if (listener != null) listener.onMediaSaved(filePath);
            boolean previouslyFull = isQueueFull();
            mMemoryUse -= data.length;
            if (isQueueFull() != previouslyFull) onQueueAvailable();
        }
    }

    private class VideoSaveTask extends AsyncTask<Void, Void, String> {
        private String path;
        private long duration;
        private ContentValues values;
        private OnMediaSavedListener listener;
        private ContentResolver resolver;

        public VideoSaveTask(String path, long duration, ContentValues values,
                             OnMediaSavedListener l, ContentResolver r) {
            this.path = path;
            this.duration = duration;
            this.values = new ContentValues(values);
            this.listener = l;
            this.resolver = r;
        }

        @Override
        protected String doInBackground(Void... v) {
            values.put(Video.Media.SIZE, new File(path).length());
            values.put(Video.Media.DURATION, duration);
            String filePath = null;
            try {
                Uri videoTable = Uri.parse(VIDEO_BASE_URI);
                Uri uri = resolver.insert(videoTable, values);

                // Rename the video file to the final name. This avoids other
                // apps reading incomplete data.  We need to do it after we are
                // certain that the previous insert to MediaProvider is completed.
                String finalName = values.getAsString(
                        Video.Media.DATA);
                if (new File(path).renameTo(new File(finalName))) {
                    path = finalName;
                }

                resolver.update(uri, values, null, null);
                filePath = path;
            } catch (Exception e) {
                // We failed to insert into the database. This can happen if
                // the SD card is unmounted.
                Log.e(TAG, "failed to add video to media store", e);
                filePath = null;
            } finally {
                Log.v(TAG, "Current video URI: " + filePath);
            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String filePath) {
            if (listener != null) listener.onMediaSaved(filePath);
        }
    }
    public String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
        String path = null;
        Cursor cursor = null;
        final String column = MediaStore.Images.ImageColumns.DATA;
        final String[] projection = {column};
        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                path = cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return path;
    }

/*    *//**
     *
     * @param uri 上传onedrive
     *//*
    private void sendBroadcastUploadPhoto(Uri uri){
        String path =getDataColumn(uri,null,null);
        Log.i(TAG,"*****path:"+ path);
        if (!TextUtils.isEmpty(path)) {
            Intent intent = new Intent(Constant.ACTION_UPLOAD_PHOTO_FOR_VOICE_TAKE_PHOTO);
            intent.putExtra(Constant.EXTRA_PHOTO_PATH_FOR_VOICE_TAKE_PHOTO, path);
            sendBroadcast(intent);
        }
    }*/
}
