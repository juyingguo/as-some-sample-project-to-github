package com.ibotn.wifitransfer.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by jy on 2017/2/12 ;9:30.<br/>
 * ibotnphone <br/>
 *
 * @description: 后台及前台线程工具类
 */
public class ThreadUtils {

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void runOnUIThread(Runnable runnable){
        mHandler.post(runnable);
    }
    public static void runOnBackgroundThread(Runnable runnable){
        new Thread(runnable).start();
    }
    /**
     *
     * @param runnable
     * @param delay
     * 在主线程运行方法
     */
    public static void runOnUiThreadDelay(Runnable runnable, long delay){
        mHandler.postDelayed(runnable, delay);
    }
}
