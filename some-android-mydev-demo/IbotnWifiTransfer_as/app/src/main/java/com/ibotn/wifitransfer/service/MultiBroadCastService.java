package com.ibotn.wifitransfer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.ibotn.wifitransfer.handle.WifiTransferCommandHandler;
import com.ibotn.wifitransfer.receiver.CoreReceiver;
import com.ibotn.wifitransfer.utils.LogUtil;
import com.ibotn.wifitransfer.utils.NetUtils;
import com.ibotn.wifitransfer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MultiBroadCastService extends Service {
    private final String TAG = MultiBroadCastService.class.getSimpleName();
    private Context context;
    public MultiBroadCastService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        LogUtil.d(TAG,"init()>>");
        CoreReceiver.addListener(listener); // TODO: 2018/8/8 需反注册

        initReceiveBroadCast();

    }
    //网络切换时回调，重新初始化跳舞socket
    private CoreReceiver.WifiChangeListener listener = new CoreReceiver.WifiChangeListener() {
        @Override
        public void onWifiChanged() {
            LogUtil.e(TAG," onWifiChanged");
            initReceiveBroadCast();
        }

        @Override
        public void onDisconnectWifi(Context context) {
            Log.e(TAG, " onDisconnectWifi");
            Toast.makeText(context,"网络已断开，请连接网络！",Toast.LENGTH_LONG).show();
        }
    };

    private void initReceiveBroadCast() {
        LogUtil.e(TAG," initReceiveBroadCast receiveMultiBroadCastThread");
        //multicast type
        if(receiveMultiBroadCastThread != null)
        {
            receiveMultiBroadCastThread.stopThread();
            receiveMultiBroadCastThread = null;
        }

        receiveMultiBroadCastThread = new ReceiveMultiBroadCastThread();
        receiveMultiBroadCastThread.start();
    }
    private static final String	DEFAULT_MULTICAST_IP = "224.0.0.0";/*"228.5.6.7"*/;
    private static final int		DEFAULT_MULTICAST_PORT = 43708;
    private final int MAC_RECV_BUF_SIZE = 1024;
    private byte[] recvBuf = new byte[MAC_RECV_BUF_SIZE];
    private byte[] multicastRecvBuf = new byte[MAC_RECV_BUF_SIZE];
    private ReceiveMultiBroadCastThread receiveMultiBroadCastThread;
    class ReceiveMultiBroadCastThread extends Thread
    {
        private boolean flagThread = true;
        private MulticastSocket multicastSocket;
        DatagramPacket packet = new DatagramPacket(multicastRecvBuf, multicastRecvBuf.length);
        InetAddress group_ip = null;
        WifiManager.MulticastLock mcLock = null;
        WifiManager wim = null;

        public void stopThread()
        {
            flagThread = false;
            if (mcLock != null) {
                mcLock.release();
                mcLock = null;
            }

            if (multicastSocket != null) {
                if (group_ip != null) {
                    try {
                        multicastSocket.leaveGroup(group_ip);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                multicastSocket.close();
                multicastSocket = null;
            }

            wim = null;
            packet = null;
        }

        public ReceiveMultiBroadCastThread()
        {
            init();
        }

        private void init() {
            wim = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            if (wim == null) {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SystemClock.sleep(1000);
                init();
            }

            mcLock = wim.createMulticastLock("ibotn_wifi_transfer");
            mcLock.acquire();

            try {
                multicastSocket = new MulticastSocket(DEFAULT_MULTICAST_PORT);
                LogUtil.e(TAG," new multicastSocket success");
                group_ip = InetAddress.getByName(/*"224.0.0.0"*/DEFAULT_MULTICAST_IP);// TODO: 2018/5/4  MulticastSocket 说明：The address 224.0.0.0 is reserved and should not be used.
                multicastSocket.setLoopbackMode(true);
                multicastSocket.setTimeToLive(255);
                multicastSocket.joinGroup(group_ip);
            } catch (IOException e) {

                e.printStackTrace();
                LogUtil.e(TAG," new MulticastSocket error msg = "+e.getMessage());
//				init();
                stopThread();
            }
        }

        @Override
        public void run() {
            LogUtil.e(TAG, " >>>>>> recvBuffer for ReceiveMultiBroadCastThread flagThread = "+flagThread);
            while (flagThread) {
                if (!NetUtils.isWifiConnected(context)) {
                    try {
                        LogUtil.e(TAG," isNetworkConnected fase");
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    stopThread();
                    break;
                }

                try {
                    //Debug(TAG, "Receive Port:"+ socket.getLocalPort() + " UDP socket waiting...");
                    LogUtil.i(TAG," Multicast receive waiting ");
                    multicastSocket.receive(packet);
                    LogUtil.i(TAG," Multicast receive end ");

                    if (packet.getLength() <= 0){
                        Log.e(TAG , " Multicast Socket: receive SKIP Length " + packet.getLength());
                        continue;
                    }
                } catch (IOException e) {
                    Log.e(TAG, " Multicast Socket: Receive Error " + e.getMessage());
                    e.printStackTrace();
                    continue;
                }

                LogUtil.e(TAG," Multicast handle message");
                boolean result = WifiTransferCommandHandler.getInstance().checkBuffer(context, packet.getData());
                LogUtil.e(TAG," Multicast wifitransfer = "+result + " multicastSocket = "+multicastSocket);
                if( result && multicastSocket != null) {
                    try {
                        JSONObject object = new JSONObject();
                        String ip = NetUtils.getIPAddress(context);
                        object.put("ibotn_ip", ip);
                        object.put("des", "Server ready");
                        // 2018/5/8 添加13位序列号，其与通话中心账户关联
                        object.put("device_id", Utils.getSerial());
                        LogUtil.e(TAG, " ip = " + ip + " Server ready");
                        byte[] data = object.toString().getBytes("UTF-8");
                        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, group_ip, DEFAULT_MULTICAST_PORT);
                        for (int i = 0; i < 10; i++) { // TODO: 2018/6/7
                            multicastSocket.send(datagramPacket);
                            LogUtil.e(TAG, " send datagramPacket " + datagramPacket.toString());
                            sleep(200);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

            Log.d(TAG, " Multicast: recvThread is end...");
        }

    }
}
