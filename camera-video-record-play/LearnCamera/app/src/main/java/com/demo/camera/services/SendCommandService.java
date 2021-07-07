package com.demo.camera.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.demo.camera.CameraPreviewService;
import com.demo.camera.MainActivity;
import com.demo.camera.common.ConstControl;
import com.demo.camera.receiver.OperationReceiver;
import com.demo.camera.utils.LogUtils;
//import com.demo.ibotnlauncher.expression.ExpressionAnimationService;

public class SendCommandService extends Service {

    private final String TAG = SendCommandService.class.getSimpleName();
    private Context context;
    /**
     * 拍照类型;0拍照，1抓拍，2连拍；
     */
    private int captureType;
    private boolean withAudio = false;

    public SendCommandService() {
        // TODO Auto-generated constructor stub
        context = this;
    }

    /*
     * command : take_picture -- take a picture.
     * command : start_recording_with_audio -- start video recording with audio.
     * command : start_recording -- start video recording without audio.
     * command : stop_recording -- stop video recording.
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String command = intent.getStringExtra(OperationReceiver.EXTRA_FUNCTION_TYPE);
            LogUtils.d(TAG, TAG + ">>>onStartCommand>>>> command: " + command);
            captureType = intent.getIntExtra(OperationReceiver.EXTRA_CAPTURE_TYPE, 0);
            withAudio = intent.getBooleanExtra(OperationReceiver.EXTRA_WITH_AUDIO,false);
            final boolean withAudio = intent.getBooleanExtra(OperationReceiver.EXTRA_WITH_AUDIO, false);
            if (command != null) {
                try {
                    if (OperationReceiver.FUNCTION_TYPE_TAKE_PICTURE.equals(command)) {

                        Intent previewIntent = new Intent(getApplicationContext(), MainActivity.class);
                        previewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        previewIntent.putExtra(OperationReceiver.EXTRA_CAPTURE_TYPE, captureType);
                        startActivity(previewIntent);
                        stopSelf();
                        LogUtils.d(TAG, TAG + ">>onStartCommand()>>>Preview Take Picture");


                    } else if (OperationReceiver.FUNCTION_TYPE_START_VIDEO_RECORDING.equals(command)) {
                        ConstControl.setExpressionAnimationState(context, ConstControl.EXPRESSION_START_VR);
                        Intent previewIntent = new Intent(getApplicationContext(), MainActivity.class);
                        previewIntent.putExtra(OperationReceiver.EXTRA_FUNCTION_TYPE,OperationReceiver.FUNCTION_TYPE_START_VIDEO_RECORDING);
                        previewIntent.putExtra(OperationReceiver.EXTRA_WITH_AUDIO,withAudio);
                        previewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(previewIntent);
                        stopSelf();
                        LogUtils.d(TAG, TAG + ">>onStartCommand()>>>Preview Recording");//开启录像

                    }
                    /*
                    else if (OperationReceiver.FUNCTION_TYPE_STOP_PREVIEW.equals(command)) {
                        ServiceConnection connection = new ServiceConnection() {
                            @Override
                            public void onServiceConnected(ComponentName className, IBinder b) {
                                CameraPreviewService.BaseServiceBinder binder = (CameraPreviewService.BaseServiceBinder) b;
                                binder.stopPreview(CameraPreviewService.SUPER_CALLER);
                                unbindService(this);
                                stopSelf();
                            }

                            @Override
                            public void onServiceDisconnected(ComponentName className) {
                                Log.d(TAG, "CameraPreviewServcie onServiceDisconnected");
                            }
                        };
                        bindPreviewService(connection);
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return START_NOT_STICKY;
    }

    private void bindPreviewService(ServiceConnection connection) {
        Log.d(TAG, "bindPreviewService");
        Intent intent = new Intent(this, CameraPreviewService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see android.app.Service#onDestroy()
     */
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
