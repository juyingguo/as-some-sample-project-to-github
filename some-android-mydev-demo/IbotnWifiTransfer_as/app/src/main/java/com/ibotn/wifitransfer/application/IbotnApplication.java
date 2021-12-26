package com.ibotn.wifitransfer.application;

import android.app.Application;
/**
 * create by jy on 2018/8/21 ,15:30
 * des:
 */
public class IbotnApplication extends Application{

    private static IbotnApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static IbotnApplication getInstance() {
        return mInstance;
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
