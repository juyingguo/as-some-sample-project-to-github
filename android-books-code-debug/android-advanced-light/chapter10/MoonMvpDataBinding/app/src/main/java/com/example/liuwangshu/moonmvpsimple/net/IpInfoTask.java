package com.example.liuwangshu.moonmvpsimple.net;

import android.util.Log;

import com.example.liuwangshu.moonmvpsimple.LoadTasksCallBack;
import com.example.liuwangshu.moonmvpsimple.model.IpInfo;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

/**
 * Created by Administrator on 2016/12/29 0029.
 */

public class IpInfoTask implements NetTask<String> {
    private static final String TAG = "IpInfoTask";
    private static IpInfoTask INSTANCE = null;
    private static final String HOST = "https://ip.taobao.com/outGetIpInfo";
    private LoadTasksCallBack loadTasksCallBack;

    private IpInfoTask() {

    }

    public static IpInfoTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IpInfoTask();
        }
        return INSTANCE;
    }

    @Override
    public void execute(String ip, final LoadTasksCallBack loadTasksCallBack) {
        Log.i(TAG,"execute,enter,ip:" + ip);
        RequestParams requestParams = new RequestParams();
        requestParams.addFormDataPart("ip", ip);
        HttpRequest.get(HOST, requestParams, new BaseHttpRequestCallback</*String*/IpInfo>() {
            @Override
            public void onStart() {
                super.onStart();
                loadTasksCallBack.onStart();
            }

            @Override
            protected void onSuccess(IpInfo ipInfo) {
                super.onSuccess(ipInfo);
                Log.i(TAG,"onSuccess,ipInfo:" + ipInfo);
                loadTasksCallBack.onSuccess(ipInfo);
            }

            /*@Override
            protected void onSuccess(String s) {
                super.onSuccess(s);
                loadTasksCallBack.onSuccess(s);
            }*/

            @Override
            public void onFinish() {
                super.onFinish();
                loadTasksCallBack.onFinish();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Log.i(TAG,"onFailure,errorCode:" + errorCode + ",msg:" + msg);
                loadTasksCallBack.onFailed();
            }
        });
    }
}


