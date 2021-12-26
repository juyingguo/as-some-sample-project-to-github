package com.ibotn.wifitransfer.handle;

import android.text.TextUtils;

import com.ibotn.wifitransfer.eventbean.MessageEvent;
import com.ibotn.wifitransfer.utils.IbotnConstants;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * create by jy on 2018/8/20 ,15:13
 * des:
 */
public class WifiTransferConnectionManager {

    private static final String TAG = WifiTransferConnectionManager.class.getSimpleName();
    private WifiTransferConnectionManager(){}
    private static WifiTransferConnectionManager instance;
    /** Returns singleton class instance */
    public static WifiTransferConnectionManager getInstance() {
        if (instance == null) {
            synchronized (WifiTransferConnectionManager.class) {
                if (instance == null) {
                    instance = new WifiTransferConnectionManager();
                }
            }
        }
        return instance;
    }

    /**
     * 服务端地址
     */
    private LinkedList<String> addressesForServer = new LinkedList<String>();
    /**
     * 获取服务端ip地址集合
     * @return LinkedList<String>
     */
    public synchronized LinkedList<String> getAddressesForServer() {
        return addressesForServer;
    }

    /**
     * 保存服务端ip到集合中的首位。并使用eventbus发出通知
     * @param ip ip address
     */
    public synchronized void setAddressesForServer(String ip) {
        if (TextUtils.isEmpty(ip))   return;

        if (!addressesForServer.contains(ip)){
            addressesForServer.addFirst(ip);
        }

        EventBus.getDefault().post(new MessageEvent(IbotnConstants.StateConstants.DETECT_DEVICE_SERVER_IP_CHANGE.name()));
    }

    /**
     *
     * @return the first ip in LinkedList<String>;or null of no element in list;
     */
    public synchronized String getFirstIpAddress() {
        String firstIp = null;
        try {
            LinkedList<String> addressesForServer = getAddressesForServer();
            if (addressesForServer != null){
                firstIp = addressesForServer.getFirst();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firstIp;
    }

    /**
     * 清空服务端ip地址集合.并使用eventbus发出通知
     */
    public synchronized void clearAddressesForServer(){
        addressesForServer.clear();
        EventBus.getDefault().post(new MessageEvent(IbotnConstants.StateConstants.DETECT_DEVICE_SERVER_IP_CHANGE.name()));
    }
}


