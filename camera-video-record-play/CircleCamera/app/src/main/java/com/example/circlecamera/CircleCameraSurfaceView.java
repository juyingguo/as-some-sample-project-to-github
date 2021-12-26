package com.example.circlecamera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CircleCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	private static final String TAG = "CircleCameraSurfaceView";
	
	CircleCameraInterface mCircleCameraInterface;
	Context mContext;
	SurfaceHolder mSurfaceHolder;

	public CircleCameraSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mSurfaceHolder = getHolder();
		mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent ��͸��  transparent ͸��
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.i(TAG, "surfaceChanged..");
		CircleCameraInterface.getInstance(mContext).doStartPreView(mSurfaceHolder, 1.333f);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		CircleCameraInterface.getInstance(mContext).doStopCamera();
	}
	

	public SurfaceHolder getSurfaceHolder(){
		return mSurfaceHolder;
	}

}
