
package com.demo.camera;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.ToneGenerator;
import android.opengl.GLES11Ext;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video;
import android.text.TextUtils;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import com.demo.camera.utils.GlUtil;
import com.demo.camera.utils.LogUtils;
import com.demo.camera.utils.Storage;
import com.ibotn.ibotncameraservice.IIbotnCameraService;
import com.ibotn.ibotncameraservice.IReleaseCamera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import static java.lang.Math.abs;


public class CameraPreviewService extends Service
        implements PreviewCallback {

    public static final int OCCUPANCY_FACE = 1;
    public static final int OCCUPANCY_IBOTNCAMERA2 = 12;
    public static final int OCCUPANCY_IBOTNCAMERA = 11;
    public static final int OCCUPANCY_IBOTNSCAN = 15;
    public static final int OCCUPANCY_IBOTNPHONE = 25;
    public static final int OCCUPANCY_UNKOWN = 99; //绑定服务失败

    private static final String TAG = CameraPreviewService.class.getSimpleName();
    private static final String VOICECOMMAND_CONTROL_EXTRA = "is_start";
    public static CameraPreviewService mInstance;

    public static ArrayList<Activity> activitys = new ArrayList<>();

    private static final int MESSAGE_TAKE_PICTURE = 1001;
    private static final int MESSAGE_START_VIDEO_RECORDING = 1002;
    private static final int MESSAGE_PREPARE_MEDIARECORDER = 1003;
    private static final int MESSAGE_REPEAT_PICTURE = 1004;
    private static final int MESSAGE_START_TIME_LAPSE_VIDEO_RECORDING = 1005;
    private static final int MESSAGE_STOP_VIDEO_RECORDING = 1006;

    public static final Size SIZE_5M = new Size(2592, 1944);
    public static final Size SIZE_1080P = new Size(1920, 1080);
    public static final Size SIZE_720P = new Size(1280, 720);
    public static final Size SIZE_720 = new Size(720, 480);
    public static final Size SIZE_VGA = new Size(640, 480);
    public static final Size SIZE_QVGA = new Size(320, 240);
    public static final Size DEFAULT_PREVIEW_SIZE = SIZE_720P;

    private static final String COMMAND_CALLER = "service";
    public static final String SUPER_CALLER = "super";

    public static final int DELAYED_TIME_AFTER_SNAPSHOT = 500;

    private BaseServiceBinder mBinder = new BaseServiceBinder();
    private Camera camera;  // Only non-null while capturing.
    //private MyCamView mCameraView;
    private final Object cameraIdLock = new Object();
    private int id = 0;
    private Camera.CameraInfo info;
    private Size requestSize;
    private SurfaceTexture mSurface = null;
    private CameraEventsHandler eventsHandler;
    private boolean firstFrameReported;
    private MediaRecorder mMediaRecorder;
    private boolean mMediaRecorderRecording;
    private static ImageFileNamer sImageFileNamer;
    private NamedImages mNamedImages;
    private ContentResolver mContentResolver;
    private MediaSaveService mMediaSaveService;
    private ContentValues mCurrentVideoValues;
    private String mCurrentVideoFilename;
    private long mRecordingStartTime = 0;
    private boolean mIsRecording = false;
    private boolean mIsRecordingWithAudio = false;
    private boolean mIsCapturing = false;
    private String mCapturingCaller = "";
    private String photoFile = "";
    private EncodeJpegHandler mEncodeJpegHandler = null;
    private final Object mStorageSpaceLock = new Object();
    private long mStorageSpaceBytes = Storage.LOW_STORAGE_THRESHOLD_BYTES;
    private OnStorageUpdateDoneListener mStorageSpaceListener;
    private boolean mCaptureTimeLapse = false;
    // Default 0. If it is larger than 0, the camcorder is in time lapse mode.
    private int mTimeBetweenTimeLapseFrameCaptureMs = 0;
    private CamcorderProfile mProfile;
    private CameraPreviewFrameHandler mCameraPreviewHandler;
    public static boolean CAMERA_RELEASE = true;
    //private YuvQueue yuvQueue = new YuvQueue(EncodeJpegTask.QUEUE_SIZE);

    public static boolean isReady() {
        return (mInstance != null);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder b) {
            mMediaSaveService = ((MediaSaveService.LocalBinder) b).getService();
            LogUtils.d(TAG, "mMediaSaveService onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            if (mMediaSaveService != null) {
                mMediaSaveService.setListener(null);
                mMediaSaveService = null;
                LogUtils.d(TAG, "mMediaSaveService onServiceDisconnected");
            }
        }
    };


    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_TAKE_PICTURE) {
                android.hardware.Camera.PictureCallback jpeg = (android.hardware.Camera.PictureCallback) msg.obj;
                Size size = new Size(msg.arg1, msg.arg2);
                eventsHandler = null;
                //(new EncodeJpegTask(requestedWidth, requestedHeight, yuvQueue)).start();
                takePicture(jpeg, size);
            } else if (msg.what == MESSAGE_START_VIDEO_RECORDING) {
                eventsHandler = null;
                boolean withAudio = ((Boolean) msg.obj).booleanValue();
                //(new EncodeJpegTask(requestedWidth, requestedHeight, yuvQueue)).start();
                startVideoRecording(withAudio);
            } else if (msg.what == MESSAGE_PREPARE_MEDIARECORDER) {
                boolean withAudio = ((Boolean) msg.obj).booleanValue();
                mRecordingStartTime = SystemClock.uptimeMillis();
                initializeRecorder(generateVideoFilename(MediaRecorder.OutputFormat.MPEG_4), withAudio);
                startRecording();
            } else if (msg.what == MESSAGE_REPEAT_PICTURE) {
                TakePictureHandler mPicHandler = (TakePictureHandler) msg.obj;
                takePicture(new JpegPictureCallback(mPicHandler), SIZE_5M);
            } else if (msg.what == MESSAGE_START_TIME_LAPSE_VIDEO_RECORDING) {
                eventsHandler = null;
                startTimeLapseVideoRecording();
            } else if (msg.what == MESSAGE_STOP_VIDEO_RECORDING) {
                if (!isTheActivityRunning(CameraPreviewService.this, "com.demo.ibotncamera.CameraVideoActivity")) {
                    eventsHandler = null;
                    stopVideoRecording();
                }
            }
        }
    };

    public interface OnStorageUpdateDoneListener {
        public void onStorageUpdateDone(boolean uiDisabled, String message);
    }

    private final MediaSaveService.OnMediaSavedListener mOnVideoSavedListener =
            new MediaSaveService.OnMediaSavedListener() {
                @Override
                public void onMediaSaved(String filePath) {
                    if (filePath != null) {
                        LogUtils.e(TAG," onMediaSaved updateStorageSpaceAndHint ");
                        videoHandler.onVideoRecordingSaved(mCurrentVideoFilename.replace(".tmp", ""));
                        updateStorageSpaceAndHint();
                    } else {
                        deleteVideoFile(mCurrentVideoFilename);
                        mCurrentVideoFilename = null;
                    }
                }
            };

    private void bindMediaSaveService() {
        LogUtils.d(TAG, "bindMediaSaveService");
        Intent intent = new Intent(this, MediaSaveService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindMediaSaveService() {
        LogUtils.d(TAG, "unbindMediaSaveService");
        if (mMediaSaveService != null) {
            unbindService(mConnection);
            mMediaSaveService.setListener(null);
            mMediaSaveService = null;
        }
    }

    // Camera error callback.
    private final Camera.ErrorCallback cameraErrorCallback =
            new Camera.ErrorCallback() {
                @Override
                public void onError(int error, Camera camera) {
                    String errorMessage;
                    if (error == android.hardware.Camera.CAMERA_ERROR_SERVER_DIED) {
                        errorMessage = "Camera server died!";
                    } else {
                        errorMessage = "Camera error: " + error;
                    }
                    LogUtils.e(TAG, errorMessage);
                    if (eventsHandler != null) {
                        eventsHandler.onCameraError(errorMessage);
                    }
                }
            };

    public static class Size {
        private int mWidth, mHeight;

        public Size(int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }
    }

    public interface EncodeJpegHandler {
        void onEncodeDone(byte[] yuv);
    }

    public interface TakePictureHandler {
        void onPictureDone(String title);
    }

    private interface CameraEventsHandler {
        // Camera error handler - invoked when camera stops receiving frames
        // or any camera exception happens on camera thread.
        void onCameraError(String errorDescription);

        // Callback invoked when camera is opening.
        void onCameraOpening(int cameraId);

        // Callback invoked when first camera frame is available after camera is opened.
        void onFirstFrameAvailable();

        // Callback invoked when camera closed.
        void onCameraClosed();
    }

    public interface VideoRecordingHandler {
        void onVideoRecordingStop();

        void onVideoRecordingError(String error);

        void onVideoRecordingSaved(String filePath);
    }

    public interface CameraPreviewFrameHandler {
        void onPreviewFrame(final byte[] data, Camera camera);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d(TAG, " onCreate");

        bindIbotnCameraService();

        mInstance = this;
        mNamedImages = new NamedImages();
        mContentResolver = getContentResolver();
        sImageFileNamer = new ImageFileNamer(
                getString(R.string.image_file_name_format));
        bindMediaSaveService();
        updateStorageSpaceAndHint();


    }

    private void bindIbotnCameraService() {
        LogUtils.e(TAG, " bind ibotncameraservice");
        Intent intent = new Intent();
        ComponentName component = new ComponentName("com.demo.ibotncameraservice", "com.demo.ibotncameraservice.IbotnCameraService");
        intent.setComponent(component);
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy");
        if (mIsRecording) {
            stopRecording();
            mIsRecording = false;
        }
        stopPreview();
        unbindMediaSaveService();
        unbindService(conn);
    }

    private void encodeJpeg(String filePath, EncodeJpegHandler jpegHandler) {
        //photoFile = sImageFileNamer.generateName(System.currentTimeMillis());
        photoFile = filePath;
        mEncodeJpegHandler = jpegHandler;
        mIsCapturing = true;
    }

    private String createName(long dateTaken) {
        Date date = new Date(dateTaken);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                getString(R.string.video_file_name_format));

        return dateFormat.format(date);
    }

    private String convertOutputFormatToFileExt(int outputFileFormat) {
        if (outputFileFormat == MediaRecorder.OutputFormat.MPEG_4) {
            return ".mp4";
        }
        return ".3gp";
    }

    private String convertOutputFormatToMimeType(int outputFileFormat) {
        if (outputFileFormat == MediaRecorder.OutputFormat.MPEG_4) {
            return "video/mp4";
        }
        return "video/3gpp";
    }

    private String generateVideoFilename(int outputFileFormat) {
        long dateTaken = System.currentTimeMillis();
        String title = createName(dateTaken);
        // Used when emailing.
        String filename = title + convertOutputFormatToFileExt(outputFileFormat);
        String mime = convertOutputFormatToMimeType(outputFileFormat);
        String path = Storage.DIRECTORY + '/' + filename;
        String tmpPath = path + ".tmp";
        mCurrentVideoValues = new ContentValues(9);
        mCurrentVideoValues.put(Video.Media.TITLE, title);
        mCurrentVideoValues.put(Video.Media.DISPLAY_NAME, filename);
        mCurrentVideoValues.put(Video.Media.DATE_TAKEN, dateTaken);
        mCurrentVideoValues.put(MediaColumns.DATE_MODIFIED, dateTaken / 1000);
        mCurrentVideoValues.put(Video.Media.MIME_TYPE, mime);
        mCurrentVideoValues.put(Video.Media.DATA, path);
        /**
         * 当前视频文件保存路径，例如：
         * 04-20 16:13:54.442: D/CameraPreviewService(1015): New video filename: /storage/sdcard/DCIM/Camera/VID_20170420_161354.mp4.tmp
         */
        LogUtils.d(TAG, "New video filename: " + tmpPath);
        mCurrentVideoFilename = tmpPath;
        return tmpPath;
    }

    private void saveVideo() {
        long duration = SystemClock.uptimeMillis() - mRecordingStartTime;
        if (duration > 0) {
            if (mCaptureTimeLapse) {
                duration = getTimeLapseVideoLength(duration);
            }
        } else {
            LogUtils.w(TAG, "Video duration <= 0 : " + duration);
        }
        if (mCurrentVideoFilename == null) return;
        mCurrentVideoValues.put(Video.Media.SIZE, new File(mCurrentVideoFilename).length());
        mCurrentVideoValues.put(Video.Media.DURATION, duration);
        mMediaSaveService.addVideo(mCurrentVideoFilename, duration,
                mCurrentVideoValues, mOnVideoSavedListener, mContentResolver);
        mCurrentVideoValues = null;
    }

    private void deleteVideoFile(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return;
        LogUtils.v(TAG, "Deleting video " + fileName);
        File f = new File(fileName);
        if (f.exists() && !f.delete()) {
            LogUtils.v(TAG, "Could not delete " + fileName);
        }
    }

    //默认处理
    private CameraPreviewService.VideoRecordingHandler videoHandler = null;


    public class BaseServiceBinder extends Binder {
        public boolean isRecording() {
            return mMediaRecorderRecording;
        }

        public boolean canUseCamera(int yourLevel) {

            /*if (ibotnCameraService == null) {
                LogUtils.e(TAG, "setupCamera bind ibotncameraservice failed!!!");
                return false;
            } else
                LogUtils.d(TAG, "setupCamera bind ibotncameraservice success!!!");*/

            /*try {
                return ibotnCameraService.canUseCamera(releaseCamera, yourLevel);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }*/
            return true;
        }

        public int getLevel() {

            if (ibotnCameraService == null) {
                LogUtils.e(TAG, "setupCamera bind ibotncameraservice failed!!!");
                return OCCUPANCY_UNKOWN;
            } else
                LogUtils.d(TAG, "setupCamera bind ibotncameraservice success!!!");

            try {
                return ibotnCameraService.getLevel();
            } catch (RemoteException e) {
                e.printStackTrace();
                return OCCUPANCY_UNKOWN;
            }
        }

        public boolean startPreview(SurfaceTexture surface, Size size, String caller, OnStorageUpdateDoneListener storageSpaceListener, CameraPreviewFrameHandler handler) {
            LogUtils.d(TAG, "BaseServiceBinder startPreview");
            mSurface = surface;
            mStorageSpaceListener = storageSpaceListener;
            //mCameraView = null;
            mCameraPreviewHandler = handler;
			/*
			if( camera != null)
			{
				LogUtils.d(TAG, "BaseServiceBinder startPreview1");
				CameraPreviewService.this.stopPreview();
				//return true;
			}
			*/
            return CameraPreviewService.this.startPreview(size, caller);
        }

        public void stopPreview(String caller) {
            if (caller.equals(mCapturingCaller) || SUPER_CALLER.equals(caller))
                CameraPreviewService.this.stopPreview();
            mStorageSpaceListener = null;
            mCameraPreviewHandler = null;
        }

        public void takePicture(Size size, TakePictureHandler takePicHandler) {
            CameraPreviewService.this.takePicture(new JpegPictureCallback(takePicHandler), size);
        }

        public void startVideoRecording(boolean withAudio, VideoRecordingHandler myVideoRecodingHandler) {
            if (myVideoRecodingHandler != null)
                videoHandler = myVideoRecodingHandler;
            mTimeBetweenTimeLapseFrameCaptureMs = 0;
            mCaptureTimeLapse = false;
            //if (mCameraView == null)
            CameraPreviewService.this.startVideoRecording(withAudio);
            handler.sendEmptyMessageDelayed(MESSAGE_STOP_VIDEO_RECORDING, 20000);
        }


        public void stopVideoRecording() {

            //if (mCameraView == null)
            CameraPreviewService.this.stopVideoRecording();
        }

        public void startTimeLapseVideoRecording(int timeBetweenTimeLapseFrameCaptureMs) {
            mTimeBetweenTimeLapseFrameCaptureMs = timeBetweenTimeLapseFrameCaptureMs;
            mCaptureTimeLapse = (mTimeBetweenTimeLapseFrameCaptureMs != 0);
            CameraPreviewService.this.startTimeLapseVideoRecording();
        }

        public long getTimeLapseVideoLength(long deltaMs) {
            return CameraPreviewService.this.getTimeLapseVideoLength(deltaMs);
        }
    }

    private static class ImageFileNamer {
        private final SimpleDateFormat mFormat;

        // The date (in milliseconds) used to generate the last name.
        private long mLastDate;

        // Number of names generated for the same second.
        private int mSameSecondCount;

        public ImageFileNamer(String format) {
            mFormat = new SimpleDateFormat(format);
        }

        public String generateName(long dateTaken) {
            Date date = new Date(dateTaken);
            String result = mFormat.format(date);

            // If the last name was generated for the same second,
            // we append _1, _2, etc to the name.
            if (dateTaken / 1000 == mLastDate / 1000) {
                mSameSecondCount++;
                result += "_" + mSameSecondCount;
            } else {
                mLastDate = dateTaken;
                mSameSecondCount = 0;
            }

            return result;
        }
    }

    public static class NamedImages {
        private Vector<NamedEntity> mQueue;

        public NamedImages() {
            mQueue = new Vector<NamedEntity>();
        }

        public void nameNewImage(long date) {
            NamedEntity r = new NamedEntity();
            r.title = sImageFileNamer.generateName(date);
            r.date = date;
            mQueue.add(r);
        }

        public NamedEntity getNextNameEntity() {
            synchronized (mQueue) {
                if (!mQueue.isEmpty()) {
                    return mQueue.remove(0);
                }
            }
            return null;
        }

        public static class NamedEntity {
            public String title;
            public long date;
        }
    }


    private final class JpegPictureCallback
            implements PictureCallback {
        //Location mLocation;
        private TakePictureHandler mTakePictureHandler;

        public JpegPictureCallback(TakePictureHandler takePicHandler/*Location loc*/) {
            mNamedImages.nameNewImage(System.currentTimeMillis());
            mTakePictureHandler = takePicHandler;
        }

        private MediaSaveService.OnMediaSavedListener listener = new MediaSaveService.OnMediaSavedListener() {
            @Override
            public void onMediaSaved(String filePath) {
                if (mTakePictureHandler != null) {
                    mTakePictureHandler.onPictureDone(filePath);
                }
            }
        };

        @Override
        public void onPictureTaken(final byte[] jpegData, Camera camera) {
//            String message = "onPictureTaken:" + jpegData.length;
            Parameters mParameters = camera.getParameters();
            int orientation = 0; //Exif.getOrientation(exif);
            Camera.Size s = mParameters.getPictureSize();
            int width, height;
            width = s.width;
            height = s.height;
            LogUtils.d(TAG, "onPictureTaken " + " width=" + width + " height=" + height);
            NamedImages.NamedEntity name = mNamedImages.getNextNameEntity();
            String title = (name == null) ? null : name.title;
            long date = (name == null) ? -1 : name.date;

            LogUtils.d(TAG, " onPictureTaken title = " + title + " date = " + date);

            if (mMediaSaveService != null) {
                mMediaSaveService.addImage(
                        jpegData, title, date, null, width, height,
                        orientation, listener, mContentResolver);
            }
            if (mCapturingCaller.equals(COMMAND_CALLER)) {
                stopPreview();
            }
			/*
			else if (mCameraView != null) {
				camera.setPreviewCallback(mCameraView);
				camera.startPreview();
			}*/
            else {
                //触发该service所实现的PreviewCallback预览回调
                startPreviewOnCameraThread(requestSize);
            }


            updateStorageSpaceAndHint();
            //Message msg = handler.obtainMessage(MESSAGE_REPEAT_PICTURE, mTakePictureHandler);
            //handler.sendMessageDelayed(msg, DELAYED_TIME_AFTER_SNAPSHOT);
        }
    }

    private int getDeviceOrientation() {
        int orientation = 0;

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        switch (wm.getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_90:
                orientation = 90;
                break;
            case Surface.ROTATION_180:
                orientation = 180;
                break;
            case Surface.ROTATION_270:
                orientation = 270;
                break;
            case Surface.ROTATION_0:
            default:
                orientation = 0;
                break;
        }
        return orientation;
    }

    private int getFrameOrientation() {
        int rotation = getDeviceOrientation();
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            rotation = 360 - rotation;
        }
        return (info.orientation + rotation) % 360;
    }

    public void restartPreview() {
        stopPreview();
        startPreview(requestSize, mCapturingCaller);
    }

    private boolean startPreview(Size size, String caller) {
        Throwable error = null;
        if (camera != null) {
            mInstance = null;
            error = new RuntimeException("Camera has already been started.");
            stopSelf();
            LogUtils.e(TAG, "111 startPreview return false");
            return false;
        }
        try {
            synchronized (cameraIdLock) {
                LogUtils.d(TAG, "Opening camera " + id);
                firstFrameReported = false;
                if (eventsHandler != null) {
                    eventsHandler.onCameraOpening(id);
                }
                mCapturingCaller = caller;
                camera = Camera.open(id);
                /*try {
                    ibotnCameraService.setLevel(OCCUPANCY_IBOTNCAMERA2);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }*/
                info = new Camera.CameraInfo();
                Camera.getCameraInfo(id, info);
                CAMERA_RELEASE = false;
            }
            LogUtils.d(TAG, "Camera orientation: " + info.orientation +
                    " .Device orientation: " + getDeviceOrientation());
            camera.setErrorCallback(cameraErrorCallback);
//            updateStorageSpaceAndHint();
            startPreviewOnCameraThread(size);

            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            error = e;
            mInstance = null;
            stopSelf();
        }
        LogUtils.e(TAG, "startCapture failed", error);
        stopPreview();
        if (eventsHandler != null) {
            eventsHandler.onCameraError("Camera can not be started.");
        }
        LogUtils.e(TAG, "222 startPreview return false");
        return false;
    }

    private void startPreviewOnCameraThread(Size size) {
        if (camera == null) {
            LogUtils.e(TAG, "Calling startPreviewOnCameraThread on stopped camera.");
            return;
        }

        requestSize = size;

        if (mSurface == null)
            mSurface = new SurfaceTexture(GlUtil.generateTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES));
        try {
            camera.setPreviewTexture(mSurface);
        } catch (IOException e) {
            LogUtils.e(TAG, "setPreviewTexture failed", e);
            throw new RuntimeException(e);
        }

        // Find closest supported format for |width| x |height| @ |framerate|.
        final Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);
        //List<Integer> formats = parameters.getSupportedPreviewFormats();
        //LogUtils.d(TAG, "getSupportedPreviewFormats:" + parameters.getSupportedPreviewFormats());

        // Update camera parameters.防抖动
        LogUtils.d(TAG, "isVideoStabilizationSupported: " +
                parameters.isVideoStabilizationSupported());
        if (parameters.isVideoStabilizationSupported()) {
            parameters.setVideoStabilization(true);
        }
        //parameters.setPreviewSize(captureFormat.width, captureFormat.height);
        parameters.setPreviewSize(requestSize.getWidth(), requestSize.getHeight());
        //parameters.setPictureSize(requestedWidth,requestedHeight);
        LogUtils.d(TAG, "setPreviewSize: " + requestSize.getWidth() + " x " + requestSize.getHeight());

        // (Re)start preview.
        //LogUtils.d(TAG, "Start capturing: ");
        //this.captureFormat = captureFormat;

        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }

        camera.setParameters(parameters);
		/*
		if (mCameraView != null)
			camera.setPreviewCallback(mCameraView);
		else
		*/
        camera.setPreviewCallback(this);
        camera.startPreview();

    }

    private void stopPreview() {
        LogUtils.d(TAG, "stopPreview");
        if (camera == null) {
            LogUtils.e(TAG, "Calling stopCapture() for already stopped camera.");
            return;
        }

        LogUtils.d(TAG, "Stop preview.");
        camera.stopPreview();
        camera.setPreviewCallback(null);
        LogUtils.d(TAG, "Release camera.");
        camera.release();
        camera = null;
        CAMERA_RELEASE = true;
        if (eventsHandler != null) {
            eventsHandler.onCameraClosed();
        }
        stopRecording();
        handler.removeMessages(MESSAGE_REPEAT_PICTURE);


        /*//恢复摄像头占用级别
        if (ibotnCameraService != null)
            try {
                ibotnCameraService.setLevel(0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        else {
            LogUtils.e(TAG, "ibotnCameraService == null");
        }*/


    }

    private IIbotnCameraService ibotnCameraService;
    private IReleaseCamera releaseCamera = new IReleaseCamera.Stub() {
        @Override
        public boolean relaseCamera() throws RemoteException {
            try {
                LogUtils.e(TAG, "relaseCamera start");
                stopPreview();
                LogUtils.e(TAG, "activitys size = " + activitys.size());
                if (!activitys.isEmpty())
                    for (Activity activity : activitys)
                        activity.finish();
                LogUtils.e(TAG, "relaseCamera end");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(TAG, "relaseCamera failed " + e.getMessage());
                return false;
            }

        }
    };

    private IBinder.DeathRecipient recipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            LogUtils.e(TAG, " binderDied "+Thread.currentThread().getId());
        }
    };
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                service.linkToDeath(recipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            ibotnCameraService = IIbotnCameraService.Stub.asInterface(service);
            if (ibotnCameraService == null)
                LogUtils.e(TAG, "onServiceConnected ibotnCameraService init failed !!! name = " + name);
            else
                LogUtils.e(TAG, "onServiceConnected ibotnCameraService init success name = " + name);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.e(TAG, "onServiceDisconnected name = " + name);
            ibotnCameraService = null;
            Toast.makeText(CameraPreviewService.this, "IbotnCameraService died", Toast.LENGTH_SHORT).show();

            bindIbotnCameraService();
        }
    };

    private void startVideoRecording(final boolean withAudio) {
        if (!checkIsSpaceAvailable())
            return;
        if (mMediaRecorderRecording)
            return;
        if (camera == null) {
            eventsHandler = new CameraEventsHandler() {
                @Override
                public void onFirstFrameAvailable() {
                    Message msg = handler.obtainMessage(MESSAGE_START_VIDEO_RECORDING, Boolean.valueOf(withAudio));
                    handler.sendMessage(msg);
                }

                @Override
                public void onCameraOpening(int cameraId) {
                }

                @Override
                public void onCameraError(String errorDescription) {
                }

                @Override
                public void onCameraClosed() {
                }
            };
            mSurface = null;
            //startPreview(SIZE_720P, COMMAND_CALLER);
            startPreview(SIZE_1080P, COMMAND_CALLER);
            return;
        }
//		if (withAudio)
//			stopVoiceCommand();
        Message prepareMsg = handler.obtainMessage(MESSAGE_PREPARE_MEDIARECORDER, Boolean.valueOf(withAudio));
        handler.sendMessageDelayed(prepareMsg, 500);
    }

    private void startTimeLapseVideoRecording() {
        if (!checkIsSpaceAvailable())
            return;
        if (mMediaRecorderRecording)
            return;
        if (camera == null) {
            eventsHandler = new CameraEventsHandler() {
                @Override
                public void onFirstFrameAvailable() {
                    Message msg = handler.obtainMessage(MESSAGE_START_TIME_LAPSE_VIDEO_RECORDING);
                    handler.sendMessage(msg);
                }

                @Override
                public void onCameraOpening(int cameraId) {
                }

                @Override
                public void onCameraError(String errorDescription) {
                }

                @Override
                public void onCameraClosed() {
                }
            };
            mSurface = null;
            //startPreview(SIZE_720P, COMMAND_CALLER);
            startPreview(DEFAULT_PREVIEW_SIZE, COMMAND_CALLER);
            return;
        }
        mRecordingStartTime = SystemClock.uptimeMillis();
        initializeRecorder(generateVideoFilename(MediaRecorder.OutputFormat.MPEG_4), false);
        startRecording();
    }

    private void stopVideoRecording() {
        //LogUtils.d(TAG, "stopVideoRecording");
        if (camera == null) {
            return;
        }
        stopRecording();
    }

    private void initializeRecorder(final String mVideoFilename, boolean withAudio) {
        mIsRecordingWithAudio = withAudio;
        mMediaRecorder = new MediaRecorder();
        //CamcorderProfile mProfile = CamcorderProfile.get(id, CamcorderProfile.QUALITY_720P);
        int quality = CamcorderProfile.QUALITY_720P; //QUALITY_480P;
        if (mCaptureTimeLapse) quality += 1000;
        mProfile = CamcorderProfile.get(id, quality);
        try {
            LogUtils.d(TAG, "mProfile.fileFormat=" + mProfile.fileFormat + " mProfile.videoBitRate=" + mProfile.videoBitRate + " mProfile.videoFrameWidth=" + mProfile.videoFrameWidth + " mProfile.videoFrameHeight=" + mProfile.videoFrameHeight + " mProfile.videoCodec=" + mProfile.videoCodec + " mProfile.videoFrameRate=" + mProfile.videoFrameRate);
            camera.unlock();
            mMediaRecorder.setCamera(camera);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            if (withAudio && !mCaptureTimeLapse) {
                mProfile.audioSampleRate = 16000;
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mMediaRecorder.setAudioSamplingRate(mProfile.audioSampleRate);
                //mMediaRecorder.setProfile(mProfile);
                mMediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);
                mMediaRecorder.setVideoEncodingBitRate(mProfile.videoBitRate);
                mMediaRecorder.setVideoEncoder(mProfile.videoCodec);
                mMediaRecorder.setAudioEncodingBitRate(mProfile.audioBitRate);
                mMediaRecorder.setAudioChannels(mProfile.audioChannels);
            } else {
                mMediaRecorder.setOutputFormat(mProfile.fileFormat);
                mMediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);
                mMediaRecorder.setVideoEncodingBitRate(mProfile.videoBitRate);
                mMediaRecorder.setVideoEncoder(mProfile.videoCodec);
            }
            mMediaRecorder.setVideoSize(requestSize.getWidth(), requestSize.getHeight());
            mMediaRecorder.setMaxDuration(0);
            mMediaRecorder.setOutputFile(mVideoFilename);
            mMediaRecorder.setMaxFileSize(0);
            mMediaRecorder.setOrientationHint(0);
            if (mCaptureTimeLapse) {
                double fps = 1000 / (double) mTimeBetweenTimeLapseFrameCaptureMs;
                mMediaRecorder.setCaptureRate(fps);
            }
            mMediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "initialize recorder error=" + e);
            releaseMediaRecorder();
        }
    }

    private void releaseMediaRecorder() {
        LogUtils.d(TAG, "Releasing media recorder.");
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    private void stopVoiceCommand() {
        Intent intent = new Intent("com.demo.ibotncamera.ACTION_VOICECOMMAND");//(Utils.ACTION_VOICECOMMAND);
        intent.putExtra(VOICECOMMAND_CONTROL_EXTRA, false);
        sendBroadcast(intent);
    }

    private void startVoiceCommand() {
/*		Intent intent = new Intent(VOICECOMMAND_CONTROL_ACTION);
		intent.putExtra(VOICECOMMAND_CONTROL_EXTRA, true);
		sendBroadcast(intent);*/
    }


    private void startRecording() {
        LogUtils.d(TAG, "startRecording");
        if (mMediaRecorder == null) {
            LogUtils.d(TAG, "initialize recorder fail!");
            return;
        }

        try {
            mMediaRecorder.start(); // Recording is now started
            mMediaRecorderRecording = true;
			/*
			if (mCameraView != null)
				camera.setPreviewCallback(mCameraView);
			else
			*/
            camera.setPreviewCallback(this);
        } catch (RuntimeException e) {
            e.printStackTrace();
            LogUtils.e(TAG, "Could not start media recorder. " + e);
            releaseMediaRecorder();
            // If start fails, frameworks will not lock the camera for us.
            camera.lock();
            return;
        }
    }

    private boolean stopRecording() {
        LogUtils.d(TAG, "stopRecording");
        if (handler.hasMessages(MESSAGE_STOP_VIDEO_RECORDING))
            handler.removeMessages(MESSAGE_STOP_VIDEO_RECORDING);
        boolean result = false;
        if (mMediaRecorderRecording && mMediaRecorder != null) {

            try {
                mMediaRecorder.stop();
                mMediaRecorderRecording = false;
//                updateStorageSpaceAndHint();
                saveVideo();
                releaseMediaRecorder();
                if (videoHandler != null) {
                    videoHandler.onVideoRecordingStop();
                }
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(TAG, "stop fail " + e);
                deleteVideoFile(mCurrentVideoFilename);
                releaseMediaRecorder();
                mCurrentVideoFilename = null;
                if (videoHandler != null) {
                    videoHandler.onVideoRecordingError(getResources().getString(R.string.error_too_fast));
                }
            }
        }
        return result;
    }

    private void takePicture(final android.hardware.Camera.PictureCallback jpeg, final Size size) {
        LogUtils.d(TAG, "takePicture width=" + size.getWidth() + " height=" + size.getHeight());
        if (!checkIsSpaceAvailable())
            return;

        if (mMediaRecorderRecording)
            return;
        try {
            if (camera == null) {
                eventsHandler = new CameraEventsHandler() {
                    @Override
                    public void onFirstFrameAvailable() {
                        Message msg = handler.obtainMessage(MESSAGE_TAKE_PICTURE, size.getWidth(), size.getHeight(), jpeg);
                        //LogUtils.d(TAG, "MESSAGE_TAKE_PICTURE w = " + msg.arg1 + ", h= " + msg.arg2);
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onCameraOpening(int cameraId) {
                    }

                    @Override
                    public void onCameraError(String errorDescription) {
                    }

                    @Override
                    public void onCameraClosed() {
                    }
                };
                mSurface = null;
                //startPreview(SIZE_720P, COMMAND_CALLER);
                startPreview(DEFAULT_PREVIEW_SIZE, COMMAND_CALLER);
                return;
            }

            final Camera.Parameters parameters = camera.getParameters();
            final Camera.Size pictureSize = getClosestSupportedSize(
                    parameters.getSupportedPictureSizes(), size.getWidth(), size.getHeight());
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            camera.setParameters(parameters);
            LogUtils.d(TAG, "PictureSize width=" + parameters.getPictureSize().width + " height=" + parameters.getPictureSize().height);
            camera.setPreviewCallback(null);
            camera.enableShutterSound(true);
            //camera.startUvcCapture(); //2nd camera capture
            camera.takePicture(shutterCallback, null, jpeg);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "take picture error!");
        }
    }

    private ToneGenerator tone;
    //快门按下的时候onShutter()被回调
    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            if (tone == null)
                //发出提示用户的声音
                tone = new ToneGenerator(AudioManager.STREAM_MUSIC,
                        ToneGenerator.MAX_VOLUME);
            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
        }
    };

    private abstract class ClosestComparator<T> implements Comparator<T> {
        // Difference between supported and requested parameter.
        abstract int diff(T supportedParameter);

        @Override
        public int compare(T t1, T t2) {
            return diff(t1) - diff(t2);
        }
    }

    private Camera.Size getClosestSupportedSize(
            List<Camera.Size> supportedSizes, final int requestedWidth, final int requestedHeight) {
        return Collections.min(supportedSizes,
                new ClosestComparator<Camera.Size>() {
                    @Override
                    int diff(Camera.Size size) {
                        return abs(requestedWidth - size.width) + abs(requestedHeight - size.height);
                    }
                });
    }

    private long getTimeLapseVideoLength(long deltaMs) {
        // For better approximation calculate fractional number of frames captured.
        // This will update the video time at a higher resolution.
        if (mCaptureTimeLapse && (mTimeBetweenTimeLapseFrameCaptureMs > 0)) {
            double numberOfFrames = (double) deltaMs / mTimeBetweenTimeLapseFrameCaptureMs;
            return (long) (numberOfFrames / mProfile.videoFrameRate * 1000);
        } else {
            return deltaMs;
        }
    }

    @Override
    public void onPreviewFrame(final byte[] data, Camera callbackCamera) {
        //LogUtils.d(TAG, "onPreviewFrame length=" + data.length);
        //index++;
        if (camera == null) {
            return;
        }
        if (camera != callbackCamera) {
            throw new RuntimeException("Unexpected camera in callback!");
        }

        //final long captureTimeNs = TimeUnit.MILLISECONDS.toNanos(SystemClock.elapsedRealtime());

        if (eventsHandler != null && !firstFrameReported) {
            eventsHandler.onFirstFrameAvailable();
            firstFrameReported = true;
        }

        if (mCameraPreviewHandler != null) {
            mCameraPreviewHandler.onPreviewFrame(data, callbackCamera);
        }

        if (mIsCapturing) {
            mIsCapturing = false;
            new Thread() {
                public void run() {
                    final byte[] yuvdata = data;
                    LogUtils.d(TAG, "yuvdata = " + yuvdata);
                    //long currindex = index;
                    try {
                        //LogUtils.d(TAG, "onPreviewFrame thread index=" + currindex + " length=" + data.length);
                        YuvImage yuv = new YuvImage(yuvdata, ImageFormat.NV21, requestSize.getWidth(), requestSize.getHeight(), null);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        yuv.compressToJpeg(new Rect(0, 0, requestSize.getWidth(), requestSize.getHeight()), 100, out);
                        byte[] imageBytes = out.toByteArray();
                        LogUtils.d(TAG, "imageBytes " + imageBytes);
                        Storage.writeFile(photoFile, imageBytes);
                        if (mEncodeJpegHandler != null) {
                            mEncodeJpegHandler.onEncodeDone(yuvdata);
                            mEncodeJpegHandler = null;
                        }
                        imageBytes = null;
                        out.close();
                    } catch (Exception e) {
                        LogUtils.e(TAG, "Failed to close file", e);
                    }
                }

                ;
            }.start();
        }
        // set to SDK.
    }

    private boolean checkIsSpaceAvailable() {
        boolean available = (getStorageSpaceBytes() > Storage.LOW_STORAGE_THRESHOLD_BYTES);
        if (!available)
            updateStorageHint(getStorageSpaceBytes());
        return available;
    }

    protected long getStorageSpaceBytes() {
        synchronized (mStorageSpaceLock) {
            return mStorageSpaceBytes;
        }
    }

    protected void updateStorageSpaceAndHint() {
        /*
         * We execute disk operations on a background thread in order to
         * free up the UI thread.  Synchronizing on the lock below ensures
         * that when getStorageSpaceBytes is called, the main thread waits
         * until this method has completed.
         *
         * However, .execute() does not ensure this execution block will be
         * run right away (.execute() schedules this AsyncTask for sometime
         * in the future. executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
         * tries to execute the task in parellel with other AsyncTasks, but
         * there's still no guarantee).
         * e.g. don't call this then immediately call getStorageSpaceBytes().
         * Instead, pass in an OnStorageUpdateDoneListener.
         */
        (new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... arg) {
                synchronized (mStorageSpaceLock) {
                    mStorageSpaceBytes = Storage.getAvailableSpace();
                    return mStorageSpaceBytes;
                }
            }

            @Override
            protected void onPostExecute(Long bytes) {
                updateStorageHint(bytes);
                // This callback returns after I/O to check disk, so we could be
                // pausing and shutting down. If so, don't bother invoking.
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected void updateStorageHint(long storageSpace) {
        //if (!mIsActivityRunning) {
        //	return;
        //}

        String message = null;
        if (storageSpace == Storage.UNAVAILABLE) {
            message = getString(R.string.no_storage);
        } else if (storageSpace == Storage.PREPARING) {
            message = getString(R.string.preparing_sd);
        } else if (storageSpace == Storage.UNKNOWN_SIZE) {
            message = getString(R.string.access_sd_fail);
        } else if (storageSpace == Storage.NEW_DIRECTORY_FAILED)
        {
            message = getString(R.string.pls_restart_device);
        }else if (storageSpace <= Storage.LOW_STORAGE_THRESHOLD_BYTES) {
            message = getString(R.string.spaceIsLow_content);
            /**
             * BUGFIX: fix the recording extends the storage limit, which is the 5.0/5.1 record fluent's loophole.
             *ActionsCode(author:liyuan, change_code)
             */
        }

        if (message != null) {
            LogUtils.w(TAG, "Storage warning: " + message);
			/*if (mStorageHint == null) {
				mStorageHint = OnScreenHint.makeText(this, message);
			} else {
				mStorageHint.setText(message);
			}
			mStorageHint.show();*/

            // Disable all user interactions,
            if (mStorageSpaceListener != null)
                mStorageSpaceListener.onStorageUpdateDone(true, message);
            else {
                Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
			/*mStorageHint.cancel();
			mStorageHint = null;*/

            // Re-enable all user interactions.
            if (mStorageSpaceListener != null)
                mStorageSpaceListener.onStorageUpdateDone(false, null);
        }
    }

    public synchronized static boolean isTheActivityRunning(Context context, String activityname) {
        boolean isRunning = false;
        // Get the Activity Manager
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
        if (taskInfo != null && taskInfo.size() > 0) {
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getClassName().equals(activityname)) {
                isRunning = true;
            }
        }
        return isRunning;
    }
}
