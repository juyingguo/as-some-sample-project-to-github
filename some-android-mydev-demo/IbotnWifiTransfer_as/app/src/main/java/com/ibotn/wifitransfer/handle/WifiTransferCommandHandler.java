package com.ibotn.wifitransfer.handle;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ibotn.wifitransfer.R;
import com.ibotn.wifitransfer.application.IbotnApplication;
import com.ibotn.wifitransfer.eventbean.MessageEvent;
import com.ibotn.wifitransfer.utils.ByteUtil;
import com.ibotn.wifitransfer.utils.DevicePath;
import com.ibotn.wifitransfer.utils.FileUtils;
import com.ibotn.wifitransfer.utils.IOManagerUtils;
import com.ibotn.wifitransfer.utils.IbotnConstants;
import com.ibotn.wifitransfer.utils.IoUtils;
import com.ibotn.wifitransfer.utils.LogUtil;
import com.ibotn.wifitransfer.utils.NetUtils;
import com.ibotn.wifitransfer.utils.ThreadUtils;
import com.ibotn.wifitransfer.utils.ToastUtils;
import com.ibotn.wifitransfer.utils.TtsUtils;
import com.ibotn.wifitransfer.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WifiTransferCommandHandler {
    private static final String TAG = WifiTransferCommandHandler.class.getSimpleName();
    private WifiTransferCommandHandler(){
    }
    private static WifiTransferCommandHandler instance;
    /** Returns singleton class instance */
    public static WifiTransferCommandHandler getInstance() {
        if (instance == null) {
            synchronized (WifiTransferCommandHandler.class) {
                if (instance == null) {
                    instance = new WifiTransferCommandHandler();
                }
            }
        }
        return instance;
    }
    ////////////////////////////////////////////////////////////////////////////
    //
    //	For demo only - UDP Broadcasting
    //
    ////////////////////////////////////////////////////////////////////////////
    public boolean checkBuffer(Context mContext, byte[] bytes) {
        Log.i(TAG, "checkBuffer(.)>>bytes to string: " + new String(bytes,0,bytes.length));
        if (bytes[0] == '{' && bytes[1] == '"') {
            return checkHandler(mContext, bytes);

        }

        LogUtil.e(TAG, "demo_handler: Wrong data: " + bytes.toString().trim());

        return false;
    }
    //如果是进入演示环境返回true,false 普通命令处理
    public boolean checkHandler(Context mContext, byte[] packet) {
        // for demo only
        JSONObject object;

        try {
            object = new JSONObject(new String(packet, 0, packet.length));

            Log.i(TAG, "demo_handler ###############################");
            String cmd = object.optString("cmd");
            Log.i(TAG, " demo_handler CMD = " + cmd);
            if (TextUtils.equals(cmd, IbotnConstants.CommandConstant.MOBILE_CLIENT_SEND_IBOTN_DEVICE_RECEIVE_FILE.name())
                    || TextUtils.equals(cmd, IbotnConstants.CommandConstant.IBOTN_DEVICE_SEND_MOBILE_CLIENT_RECEIVE_FILE.name())) {
                doControlHandler(mContext, object);
                return true;
            }else
            {
                doControlHandler(mContext, object);
                return false;
            }

        } catch (JSONException e) {

			e.printStackTrace();
        }
        return false;
    }
    public void doControlHandler(Context mContext,JSONObject object){
        try {
            long recvTime = Calendar.getInstance().getTimeInMillis();
            String cmd = object.optString("cmd");
            Log.i(TAG, "cmd>>:" + cmd);
            if (IbotnConstants.CommandConstant.MOBILE_CLIENT_SEND_IBOTN_DEVICE_RECEIVE_FILE.name().equalsIgnoreCase(cmd)) {
                initReceiveFileSocket();
            } else if (IbotnConstants.CommandConstant.IBOTN_DEVICE_SEND_MOBILE_CLIENT_RECEIVE_FILE.name().equalsIgnoreCase(cmd)) {
                if (object.has("ip")){
                    String ip = object.optString("ip");
                    if (!TextUtils.isEmpty(ip)){
                        WifiTransferConnectionManager.getInstance().setAddressesForServer(ip);
                    }
                }
            } else {
                Log.e(TAG, "Unknow cmd: " + cmd);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void initReceiveFileSocket() {
        LogUtil.e(TAG,"initReceiveFileSocket()>>receiveFileServerThread:" + receiveFileServerThread);
        try {
            if (receiveFileServerThread != null){
                receiveFileServerThread.stopReceive();
                receiveFileServerThread = null;
            }
            receiveFileServerThread = new ReceiveFileServerThread();
        }catch (IOException e)
        {
            e.printStackTrace();
            LogUtil.e(TAG,"initReceiveFileSocket>> new ReceiveFileServerThread error msg = " + e.getMessage());
            exitReceiveFileSocket();
        }
        if (receiveFileServerThread != null){
            receiveFileServerThread.start();
        }
    }
    public static void exitReceiveFileSocket() {
        LogUtil.e(TAG,"exitReceiveFileSocket()");

        if(receiveFileServerThread != null){
            receiveFileServerThread.stopReceive();
            receiveFileServerThread.interrupt();
            receiveFileServerThread = null;
        }
    }
    private static ReceiveFileServerThread receiveFileServerThread;
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    /**
     * 接收文件服务端线程
     */
    private class ReceiveFileServerThread extends Thread{
        private boolean isRun = true;
        private ServerSocket serverSocket = null;
        private Socket socket = null;
        private final int SOCKET_PORT = 5566;
        private ReceiveFileServerThread() throws  IOException{
            init();
        }

        private void init() throws IOException {
            //////
//			serverSocket = new ServerSocket(SOCKET_PORT);
            ////////
//			serverSocket = new ServerSocket();
//			LogUtil.e(TAG,"init()>>serverSocket>>getReuseAddress:" + serverSocket.getReuseAddress());
//			serverSocket.setReuseAddress(true);
//			serverSocket.bind(new InetSocketAddress(SOCKET_PORT));
            ////////
            String ip = NetUtils.getIPAddress(IbotnApplication.getInstance());
            InetAddress inetAddress = InetAddress.getByName(ip);
            LogUtil.e(TAG,"init()>> new ServerSocket >>ip:" + ip + ",inetAddress:" + inetAddress);
            serverSocket = new ServerSocket(SOCKET_PORT,50, inetAddress);
            LogUtil.e(TAG,"init()>> new ServerSocket success");
        }

        private void stopReceive()
        {
            LogUtil.e(TAG,"stopReceive()>>serverSocket:" + serverSocket
                    + "\n socket:" + socket);
            this.isRun = false;
            try {
                if (serverSocket != null){
                    serverSocket.close();
                    serverSocket = null;
                }
                if (socket != null){
                    socket.close();
                    socket = null;
                }
            }catch (IOException e)
            {
                e.printStackTrace();
                LogUtil.e(TAG,"stopReceive IOException = "+e.getMessage());
            }
        }
        @Override
        public void run() {
            super.run();
            while (isRun) {
                LogUtil.e(TAG,"near field asr socket server>>waiting client to connect ...");
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                LogUtil.e(TAG,"near field asr socket server>>receive client to connect ...");
                String remoteHostAddress = socket.getInetAddress().getHostAddress();
                String localAddress = socket.getLocalAddress().getHostAddress();
                LogUtil.d(TAG, TAG +">>>>receiveConnection>>remoteHostAddress:"+remoteHostAddress + ",localAddress:" + localAddress);
//                singleThreadExecutor.submit(new RawByteReceiveFileRunnable2(socket));//use byte to read data
                singleThreadExecutor.submit(new RawByteReceiveFileEnhanceRunnable(socket));//use byte to read data

            }
        }
    }

    /**
     * RawByteReceiveFileRunnable Runnable
     * 与发送端对应文件名使用字节方式接受文件
     */
    private class RawByteReceiveFileRunnable2 implements Runnable {
        private Socket socket;
        private final String DEFAULT_RECEIVE_FILE_NAME = "record_wav.wav";
        /**
         * 按照文档定义。数据流前n(n=150)个字节为文件名（完整文件名，即包含后缀）,utf-8编码，一个汉字占用3个字节
         */
        private final int DEFINED_FILE_NAME_MIX_BYTE_LENGTH = 150;//固定150
        /**
         * 按照文档定义。数据流前15-18个字节为文件大小
         */
        private final int DEFINED_FILE_SIZE_FIX_BYTE_LENGTH = 8;

        public RawByteReceiveFileRunnable2(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            DataInputStream dataInputStream = null;
            try {
                LogUtil.d(TAG, TAG + ">>>>ReceiveFileRunnable>>>>Server: connection done>>socket:" + socket);
                inputStream =  socket.getInputStream();

                //如果客户端发送的文件名小于定义的文件名字节长度(不足长度，字节值补0)，将所有字节直接转化为UTF-8字符串，无法根据该字符串创建文件
                byte[] fileNameBuffer = new byte[DEFINED_FILE_NAME_MIX_BYTE_LENGTH];
                dataInputStream = new DataInputStream(inputStream);
                final String fileName = dataInputStream.readLine();
                long fileLength = Long.parseLong(dataInputStream.readLine());

                LogUtil.d(TAG, TAG + ">>>>>ReceiveFileRunnable>>>>filename:" + fileName + ",fileLength:" + fileLength);

                if (!TextUtils.isEmpty(fileName))
                {
                    // TODO: 2018/8/7
                    //机器端存储接收的文件：目录为优盘优先，外置ext-sd次之。内部sdcard禁止存放
                    String rootStorageDir = null;
                    String usbStoragePath = DevicePath.getInstance(IbotnApplication.getInstance()).getUsbStoragePath();
                    if (!TextUtils.isEmpty(usbStoragePath)){
                        rootStorageDir = usbStoragePath;
                    }else {
                        rootStorageDir = DevicePath.getInstance(IbotnApplication.getInstance()).getExtSdStoragePath();
                    }
                    if (TextUtils.isEmpty(rootStorageDir)){
                        Toast.makeText(IbotnApplication.getInstance(), "没有检查到优盘或外置卡", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FileUtils.isImage(fileName)){
                        rootStorageDir += IbotnConstants.StoragePathConstants.DIRECTORY_IMAGE;
                    }else if (FileUtils.isAudio(fileName)){
                        rootStorageDir += IbotnConstants.StoragePathConstants.DIRECTORY_AUDIO;
                    }else if (FileUtils.isVideo(fileName)){
                        rootStorageDir += IbotnConstants.StoragePathConstants.DIRECTORY_VIDEO;
                    }else if (FileUtils.isDocument(fileName)){
                        rootStorageDir += IbotnConstants.StoragePathConstants.DIRECTORY_DOCUMENT;
                    }else{
                        rootStorageDir += IbotnConstants.StoragePathConstants.DIRECTORY_OTHER;
                    }

                    LogUtil.d(TAG, TAG + ">>>>>ReceiveFileRunnable>>>>rootStorageDir:" + rootStorageDir);
                    boolean createOrExistsDir = FileUtils.createOrExistsDir(rootStorageDir);
                    LogUtil.d(TAG, TAG + ">>>>>ReceiveFileRunnable>>>>createOrExistsDir:" + createOrExistsDir);

                    final File file = new File(rootStorageDir,fileName);
                    FileUtils.createOrExistsFile(file);

                    outputStream = new FileOutputStream(file);
                    LogUtil.d(TAG, TAG + ">>>>>ReceiveFileRunnable>>>>inputStream.available():" + inputStream.available());
                    FileUtils.copyFile(dataInputStream, outputStream);
                    //start asr
                    LogUtil.d(TAG, TAG + ">>>>>ReceiveFileRunnable>>>>FileUtils.isFileExists(file):" + FileUtils.isFileExists(file));
                    // TODO: 2018/8/7
                }

            } catch (IOException e) {
                LogUtil.e(TAG, TAG +">>>>ReceiveFileRunnable>>>>IOException:" +  e.getMessage());
            }finally {
                IOManagerUtils.close(socket);
                IOManagerUtils.close(inputStream);
                IOManagerUtils.close(dataInputStream);
                IOManagerUtils.close(outputStream);
            }
        }
    }
    /**
     * RawByteReceiveFileRunnable Runnable
     * 与发送端对应文件名使用字节方式接受文件
     */
    private class RawByteReceiveFileEnhanceRunnable implements Runnable {
        private Socket socket;

        public RawByteReceiveFileEnhanceRunnable(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                LogUtil.d(TAG, TAG + ">>>>ReceiveFileRunnable>>>>Server: connection done>>socket:" + socket);
                inputStream =  socket.getInputStream();

                final String fileName = FileUtils.parseFileName(inputStream);
                final long fileLength = FileUtils.parseFileLength(inputStream);

                LogUtil.d(TAG, TAG + ">>>>>ReceiveFileRunnable>>>>filename:" + fileName + ",fileLength:" + fileLength);

                if (!TextUtils.isEmpty(fileName))
                {
                    // TODO: 2018/8/7
                    //机器端存储接收的文件：目录为优盘优先，外置sd-ext次之。内部sdcard禁止存放
                    String rootStorageDir = null;
                    String usbStoragePath = DevicePath.getInstance(IbotnApplication.getInstance()).getUsbStoragePath();
                    if (!TextUtils.isEmpty(usbStoragePath)){
                        rootStorageDir = usbStoragePath;
                    }else {
                        rootStorageDir = DevicePath.getInstance(IbotnApplication.getInstance()).getExtSdStoragePath();
                    }
                    if (TextUtils.isEmpty(rootStorageDir)){
                        ThreadUtils.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                String str = IbotnApplication.getInstance().getString(R.string.str_un_exist_uhost_sdext);
                                ToastUtils.showCustomStaticToast(IbotnApplication.getInstance(),str);
                                TtsUtils.startTtsSpeaker(IbotnApplication.getInstance(),str,TtsUtils.TTS_WEIGHT_5);
                            }
                        });
                        return;
                    }
                    if (FileUtils.isImage(fileName)){
                        rootStorageDir += IbotnConstants.StoragePathConstants.DIRECTORY_IMAGE;
                    }else if (FileUtils.isAudio(fileName)){
                        rootStorageDir += IbotnConstants.StoragePathConstants.DIRECTORY_AUDIO;
                    }else if (FileUtils.isVideo(fileName)){
                        rootStorageDir += IbotnConstants.StoragePathConstants.DIRECTORY_VIDEO;
                    }else if (FileUtils.isDocument(fileName)){
                        rootStorageDir += IbotnConstants.StoragePathConstants.DIRECTORY_DOCUMENT;
                    }else{
                        rootStorageDir += IbotnConstants.StoragePathConstants.DIRECTORY_OTHER;
                    }

                    LogUtil.d(TAG, TAG + ">>>>>ReceiveFileRunnable>>>>rootStorageDir:" + rootStorageDir);
                    boolean createOrExistsDir = FileUtils.createOrExistsDir(rootStorageDir);
                    LogUtil.d(TAG, TAG + ">>>>>ReceiveFileRunnable>>>>createOrExistsDir:" + createOrExistsDir);

                    final File file = new File(rootStorageDir,fileName);
                    FileUtils.createOrExistsFile(file);

                    outputStream = new FileOutputStream(file);
                    LogUtil.d(TAG, TAG + ">>>>>ReceiveFileRunnable>>>>inputStream.available():" + inputStream.available());
                    IoUtils.copyStream(inputStream, outputStream, new IoUtils.CopyListener() {
                        @Override
                        public boolean onBytesCopied(final int current, final int total) {
//                            LogUtil.d(TAG, TAG + ">>>>onBytesCopied>>receive file >>fileName:" + fileName + ",current:" + current + ",total:" + total);
                            ThreadUtils.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    String strProgress = IbotnApplication.getInstance().getString(R.string.str_receiving) + " : " + fileName + "\n" + (Utils.readableFileSize(current) + "/" + Utils.readableFileSize(fileLength));
                                    ToastUtils.showCustomStaticToast(IbotnApplication.getInstance(),strProgress);
                                }
                            });
                            return true;
                        }
                    });
                    //start asr
                    LogUtil.d(TAG, TAG + ">>>>>ReceiveFileRunnable>>>>FileUtils.isFileExists(file):" + FileUtils.isFileExists(file));
                    // TODO: 2018/8/7
//                    EventBus.getDefault().post(new MessageEvent(IbotnConstants.StateConstants.RECEIVE_FILE_SUCCESS.name(),file.getAbsolutePath()));
                    ThreadUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showCustomStaticToast(IbotnApplication.getInstance(), FileUtils.getFileName(file) + " " + IbotnApplication.getInstance().getString(R.string.str_receive_success));
                        }
                    });
                }

            } catch (IOException e) {
                LogUtil.e(TAG, TAG +">>>>ReceiveFileRunnable>>>>IOException:" +  e.getMessage());
                ThreadUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showCustomStaticToast(IbotnApplication.getInstance(), IbotnApplication.getInstance().getString(R.string.str_receive_fail));
                    }
                });
            }finally {
                IOManagerUtils.close(socket);
                IOManagerUtils.close(inputStream);
                IOManagerUtils.close(outputStream);
            }
        }
    }

}
