package com.example.liuwangshu.moonmvpsimple.ipinfo;

import android.util.Log;

import com.example.liuwangshu.moonmvpsimple.LoadTasksCallBack;
import com.example.liuwangshu.moonmvpsimple.model.IpInfo;
import com.example.liuwangshu.moonmvpsimple.net.NetTask;


/**
 * Created by Administrator on 2016/12/29 0029.
 */

public class IpInfoPresenter implements IpInfoContract.Presenter, LoadTasksCallBack<IpInfo> {
    private final String TAG = IpInfoPresenter.class.getSimpleName();
    private NetTask netTask;
    private IpInfoContract.View addTaskView;

    public IpInfoPresenter(IpInfoContract.View addTaskView, NetTask netTask) {
        this.netTask = netTask;
        this.addTaskView=addTaskView;
    }
    @Override
    public void getIpInfo(String ip) {
        if (netTask != null) netTask.execute(ip, this);
    }

    @Override
    public void cancelTask() {
        netTask.cancelTask();
    }

    @Override
    public void onSuccess(IpInfo ipInfo) {
        Log.d(TAG,"onSuccess,ipInfo:" + ipInfo);
        boolean active = addTaskView.isActive();
        Log.d(TAG,"onSuccess,addTaskView.isActive():" + active);
        if(active){
            addTaskView.setIpInfo(ipInfo);
        }
    }

    /*@Override
    public void onSuccess(String ipInfo) {
        if(addTaskView.isActive()){
            addTaskView.setIpInfo(ipInfo);
        }
    }*/

    @Override
    public void onStart() {
        if(addTaskView.isActive()){
            addTaskView.showLoading();
        }
    }

    @Override
    public void onFailed() {
        if(addTaskView.isActive()){
            addTaskView.showError();
            addTaskView.hideLoading();
        }
    }

    @Override
    public void onFinish() {
        if(addTaskView.isActive()){
            addTaskView.hideLoading();
        }
    }
}
