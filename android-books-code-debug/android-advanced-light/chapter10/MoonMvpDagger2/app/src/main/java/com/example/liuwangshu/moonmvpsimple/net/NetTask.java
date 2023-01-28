package com.example.liuwangshu.moonmvpsimple.net;

import com.example.liuwangshu.moonmvpsimple.LoadTasksCallBack;
import com.example.liuwangshu.moonmvpsimple.ipinfo.IpInfoContract;
import com.example.liuwangshu.moonmvpsimple.model.IpInfo;

import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2016/12/29 0029.
 */

public interface NetTask<T> {
    Subscription execute(T data , LoadTasksCallBack callBack);
}
