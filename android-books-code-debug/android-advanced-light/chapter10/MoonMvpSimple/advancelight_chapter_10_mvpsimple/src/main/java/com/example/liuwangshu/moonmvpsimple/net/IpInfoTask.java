package com.example.liuwangshu.moonmvpsimple.net;

import android.util.Log;

import com.example.liuwangshu.moonmvpsimple.LoadTasksCallBack;
import com.example.liuwangshu.moonmvpsimple.model.IpInfo;
import com.example.liuwangshu.moonmvpsimple.util.GsonUtil;
import com.google.gson.Gson;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

/**
 * Created by Administrator on 2016/12/29 0029.
 */

public class IpInfoTask implements NetTask<String> {
    private static final String TAG = "IpInfoTask";
    private static IpInfoTask INSTANCE = null;
//    private static final String HOST = "http://ip.taobao.com/service/getIpInfo.php";
//    private static final String HOST = "https://ip.taobao.com/outGetIpInfo";
    private static final String HOST = "https://ip.useragentinfo.com/json";
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
        Log.d(TAG,"execute,ip:" + ip);
        final RequestParams requestParams = new RequestParams();
        requestParams.addFormDataPart("ip", ip);
        /**
         * com.example.liuwangshu.moonmvpsimple.net.IpInfoTask: execute,onFailure,errorCode:1002,msg:Data parse exception
         */
        /*HttpRequest.get(HOST, requestParams, new BaseHttpRequestCallback<IpInfo>() {
            @Override
            public void onStart() {
                super.onStart();
                loadTasksCallBack.onStart();
            }

            @SuppressLint("LongLogTag")
            @Override
            protected void onSuccess(IpInfo ipInfo) {
                super.onSuccess(ipInfo);
                Log.d(TAG,"execute,onSuccess,ipInfo:" + ipInfo);
                loadTasksCallBack.onSuccess(ipInfo);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                loadTasksCallBack.onFinish();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Log.d(TAG,"execute,onFailure,errorCode:" + errorCode + ",msg:" + msg);
                loadTasksCallBack.onFailed();
            }
        });*/
        HttpRequest.get(HOST, requestParams, new BaseHttpRequestCallback<String>() {
            @Override
            public void onStart() {
                super.onStart();
                loadTasksCallBack.onStart();
            }

            @Override
            protected void onSuccess(String s) {
                super.onSuccess(s);
                Log.d(TAG,"execute,onSuccess,result:" + s);
                Log.d(TAG,"execute,onSuccess,call [SystemClock.sleep(3000)].");
                IpInfo ipInfo = GsonUtil.toBean(s,IpInfo.class);
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
                Log.d(TAG,"execute,onFailure,errorCode:" + errorCode + ",msg:" + msg);
                loadTasksCallBack.onFailed();
            }
        });
    }

    @Override
    public void cancelTask() {
        Log.d(TAG,"cancelTask");
        HttpRequest.cancel(HOST);//模拟取消请求
    }
}


