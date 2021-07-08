package com.demo.camera;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.demo.camera.beans.LearnTrajectoryBean;
import com.demo.camera.beans.MessageEvent;
import com.demo.camera.common.ConstControl;
import com.demo.camera.receiver.OperationReceiver;
import com.demo.camera.utils.DialogUtils;
import com.demo.camera.utils.ImageCacheUtil;
import com.demo.camera.utils.LearnTrajectoryUtil;
import com.demo.camera.utils.LogUtils;
import com.demo.camera.utils.Storage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends FullScreenActivity implements TextureView.SurfaceTextureListener, View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int IMAGE_SIZE = 100;


    private TextureView mPreviewContent;
    private Button btn_takevideo, btn_takephoto;
    private CircleImageView img_dcim;
    private CameraPreviewService.BaseServiceBinder mPreviewServcieBinder;
    private Chronometer chronometer;
    private ImageView img_back;
    //连拍，还是普通拍照
    private int take_photo_type = -1;
    private boolean withAudio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e(TAG, " onCreate");
        CameraPreviewService.activitys.add(this);
        setContentView(R.layout.activity_main);
        initViews();
        initListenters();
        take_photo_type = getIntent().getIntExtra(OperationReceiver.EXTRA_CAPTURE_TYPE, -1);
        withAudio = getIntent().getBooleanExtra(OperationReceiver.EXTRA_WITH_AUDIO, false);
        EventBus.getDefault().register(this);

        //开始记录轨迹
        trajectoryHolder.startLearn(new LearnTrajectoryBean(System.currentTimeMillis(),
                "相机",
                LearnTrajectoryUtil.Constant.TYPE_TAKE_PHOTO));
        trajectoryHolder.setTimeSpace(1000);
    }

    /**
     * 记录轨迹的holder
     */
    private LearnTrajectoryUtil.LearnTrajectoryHolder trajectoryHolder = new LearnTrajectoryUtil.LearnTrajectoryHolder();

    /**
     * 连拍张数
     */
    private static final int MAX_NUM_CONTINUOUS_SHOOTING = 5;

    private int ttsCount = 0;
    private static final int MSG_COUNT_DOWN = 1;
    private static final int MSG_FINISH = 3;
    /**
     * 连拍
     */
    private static final int MSG_CONTINUOUS_SHOOTING = 5;
    private static final int MSG_START_VIDEIO = 7;
    /**
     * 连拍时，拍照的个数
     */
    private AtomicInteger aiContinuousPhotoNum = new AtomicInteger();

    private Handler countHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_COUNT_DOWN:
                    if (ttsCount > 0) {
                        ConstControl.startTtsSpeaker(MainActivity.this, tts_number(ttsCount));
                        ttsCount--;
                        countHandler.sendEmptyMessageDelayed(MSG_COUNT_DOWN, 1200);
                    } else {
                        takePicture();
                        countHandler.sendEmptyMessageDelayed(MSG_FINISH, 1000);
                    }
                    break;

                case MSG_FINISH:
                    finish();
                    break;
                case MSG_CONTINUOUS_SHOOTING:
                    if (aiContinuousPhotoNum.getAndAdd(1) < MAX_NUM_CONTINUOUS_SHOOTING) {
                        takePicture();
                        countHandler.sendEmptyMessageDelayed(MSG_CONTINUOUS_SHOOTING, 1000);
                    } else {
                        countHandler.sendEmptyMessageDelayed(MSG_FINISH, 1000);
                    }
                    break;
                case MSG_START_VIDEIO:
                    startAndendRecording();
                    break;
                default:
            }
        }

        private String tts_number(int number) {
            String num = null;
            switch (number) {
                case 1:
                    num = getResources().getString(R.string.tts_1);
                    break;
                case 2:
                    num = getResources().getString(R.string.tts_2);
                    break;
                case 3:
                    num = getResources().getString(R.string.tts_3);
                    break;
                default:
                    num = "error";
                    break;
            }

            return num;
        }
    };

    /**
     * @param captureType 拍照类型;0拍照，1抓拍，2连拍；
     */
    private void dealPhoto(int captureType) {
        if (captureType == 0) {
            startCountDown();
        } else if (captureType == 1) {
//            countHandler.sendEmptyMessageDelayed(MSG_COUNT_DOWN, 1000);

        } else if (captureType == 2) {
            countHandler.sendEmptyMessageDelayed(MSG_CONTINUOUS_SHOOTING, 1000);
        }

    }

    private void startCountDown() {
        ttsCount = 3;
        countHandler.sendEmptyMessageDelayed(MSG_COUNT_DOWN, 1000);
    }

    private void initListenters() {
        btn_takevideo.setOnClickListener(this);
        btn_takephoto.setOnClickListener(this);
        img_dcim.setOnClickListener(this);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
//                LogUtils.d(TAG, "onChronometerTick chronometer " + chronometer.getTransitionName());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e(TAG, "onPause");
        releaseCamera();
        unbindPreviewService();
        chronometer.setVisibility(View.GONE);
    }

    private void releaseCamera() {
        if (mPreviewServcieBinder != null) {
            mPreviewServcieBinder.stopPreview(TAG);
        }
    }

    private void unbindPreviewService() {
        Log.d(TAG, "unbindPreviewService");
        if (mPreviewServcieBinder != null) {
            unbindService(mConnection);
            mPreviewServcieBinder = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i(TAG, "onResume");
        mPreviewContent = (TextureView) findViewById(R.id.preview_content);
        mPreviewContent.setSurfaceTextureListener(this);

        if (withAudio)
            countHandler.sendEmptyMessageDelayed(MSG_START_VIDEIO, 1000);
        else
            dealPhoto(take_photo_type);
        img_dcim.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, " onDestroy");
        CameraPreviewService.activitys.remove(this);
        EventBus.getDefault().unregister(this);

        //结束记录轨迹
        trajectoryHolder.endLearn();
        LearnTrajectoryUtil.sendBro(this,trajectoryHolder);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopRecordEvent(MessageEvent messageEvent)
    {
        LogUtils.e(TAG," onStopRecordEvent messageEvent = "+messageEvent);
        if(countHandler != null && countHandler.hasMessages(MSG_START_VIDEIO))
            countHandler.removeMessages(MSG_START_VIDEIO);
        startAndendRecording();
    }

    private void initViews() {
        mPreviewContent = (TextureView) findViewById(R.id.preview_content);
        mPreviewContent.setSurfaceTextureListener(this);
        btn_takevideo = (Button) findViewById(R.id.btn_takevideo);
        btn_takephoto = (Button) findViewById(R.id.btn_takephoto);
        img_dcim = (CircleImageView) findViewById(R.id.img_dcim);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        LogUtils.d(TAG, "onSurfaceTextureAvailable,width=" + width + ",height=" + height);
        mSurface = surfaceTexture;
        float ratio = (float) CameraPreviewService.SIZE_1080P.getWidth() / (float) CameraPreviewService.SIZE_1080P.getHeight();
        ViewGroup.LayoutParams param = mPreviewContent.getLayoutParams();
        float minHeight = Math.min((float) width / ratio, (float) height);
        param.width = (int) (minHeight * ratio);
        param.height = (int) minHeight;
        LogUtils.d(TAG, "onSurfaceTextureAvailable,param.width =" + param.width  + ",param.height =" + param.height );
        mPreviewContent.setLayoutParams(param);
        bindPreviewService();
    }

    private void bindPreviewService() {
        LogUtils.d(TAG, "bindPreviewService");
        Intent intent = new Intent(this, CameraPreviewService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        LogUtils.d(TAG, "onSurfaceTextureSizeChanged");
        mSurface = surfaceTexture;
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        LogUtils.d(TAG, "onSurfaceTextureDestroyed");
        mSurface = null;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//        LogUtils.d(TAG, "onSurfaceTextureUpdated");
        mSurface = surfaceTexture;
    }


    private String mCurrentFilePath = "";

    /**
     * 拍照
     */
    private void takePicture() {
        //保证录像结束后才可以拍照
        if (isVideoing) {
            Toast.makeText(this, getResources().getString(R.string.error_pl_stop_videio), Toast.LENGTH_SHORT).show();
            return;
        }

        //防止重复点击过快
        if (preventMultipleRunPictrue()) {
            Toast.makeText(this, getResources().getString(R.string.error_click_too_fast), Toast.LENGTH_SHORT).show();
            return;
        }

        if (chronometer.getVisibility() == View.VISIBLE)
            chronometer.setVisibility(View.GONE);
        if (mPreviewServcieBinder != null) {
            isPictrueing = true;
            mPreviewServcieBinder.takePicture(CameraPreviewService.SIZE_1080P, new CameraPreviewService.TakePictureHandler() {
                @Override
                public void onPictureDone(String title) {
                    LogUtils.d(TAG, " onPictureDone title " + title + " Thread = " + Thread.currentThread().getName());
                    String filePath = Storage.DIRECTORY + File.separator + title + Storage.JPEG_POSTFIX;
                    File file = new File(filePath);
                    if (file.exists()) {
                        LogUtils.d(TAG, "onPictureDone setBitmap start filePath = " + filePath);
                        Bitmap bitmap = ImageCacheUtil.getResizedBitmap(filePath, null, MainActivity.this, null, IMAGE_SIZE, true);
                        img_dcim.setImageBitmap(bitmap);
                        LogUtils.d(TAG, "onPictureDone setBitmap end filePath = " + filePath);
                        mCurrentFilePath = filePath;
                        img_dcim.setVisibility(View.VISIBLE);
                        mType = "image";
                        isPictrueing = false;
                    }
                }
            });
        }

    }


    private SurfaceTexture mSurface;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder b) {
            mPreviewServcieBinder = (CameraPreviewService.BaseServiceBinder) b;
            LogUtils.d(TAG, "onServiceConnected CameraPreviewServcie onServiceConnected");

            startPreview();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            if (mPreviewServcieBinder != null) {
                mPreviewServcieBinder = null;
                Log.d(TAG, "CameraPreviewServcie onServiceDisconnected");
            }
        }
    };

    private void startPreview() {
        if (mPreviewServcieBinder == null)
            return;
        if (!(mPreviewServcieBinder.canUseCamera(CameraPreviewService.OCCUPANCY_IBOTNCAMERA2)
                && mPreviewServcieBinder.startPreview(mSurface, CameraPreviewService.SIZE_1080P, TAG, null, null))) {
            //正在使用提示
            /*int who = mPreviewServcieBinder.getLevel();
            LogUtils.e(TAG, "setupCamera onFailed who = " + who);
            String who_call = getResources().getString(R.string.camera_call);
            //level 相关定义 0 无应用占用摄像头 1-10 face相关应用 11-20 camera相关应用 21-30 phone相关应用
            if (1 <= who && who <= 10)
                who_call = getResources().getString(R.string.face_call);
            if (11 <= who && who <= 20)
                who_call = getResources().getString(R.string.camera_call);
            if (21 <= who && who <= 30)
                who_call = getResources().getString(R.string.video_call);
            if(31 <= who &&  who <= 40)
                who_call = getResources().getString(R.string.courseware);

            if (who == CameraPreviewService.OCCUPANCY_IBOTNCAMERA2 || who == CameraPreviewService.OCCUPANCY_UNKOWN || who == CameraPreviewService.OCCUPANCY_FACE)
                DialogUtils.showErrorDialog(MainActivity.this, getResources().getString(R.string.please_try_later), new DialogUtils.OkClickListener() {
                    @Override
                    public void okClick() {
                        MainActivity.this.finish();
                    }
                });
            else
                DialogUtils.showErrorDialog(MainActivity.this, who_call + " " + getString(R.string.camera_opened), new DialogUtils.OkClickListener() {
                    @Override
                    public void okClick() {
                        MainActivity.this.finish();
                    }
                });*/
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_takevideo:
                startAndendRecording();
                break;
            case R.id.btn_takephoto:
                takePicture();
                break;
            case R.id.img_dcim:
                lookForPhoto();
                break;
            case R.id.img_back:
                finish();
                break;

        }
    }

    private long mLastCallTimeVideo = 0l;
    private long mLastCallTimePictrue = 0l;

    /**
     * Preventing multiple clicks,run ,....
     *
     * @return true 被阻止了
     */
    public boolean preventMultipleRunVideo() {

        long now = SystemClock.elapsedRealtime();
        if (now - mLastCallTimeVideo <= 2000) {
            return true;
        }
        mLastCallTimeVideo = now;
        return false;
    }

    /**
     * Preventing multiple clicks,run ,....
     *
     * @return true 被阻止了
     */
    public boolean preventMultipleRunPictrue() {

        long now = SystemClock.elapsedRealtime();
        if (now - mLastCallTimePictrue <= 1000) {
            return true;
        }
        mLastCallTimePictrue = now;
        return false;
    }

    private boolean isVideoing = false;
    private boolean isPictrueing = false;

    private void startAndendRecording() {
        //保证拍照结束后才可以录像
        if (isPictrueing) {
            Toast.makeText(this, getResources().getString(R.string.error_click_too_fast), Toast.LENGTH_SHORT).show();
            return;
        }

        //防止重复点击过快
        if (preventMultipleRunVideo()) {
            Toast.makeText(this, getResources().getString(R.string.error_click_too_fast), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPreviewServcieBinder != null) {
            if (mPreviewServcieBinder.isRecording()) {
                mPreviewServcieBinder.stopVideoRecording();

            } else {
                mPreviewServcieBinder.startVideoRecording(true, videoHandler);
                btn_takevideo.setBackgroundResource(R.drawable.selector_bg_stop);
                chronometer.setVisibility(View.VISIBLE);
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                isVideoing = true;
            }
        }

        getSystemService(Context.WINDOW_SERVICE);
    }

    private String mType = "";
    private CameraPreviewService.VideoRecordingHandler videoHandler = new CameraPreviewService.VideoRecordingHandler() {

        @Override
        public void onVideoRecordingStop() {
            LogUtils.d(TAG, "onVideoRecordingStop");
        }

        @Override
        public void onVideoRecordingError(String error) {
            LogUtils.d(TAG, "onVideoRecordingError error " + error);
            if (!TextUtils.isEmpty(error))
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVideoRecordingSaved(String filePath) {
            LogUtils.d(TAG, "onVideoRecordingSaved filePath = " + filePath);
            Bitmap bitmap = ImageCacheUtil.createVideoThumbnail(filePath, IMAGE_SIZE, IMAGE_SIZE);
            LogUtils.d(TAG, "onVideoRecordingSaved bitmap = " + bitmap);
            img_dcim.setImageBitmap(bitmap);
            img_dcim.setVisibility(View.VISIBLE);
            mCurrentFilePath = filePath;
            mType = "video";
            btn_takevideo.setBackgroundResource(R.drawable.selector_bg_takevedio);
            chronometer.stop();
            isVideoing = false;
            //判断是否语音唤醒
            if (withAudio)
                finish();
        }
    };

    private void lookForPhoto() {
    /*    Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse("file://" + mCurrentFilePath), mType + "/*");
        startActivity(intent);*/
//        "com.infomax.ibotncloudplayer", "com.infomax.ibotncloudplayer.activity.Activity_album_video"
        if (isVideoing) {
            Toast.makeText(this, getResources().getString(R.string.error_pl_stop_videio), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setClassName("com.infomax.ibotncloudplayer", "com.infomax.ibotncloudplayer.activity.Activity_album_video");
        intent.putExtra("type",mType);
        startActivity(intent);
    }
}
