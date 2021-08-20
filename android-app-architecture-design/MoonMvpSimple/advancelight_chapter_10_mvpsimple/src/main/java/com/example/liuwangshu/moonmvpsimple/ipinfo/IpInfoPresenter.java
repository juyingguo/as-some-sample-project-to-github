package com.example.liuwangshu.moonmvpsimple.ipinfo;

import com.example.liuwangshu.moonmvpsimple.LoadTasksCallBack;
import com.example.liuwangshu.moonmvpsimple.net.NetTask;


/**
 * Created by Administrator on 2016/12/29 0029.
 */

public class IpInfoPresenter implements IpInfoContract.Presenter, LoadTasksCallBack<String> {
    private NetTask<String> netTask;
    private IpInfoContract.View addTaskView;

    IpInfoPresenter(IpInfoContract.View addTaskView, NetTask<String> netTask) {
        this.netTask = netTask;
        this.addTaskView=addTaskView;
    }
    @Override
    public void getIpInfo(String ip) {
        if (netTask != null) netTask.execute(ip, this);
    }

    /*@Override
    public void onSuccess(IpInfo ipInfo) {
        if(addTaskView.isActive()){
            addTaskView.setIpInfo(ipInfo);
        }
    }*/

    @Override
    public void onSuccess(String ipInfo) {
        if(addTaskView.isActive()){
            addTaskView.setIpInfo(ipInfo);
        }
    }

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
