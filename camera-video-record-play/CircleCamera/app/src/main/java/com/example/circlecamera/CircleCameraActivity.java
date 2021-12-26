package com.example.circlecamera;

import java.io.File;

import com.example.circlecamera.CircleCameraInterface.CamOpenOverCallback;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;

public class CircleCameraActivity extends Activity implements CamOpenOverCallback {
	private static final String TAG = "CircleCameraActivity";
	private static final int TAKEPHOTO = 1;
	
	CircleCameraSurfaceView mCircleCameraSurfaceView = null;
	float previewRate = -1f;
	private CircleMaskView maskView;

	private ImageButton btnShutter;
	
	Point circlePictureSize = null;
	
	int screenW = 350;//dp
	int screenH = 350;//dp
	
	private String thumbPath = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initViewParams();
        startCamera();
//        bindService();
        RegisterBroadCast();
    }
    
//    private void bindService() {
//    	Intent intent = new Intent(CircleCameraActivity.this,CircleCameraInterface.class);
//        bindService(intent, conn, Context.BIND_AUTO_CREATE);
//	}

	private void RegisterBroadCast() {
    		IntentFilter intentFilter = new IntentFilter();
    		intentFilter.addAction(FileUtil.ACTION_SERVICE);
    		CircleCameraActivity.this.registerReceiver(reciever, intentFilter);
    		
    		Log.i(TAG, "RegisterReceiever ======================== ");
	}
    
//    private ServiceConnection conn = new ServiceConnection() {
//		
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			
//		}
//		
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			Log.i(TAG, "服务已经连接。。。=+++++++++++++++++++++++++++++++++++");
//		}
//	};
    
    private BroadcastReceiver reciever = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(FileUtil.ACTION_SERVICE)){
				if(intent != null){
					thumbPath = intent.getStringExtra("thumbPath");
					if(thumbPath != null){
						File file = new File(thumbPath);
						Uri uri = Uri.fromFile(file);
						Intent intent1 = new Intent(CircleCameraActivity.this,ImageCapterActivity.class);
						intent1.setData(uri);
						intent1.putExtra("thumbPath", thumbPath);
						startActivityForResult(intent1, TAKEPHOTO);
					}
				}
			}
		}
    };
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			switch(requestCode){
			case TAKEPHOTO:
				if(data != null){
					String thumbPath = data.getStringExtra("thumbPath");
					Intent intent = new Intent();
					intent.putExtra("thumbPath", thumbPath);
					setResult(RESULT_OK, intent);
				}
				finish();
				break;
			}
		}
		
	}

	private void startCamera() {
    	Thread openThread = new Thread(){
        	@Override
        	public void run() {
        	CircleCameraInterface.getInstance(CircleCameraActivity.this).doOpenCamera(CircleCameraActivity.this);
        	}
        };
        openThread.start();
	}

	@Override
    protected void onRestart() {
    	super.onRestart();
    	startCamera();
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(reciever);
//		unbindService(conn);
	}

	private void initViewParams() {
		maskView.widthScreen = DisplayUtil.dip2px(CircleCameraActivity.this, this.screenW);
		
		LayoutParams params = mCircleCameraSurfaceView.getLayoutParams();
		Point p = DisplayUtil.getScreenMetrics(this);
		params.width = p.x;
		params.height = p.y;
		
		Log.i(TAG, "screenwidth = "+ p.x +"screenheight = "+p.y);
		previewRate = DisplayUtil.getScreenRate(this);//默认全屏的比例预览
		mCircleCameraSurfaceView.setLayoutParams(params);
		
	}

	private void initView() {
		mCircleCameraSurfaceView = (CircleCameraSurfaceView)findViewById(R.id.id_surfaceview);
		maskView = (CircleMaskView)findViewById(R.id.id_maskView);
		btnShutter = (ImageButton)findViewById(R.id.id_btn_shutter);
		btnShutter.setOnClickListener(clickListener);
	}

	@Override
	public void cameraHasOpened() {
		SurfaceHolder holder = mCircleCameraSurfaceView.getSurfaceHolder();
		CircleCameraInterface.getInstance(CircleCameraActivity.this).doStartPreView(holder, previewRate);
		if(maskView != null){
			
		}
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.id_btn_shutter:
				if(circlePictureSize == null){
					circlePictureSize = CreateCirclePicture(
							DisplayUtil.dip2px(CircleCameraActivity.this, screenW), 
							DisplayUtil.dip2px(CircleCameraActivity.this, screenH));
				}
				CircleCameraInterface.getInstance(CircleCameraActivity.this).doTakePicture(circlePictureSize.x,circlePictureSize.y);
				 
				break;
			}
		}
	};
	
	
	private Point CreateCirclePicture(int w,int h){
		int wScreen = DisplayUtil.getScreenMetrics(this).x;
		int hScreen = DisplayUtil.getScreenMetrics(this).y;
		int wSavePicture = CircleCameraInterface.getInstance(CircleCameraActivity.this).doGetPrictureSize().y;//因为图片旋转了，所以宽高换位
		int hSavePicture = CircleCameraInterface.getInstance(CircleCameraActivity.this).doGetPrictureSize().x;
		float wRate = (float)(wSavePicture) / (float)(wScreen);
		float hRate = (float)(hSavePicture) / (float)(hScreen);
		float rate = (wRate <= hRate) ? wRate : hRate;//也可以按照最小比率计算
		
		int wRectPicture = (int)(w * wRate);
		int hRectPicture = (int)(h * hRate);
		
		return new Point(wRectPicture,hRectPicture);
	}
	
	
}
