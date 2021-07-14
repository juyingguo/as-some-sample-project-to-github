package com.demo.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class OpenAppTest {

    private final String TAG=getClass().getName();

    @Test
    public void case1(){
        UiDevice mDevice=UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());//获取设备用例

        try {
            if(!mDevice.isScreenOn()){  
                mDevice.wakeUp();//唤醒屏幕
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //mDevice.pressHome();  //按home键

//        String mPackageName = "com.android.contacts";
        String mPackageName = "com.android.camera2";
        startAPP(mPackageName);  //启动app
        mDevice.waitForWindowUpdate(mPackageName, 10 * 2000);//等待app
        closeAPP(mDevice, mPackageName);//关闭app
    }

    @Test
    public void demo() throws UiObjectNotFoundException {
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();
        mDevice.pressHome();
        UiObject x=mDevice.findObject(new UiSelector().text("相机"));
        x.click();

    }

    private void startAPP(String sPackageName){
        Context mContext = InstrumentationRegistry.getInstrumentation().getContext();

        Intent myIntent = mContext.getPackageManager().getLaunchIntentForPackage(sPackageName);  //通过Intent启动app
        mContext.startActivity(myIntent);
    }

    private void closeAPP(UiDevice uiDevice,String sPackageName){
        Log.i(TAG, "closeAPP: ");
        try {
            uiDevice.executeShellCommand("am force-stop "+sPackageName);//通过命令行关闭app
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startAPP(UiDevice uiDevice,String sPackageName, String sLaunchActivity){
        try {
            uiDevice.executeShellCommand("am start -n "+sPackageName+"/"+sLaunchActivity);//通过命令行启动app
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}