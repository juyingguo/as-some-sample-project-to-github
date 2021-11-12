package com.ibotn.wifitransfer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.ibotn.wifitransfer.service.MultiBroadCastService;
import com.ibotn.wifitransfer.utils.LogUtil;
import com.ibotn.wifitransfer.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * create by jy on 2018/8/8 ,14:25
 * des:
 */
public class CoreReceiver extends BroadcastReceiver {
    private static String wifiName = "";
    private static List<WifiChangeListener> wifiChangeListeners = new ArrayList<>();
    private final String TAG = CoreReceiver.class.getSimpleName();
    private Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String action = intent.getAction();
        LogUtil.d(TAG,TAG + ">>onReceive()>>action:" + action);
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            mContext.startService(new Intent(mContext, MultiBroadCastService.class));
        } else if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            LogUtil.e(TAG, " CONNECTIVITY_ACTION " + mWiFiNetworkInfo);
            if (NetUtils.isWifiConnected(context)) {
                Log.e(TAG, " 111 wifi name " + wifiName);
                String tempWifiName = NetUtils.getWifiName(context);
                if (!TextUtils.isEmpty(tempWifiName))
                    tempWifiName = tempWifiName.replace("\"", "");

                if (!TextUtils.equals(wifiName, tempWifiName)) {
                    if (!wifiChangeListeners.isEmpty()) {
                        for (WifiChangeListener listener : wifiChangeListeners) {
                            listener.onWifiChanged();
                        }
                    }
                }
                wifiName = tempWifiName;
                Log.e(TAG, " 222 wifi name " + wifiName);

            } else if (NetUtils.isWifiDisconnected(context)) {
                Log.e(TAG, " 333 disconnect wifi ");
                wifiName = "";
                for (WifiChangeListener listeners : wifiChangeListeners) {
                    listeners.onDisconnectWifi(context);
                }
            }
        }
    }

    public static void addListener(WifiChangeListener listener) {
        if (listener != null)
            wifiChangeListeners.add(listener);
    }
    public interface WifiChangeListener {
        void onWifiChanged();

        void onDisconnectWifi(Context context);
    }
}
