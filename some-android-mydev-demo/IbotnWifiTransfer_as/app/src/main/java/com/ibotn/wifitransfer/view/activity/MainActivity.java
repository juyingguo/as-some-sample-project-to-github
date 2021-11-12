package com.ibotn.wifitransfer.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibotn.wifitransfer.R;
import com.ibotn.wifitransfer.eventbean.MessageEvent;
import com.ibotn.wifitransfer.handle.WifiTransferConnectionManager;
import com.ibotn.wifitransfer.service.MultiBroadCastService;
import com.ibotn.wifitransfer.utils.ByteUtil;
import com.ibotn.wifitransfer.utils.DevicePath;
import com.ibotn.wifitransfer.utils.FileUtils;
import com.ibotn.wifitransfer.utils.IOManagerUtils;
import com.ibotn.wifitransfer.utils.IbotnConstants;
import com.ibotn.wifitransfer.utils.IoUtils;
import com.ibotn.wifitransfer.utils.LogUtil;
import com.ibotn.wifitransfer.utils.NetUtils;
import com.ibotn.wifitransfer.utils.ToastUtils;
import com.ibotn.wifitransfer.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends FullScreenActivity implements View.OnClickListener{

    private String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    private Button btn_connect;
    private ImageView iv_back;
    private TextView tv_send_file;
    private TextView tv_select_file;
    private TextView tv_display_file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
        mContext = this;

        initViews();
        initRes();
        registerListener();

        mContext.startService(new Intent(mContext, MultiBroadCastService.class)); // TODO: 2018/8/7

        List<String> storagePaths = DevicePath.getStoragePaths(mContext);
        LogUtil.d(TAG,"onCreate>>storagePaths:" + storagePaths);
        LogUtil.d(TAG,"onCreate>> Environment.getExternalStorageDirectory():" + Environment.getExternalStorageDirectory().getAbsolutePath());

        LogUtil.d(TAG,"onCreate>>NetUtils.getLocalIpAddress():" + NetUtils.getLocalIpAddress());
        LogUtil.d(TAG,"onCreate>>NetUtils.getIPAddress(mContext):" + NetUtils.getIPAddress(mContext));
        LogUtil.d(TAG,"onCreate>>NetUtils.getExternalFilesDir():" + mContext.getExternalFilesDir("wifitransfer").getAbsolutePath());
        LogUtil.d(TAG,"onCreate>>NetUtils.getCacheDir():" + mContext.getCacheDir().getAbsolutePath());


    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        btn_connect = (Button) findViewById(R.id.btn_connect);
        tv_send_file = (TextView) findViewById(R.id.tv_send_file);
        tv_select_file = (TextView) findViewById(R.id.tv_select_file);
        tv_display_file = (TextView) findViewById(R.id.tv_display_file);
    }
    private void registerListener() {
        iv_back.setOnClickListener(this);
        btn_connect.setOnClickListener(this);
        tv_send_file.setOnClickListener(this);
        tv_select_file.setOnClickListener(this);
    }
    /////////////
    /**
     * 多个类型文件路径结合
     */
    private ArrayList<String> multiFileTypePaths = new ArrayList<>();
    private String firstIpAddress = null;
    private void initRes() {

        checkAndGetIp();
        ArrayList<String> stringArrayListExtra = getIntent().getStringArrayListExtra(IbotnConstants.ExtraConstants.EXTRA_RESULT_LIST);
        if (stringArrayListExtra != null){
            multiFileTypePaths.addAll(stringArrayListExtra);
        }
        displayFile();
    }

    private void checkAndGetIp() {
        firstIpAddress = WifiTransferConnectionManager.getInstance().getFirstIpAddress();
        LogUtil.d(TAG, TAG + ">>>>>checkAndGetIp()>>firstIpAddress:" + firstIpAddress);
        if (TextUtils.isEmpty(firstIpAddress)){
            updateView(false);
            setCurrState(IbotnConstants.StateConstants.UN_CONNECT);
        }else {
            updateView(true);
            setCurrState(IbotnConstants.StateConstants.CONNECTED);
        }
    }

    @Override
    public void onClick(View v) {
        if (iv_back == v){
            finish();
        }else if (tv_select_file == v){
            selectFile();
        }else if (tv_send_file == v){
            sendFile();
        }
    }

    private Handler handlerUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == IbotnConstants.StateConstants.CONNECT_ERROR.ordinal()) {
                updateView(false);
            } else if (what == IbotnConstants.StateConstants.CONNECTED.ordinal()) {
                setCurrState(IbotnConstants.StateConstants.CONNECTED);
                updateView(true);
            } else if (what == IbotnConstants.StateConstants.SEND_FILE_ERROR.ordinal()) {
                Toast.makeText(mContext, "send_file_error", Toast.LENGTH_SHORT).show();

                setCurrState(IbotnConstants.StateConstants.UN_CONNECT);
                updateView(false);

                WifiTransferConnectionManager.getInstance().clearAddressesForServer();

            } else if (what == IbotnConstants.StateConstants.SEND_FILE_SUCCESS.ordinal()) {
                Toast.makeText(mContext, "send_file_success", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN/*,sticky = true*/)
    public void onMessageEvent(MessageEvent event){
        if (event != null){
            if (TextUtils.equals(event.getMessage(),IbotnConstants.StateConstants.DETECT_DEVICE_SERVER_IP_CHANGE.name())){
                checkAndGetIp();
            }else if (TextUtils.equals(event.getMessage(),IbotnConstants.StateConstants.RECEIVE_FILE_SUCCESS.name())){
                ToastUtils.showCustomToast(mContext,event.getFilePath());
            }
        }
    }
    //////////////////////////
    private IbotnConstants.StateConstants currState;
    public void setCurrState(IbotnConstants.StateConstants currState) {
        this.currState = currState;
    }
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private void sendFile() {
        if (currState != IbotnConstants.StateConstants.CONNECTED) {
            Toast.makeText(mContext, R.string.str_un_connect, Toast.LENGTH_SHORT).show();
            return ;
        }
        if (multiFileTypePaths != null ){
            for (final String path : multiFileTypePaths){
                if (currState != IbotnConstants.StateConstants.CONNECTED){
                    break;
                }
                singleThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        sendFileWithRawByteEnhance(new File(path));
                    }
                });
            }
        }

    }

    ////////////
    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, IbotnConstants.RequestCode.REQUEST_CODE_SELECT_FILE.ordinal());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == IbotnConstants.RequestCode.REQUEST_CODE_SELECT_FILE.ordinal()){
                dealSelectFile(data);
            }
        }
    }
    /////////////////////////////////////////
    private final int MAX_COUNT_SEND_FILE  = 10;
    String filePath;
    private void dealSelectFile(final Intent data) {
        if (data == null) return;
        filePath = null;
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) {

                try {
                    Uri uri = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4以后
                        filePath = FileUtils.getPath(mContext, uri);
                    } else {//4.4以下下系统调用方法
                        filePath = FileUtils.getRealPathFromURI(mContext,uri);
                    }
                    LogUtil.d(TAG, "dealSelectFile>>filePath:" + filePath);

                }catch (Exception e){
                    LogUtil.d(TAG, "dealSelectFile>>e:" + e.getMessage());
                }

                emitter.onNext("ok");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        if (!TextUtils.isEmpty(filePath) && FileUtils.isFile(filePath) && FileUtils.isFileExists(filePath)){
                            if (multiFileTypePaths.size() > MAX_COUNT_SEND_FILE){
                                Toast.makeText(mContext, "一次最多发送10个文件", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            multiFileTypePaths.add(filePath);
                            displayFile();
                        }
                    }
                });
    }
    private void displayFile() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String path : multiFileTypePaths){
            stringBuilder.append(path).append("\n");
        }
        tv_display_file.setText(stringBuilder.toString());

    }

    //////////////////////
    private Socket socket;
    private final int SOCKET_PORT = 5566;
    private static final int SOCKET_TIMEOUT = 10*1000;
    /**
     *
     * @throws IOException IOException
     */
    private void initSocket() throws IOException {
        String firstIpAddress = WifiTransferConnectionManager.getInstance().getFirstIpAddress();
        LogUtil.d(TAG, TAG + ">>>>>initSocket()>>firstIpAddress:" + firstIpAddress);
        if (TextUtils.isEmpty(firstIpAddress)) {
            return;
        }
        socket = new Socket();
        socket.bind(null);
        socket.connect((new InetSocketAddress(firstIpAddress, SOCKET_PORT)), SOCKET_TIMEOUT);

    }

    ///////////////////////////////////////////
    /**
     * send file to server ,use socket.
     * 使用定义的字节方式发送文件，采用换行符字节的方式【文件名 + 换行符，文件长度 + 换行符，文件整个内容】
     */
    public void sendFileWithRawByteEnhance(File file) {
        LogUtil.d(TAG, TAG + ">>>>>sendFileWithRawByteEnhance()>>");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            LogUtil.d(TAG, TAG + ">>>>>Opening client socket - >>>");
            initSocket();
            LogUtil.d(TAG, TAG + ">>>>>Open client socket success>>getInetAddress:" + socket.getInetAddress());
            LogUtil.d(TAG, TAG + ">>>>>Open client socket success>>getLocalAddress:" + socket.getLocalAddress());
            if (socket == null) {
                handlerUI.sendEmptyMessage(IbotnConstants.StateConstants.SEND_FILE_ERROR.ordinal());
                return;
            }

            LogUtil.d(TAG, TAG + ">>>>>Client socket send file start>>>");
            outputStream = socket.getOutputStream();
            if (file != null && file.exists()) {
                LogUtil.d(TAG, ">>>>>filename:" + file.getName() + ",length:" + file.length());
                final String fileName = file.getName();
                byte[] fileNameToBytes = fileName.getBytes(Charset.forName("UTF-8"));
                LogUtil.d(TAG, ">>>>>fileNameToBytes.length:" + fileNameToBytes.length);
                //write file length ,use byte array
                outputStream.write(fileNameToBytes);
                outputStream.write("\n".getBytes());

                long actualFileSize = file.length();
                byte[] definedFileSizeByteArray = ByteUtil.getBytes(actualFileSize);//需要按照定义，数组长度为8
                outputStream.write(definedFileSizeByteArray);
                outputStream.write("\n".getBytes());

                inputStream = new FileInputStream(file);
                IoUtils.copyStream(inputStream, outputStream, new IoUtils.CopyListener() {
                    @Override
                    public boolean onBytesCopied(final int current, final int total) {
//                            MyLog.d(TAG, TAG + ">>>>onBytesCopied>>fileName:" + fileName + ",current:" + current + ",total:" + total);
                        handlerUI.post(new Runnable() {
                            @Override
                            public void run() {
                                String strProgress = mContext.getString(R.string.str_sending) + " : " + fileName + "\n" + (Utils.readableFileSize(current) + "/" + Utils.readableFileSize(total));
                                ToastUtils.showCustomStaticToast(mContext,strProgress);
                            }
                        });
                        return true;
                    }
                });
                LogUtil.d(TAG, TAG + ">>>>>Client socket send file finish!");

                handlerUI.sendEmptyMessage(IbotnConstants.StateConstants.SEND_FILE_SUCCESS.ordinal());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, TAG + ">>>>>Exception:" + e.getMessage());
            handlerUI.sendEmptyMessage(IbotnConstants.StateConstants.SEND_FILE_ERROR.ordinal());
        } finally {
            IOManagerUtils.closeSilently(inputStream);
            IOManagerUtils.closeSilently(outputStream);
            IOManagerUtils.close(socket);
            inputStream = null;
            outputStream = null;
            socket = null;
        }
    }

    /**
     *
     * @param connected true connected
     */
    private void updateView(boolean connected) {
        if (connected) {
            btn_connect.setText(R.string.str_connected);
            btn_connect.setEnabled(false);
        } else {
            btn_connect.setText(R.string.str_wait_receiver_to_connect);
            btn_connect.setEnabled(true);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        handlerUI.removeCallbacksAndMessages(null);
        singleThreadExecutor.shutdown();
        List<Runnable> runnables = singleThreadExecutor.shutdownNow();
        for (Runnable runnable : runnables){
            LogUtil.d(TAG, TAG + ">>>>>onDestroy>>runnable:" + runnable);
        }
    }
}
