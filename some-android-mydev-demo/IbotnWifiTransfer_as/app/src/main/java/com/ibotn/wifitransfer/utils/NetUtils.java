package com.ibotn.wifitransfer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by jy on 2017/2/7 ;8:56.<br/>
 *
 * @description:
 */
public class NetUtils {
    private static final String TAG = "NetUtils";
    /**
     * 检测网络是否连接。该方法并不能检查假连接
     * @see #checkUnusefulNetworkByPing(Context)
     * @see #checkUnusefulNetworkByInetAddress()
     * @return 没有打开网络false;
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isConnected();
                //return mNetworkInfo.isConnectedOrConnecting();
                //return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /* @author suncat
* @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
* @return
*/
    public static final boolean ping() {

        String result = null;
        try {
            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.d("----result---", "result = " + result);
        }
        return false;
    }

    /**
     *
     * @param context
     * @return 获取当前连接的wifi 名
     */
    public static final String getWifiName(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return "";

        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo == null)
            return "";
        else
            return networkInfo.getExtraInfo();
    }

    /**
     * 检测网络是否连接
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用，已经链接成功了
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用，已经链接成功了
     */
    public static boolean isWifiDisconnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.getState() == NetworkInfo.State.DISCONNECTED;
            }
        }
        return true;
    }


    /**
     * 判断MOBILE网络是否可用
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
    /**
     * 获取本地内网IP
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                Enumeration<InetAddress> enIp = ni.getInetAddresses();
                while (enIp.hasMoreElements()) {
                    InetAddress inet = enIp.nextElement();
                    if (!inet.isLoopbackAddress() && (inet instanceof Inet4Address)) {
                        return inet.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return "0";//return error ip
    }
    /**
     * @description 之前能够ping 通的地址，现在部分ping不通了。服务器做了限制，且服务器随时可能做限制，以防止数据攻击（ping of death），废弃该方式。20180712
     * <br/>
     * 该方法用于检查【假连接】，请在网络连接后调用。 <br/>
     * 1.use java.lang.Runtime.getRuntime().exec("ping ... ") to check the unreal net connected <br/>
     * 2.final String SERVER_ADDRESS_1 =  "www.ibotn.com";  <br/>
     * 3.final String SERVER_ADDRESS_2 =  "www.baidu.com";  <br/>
     * 4.为了保险起见，先ping SERVER_ADDRESS_1;如果为false,就继续ping SERVER_ADDRESS_2; <br/>
     * 5.要在工作线程调用该方法,<br/>
     * @param context
     * @return  假连接都返回false;
     * @see #checkNetworkByAccess()
     */
    public synchronized  static boolean checkUnusefulNetworkByPing(Context context) {
        boolean result = false;
        if (context == null){
            return result;
        }
        final String SERVER_ADDRESS_1 =  "www.iflytek.com";
        final String SERVER_ADDRESS_2 =  "www.ibotn.com";
        final String SERVER_ADDRESS_3 =  "www.baidu.com";
        final String[] SERVER_ADDRESSES = {SERVER_ADDRESS_1,SERVER_ADDRESS_2,SERVER_ADDRESS_3};
        long beginTime  = System.currentTimeMillis();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //假连接时：netInfo:[type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: "HONOR H30-L01", roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false]
            LogUtil.d(TAG, TAG + ">>>>checkUnusefulNetworkByPing>>>netInfo:" + netInfo);
            if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
                if (netInfo == null) {
                    LogUtil.d(TAG, TAG + ">>>>checkUnusefulNetworkByPing>>>Network is off");
                }
                else if (!netInfo.isConnected()){
                    LogUtil.d(TAG, TAG + ">>>>checkUnusefulNetworkByPing>>>Network is not connected");
                }
                else{
                    LogUtil.d(TAG, TAG + ">>>>checkUnusefulNetworkByPing>>>Network is not available");
                }
                result =  false;
            } else {
                try {
                    //-w是指执行的最后期限，也就是执行的时间，单位为秒
                    //-c是指ping的次数
                    //us.ntp.org.cn 【server address】未使用
                    for (String serverAddress : SERVER_ADDRESSES){
                        LogUtil.e(TAG, TAG + ">>>>serverAddress:" + serverAddress);
                        Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 -W 5 " +serverAddress);//在Linux 下执行-c 2 -W 5 us.ntp.org.cn

                        //p1.waitFor()是阻塞的。
                        if (p1.waitFor() != 0) {//0 表示正常终止
                            LogUtil.d(TAG, TAG + ">>>>checkUnusefulNetworkByPing>>>Network still not working");
                        }else {
                            LogUtil.d(TAG, TAG + ">>>>checkUnusefulNetworkByPing>>>Network working");
                            result = true;
                        }
                        if (result){
                            break;
                        }
                    }
                } catch (Exception e) {
                    LogUtil.d(TAG, TAG + ">>>>checkUnusefulNetworkByPing>>>:" + e.getMessage());
                }
            }
        }

        LogUtil.e(TAG, TAG + ">>>>checkUnusefulNetworkByPing>>>consume time:" + (System.currentTimeMillis() - beginTime) + ",result:" + result);

        return result;
    }
    /**
     * 通过web,http/https 方式访问地址，有一个可以访问，就认为网络可用。如所配的地址，都改变，需要重新设置新的地址。<br/>
     * @return true success
     */
    public static boolean checkNetworkByAccess() {
        boolean result = false;
        long beginTime  = System.currentTimeMillis();
        //20180712 网站地址
        final String[] SERVER_ADDRESSES = {
                "http://www.ibotn.com",
                "https://www.xfyun.cn/",
                "https://www.baidu.com",
                "https://mail.sina.com.cn/",
                "http://www.tsinghua.edu.cn",
                "http://www.pku.edu.cn/",
                "http://tv.cctv.com/",
                "http://www.qq.com/"

        };
        for (String serverAddress : SERVER_ADDRESSES){
            try {
                result = isAddressCanConnected(serverAddress);
            } catch (Exception e) {
//                LogUtil.e(TAG, TAG + ">>>>checkNetworkByAccess>>>IOException:" + e.getMessage());
                result = false;
            }
            if (result){
                break;
            }
        }
        LogUtil.d(TAG, TAG + ">>>>checkNetworkByAccess>>>consume time:" + (System.currentTimeMillis() - beginTime) + ",result:" + result);

        return result;
    }

    private static boolean isAddressCanConnected(String urlStr) {
//        LogUtil.e(TAG, TAG + ">>>>isAddressCanConnected>>>urlStr:" + urlStr);
        boolean result = false;
        long beginTime  = System.currentTimeMillis();
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10* 1000);
            conn.setConnectTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
//            LogUtil.e(TAG, TAG + ">>>>isAddressCanConnected>>>responseCodee:" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                result = true;
            } else {
                result = false;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        finally {
            try {
                if (is != null){
                    is.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            if (conn != null){
                conn.disconnect();
            }
        }
//        LogUtil.e(TAG, TAG + ">>>>>isAddressCanConnected>>>consume time:" + (System.currentTimeMillis() - beginTime) + ",result:" + result);
        return result;
    }
    /**
     * @deprecated
     * 20180710以后，该方式，无法正确检测网络；该方式废弃
     * <br/>
     * 该方法用于检查【假连接】，请在网络连接后调用。 <br/>
     * 1.use java.lang.Runtime.getRuntime().exec("ping ... ") to check the unreal net connected <br/>
     * 2.final String SERVER_ADDRESS_1 =  "www.ibotn.com";  <br/>
     * 3.final String SERVER_ADDRESS_2 =  "www.baidu.com";  <br/>
     * 4.为了保险起见，先ping SERVER_ADDRESS_1;如果为false,就继续ping SERVER_ADDRESS_2; <br/>
     * 5.最好在工作线程调用该方法,<br/>
     * @return  假连接都返回false;
     */
    public synchronized  static boolean checkUnusefulNetworkByInetAddress() {
        //LogUtil.e(TAG, "Network is off");
//        SuUtil.executeCmd("sudo setcap cap_net_raw=ep /usr/lib/jvm/jdk/bin/java"); //  2018/7/10 设置权限，但仍然无效
        boolean result = false;
        long beginTime  = System.currentTimeMillis();
        final String SERVER_ADDRESS_1 = "www.ibotn.com";
        final String SERVER_ADDRESS_2 = "www.baidu.com";
        final String[] SERVER_ADDRESSES = {SERVER_ADDRESS_1, SERVER_ADDRESS_2};
        for (String serverAddress : SERVER_ADDRESSES){
            LogUtil.e(TAG, TAG + ">>>>serverAddress:" + serverAddress);
            try {
                result = InetAddress.getByName(serverAddress).isReachable(5*1000);
            } catch (IOException e) {
                LogUtil.e(TAG, TAG + ">>>>checkUnusefulNetworkByInetAddress>>>IOException:" + e.getMessage());
                result = false;
            }
            if (result){
                break;
            }
        }
        LogUtil.e(TAG, TAG + ">>>>checkUnusefulNetworkByInetAddress>>>consume time:" + (System.currentTimeMillis() - beginTime) + ",result:" + result);

        return result;
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
