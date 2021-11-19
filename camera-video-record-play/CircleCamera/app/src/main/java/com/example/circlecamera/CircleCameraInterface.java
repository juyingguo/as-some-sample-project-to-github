package com.example.circlecamera;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;

@SuppressWarnings("deprecation")
public class CircleCameraInterface extends Service {
	
	private static final String TAG = "CircleCameraInterface";
	private Camera mCamera;
	private Camera.Parameters mParams;
	private boolean isPreviewing = false ;
	private float mPreviewRate = -1f;
	private  static CircleCameraInterface mCircleCameraInterface;
	
	static int DST_RECT_WIDTH;
	static int DST_RECT_HEIGHT;
	
	private Context mContext;
	
	public interface CamOpenOverCallback{
		public void cameraHasOpened();
	}

	public CircleCameraInterface(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	public static synchronized CircleCameraInterface getInstance (Context mContext){
		if(mCircleCameraInterface == null){
			mCircleCameraInterface = new CircleCameraInterface(mContext);
		}
		return mCircleCameraInterface;
	}
	
	/**打开Camera
	 *@param  callback
	 */
	public void doOpenCamera(CamOpenOverCallback callback){
		Log.i(TAG, "Camera open ...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mCamera = Camera.open();
		Log.i(TAG, "Camera open over..");
		if(callback != null){
			callback.cameraHasOpened();
		}
	}
	
	/**使用SurfaceView 开启预览
	 * @param holder
	 * @param previewRate
	 */
	
	public void doStartPreView(SurfaceHolder holder , float previewRate){
		Log.i(TAG, "doStartPreview");
		if(isPreviewing){
			mCamera.stopPreview();
			return;
		}
		if(mCamera != null){
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
			}
			initCamera(previewRate);
		}
	}
	
	/**
	 * 使用TextureView 预览 Camera
	 * @param previewRate
	 */
	
	public void doStartPreview(SurfaceTexture surface , float previewRate){
		Log.i(TAG, "doStartPreview ..");
		if(isPreviewing){
			mCamera.stopPreview();
			return;
		}
		if(mCamera != null){
		try {
			mCamera.setPreviewTexture(surface);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initCamera(previewRate);
		}
	}
	
	/**
	 * 停止预览，释放Camera
	 * @param 
	 */
	public void doStopCamera(){
		if(mCamera != null){
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			isPreviewing = false;
			mPreviewRate = -1f;
			mCamera.release();
			mCamera = null;
		}
	}
	
	/**
	 * 拍照
	 */
	public void doTakePicture(){
		if(isPreviewing && (mCamera != null)){
			mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
		}
	}
	
	public void doTakePicture(int w,int h){
		if(isPreviewing && (mCamera != null)){
			Log.i(TAG, "矩形拍照尺寸：w =  " + w );
			Log.i(TAG, "矩形拍照尺寸：h =  " + h );
			DST_RECT_WIDTH = w;
			DST_RECT_HEIGHT = h;
			mCamera.takePicture(mShutterCallback, null, mRectJpegPictureCallback);
		}
	}
	
	public Point doGetPrictureSize(){
		Size s = mCamera.getParameters().getPictureSize();
		return new Point(s.width,s.height);
	}
	
	private void initCamera(float previewRate) {
		if(mCamera != null){
			mParams = mCamera.getParameters();
			mParams.setPictureFormat(PixelFormat.JPEG);//设置拍照后的存储的图片格式
			
			//设置PreviewSize 和 PictureSize
			Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
					mParams.getSupportedPictureSizes(), previewRate, 800);
			mParams.setPictureSize(pictureSize.width, pictureSize.height);
			
			Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
					mParams.getSupportedPreviewSizes(), previewRate, 800);
			mParams.setPreviewSize(previewSize.width, previewSize.height);
			
			mCamera.setDisplayOrientation(90);
			
			List<String> focusModes = mParams.getSupportedFocusModes();
			if(focusModes.contains("continuous-video")){
				mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}
			mCamera.setParameters(mParams);
			mCamera.startPreview(); // 开启预览
			
			isPreviewing = true ;
			mPreviewRate = previewRate;
			
			mParams = mCamera.getParameters();//重新get一次
			Log.i(TAG, "最终设置:PreviewSize--With = " + mParams.getPreviewSize().width
					+ "Height = " + mParams.getPreviewSize().height);
			Log.i(TAG, "最终设置:PictureSize--With = " + mParams.getPictureSize().width
					+ "Height = " + mParams.getPictureSize().height);
		}
	}
	
	//为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量
		ShutterCallback mShutterCallback = new ShutterCallback() {
			//快门按下的回调，在这里我们可以设置类似播放咔嚓之类的操作，默认的就是咔嚓
			@Override
			public void onShutter() {
			}
		};
		
		PictureCallback mRawCallback = new PictureCallback() {
			//拍摄的未压缩原数据的回调，可以为null
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				
			}
		};
		
		/**
		 * 常规的回调
		 */
		PictureCallback mJpegPictureCallback = new PictureCallback() {
			//对jpeg图像数据的回调，最重要的一个回调
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				Bitmap b = null;
				if(data != null){
					b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成类图
					mCamera.stopPreview();
					isPreviewing = false;
				}
				//保存图片到sdcard
				if(b != null){
					//设置FOCUS_MODE_CONTINUOUS_VIDEO 之后，myParam.set("rotation",90);失效
					//图片不能旋转，在这里旋转
					Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
					FileUtil.saveBitmap(rotaBitmap);
				}
				//再次进入预览
				mCamera.startPreview();
				isPreviewing = true ;
				
			}
		};

		/**
		 * 拍摄指定区域的circle
		 */
		PictureCallback mRectJpegPictureCallback = new PictureCallback() {
			//对jpeg 图像数据的回调，最重要的一个回调
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				Bitmap b = null;
				if(data != null){
					b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成类图
					mCamera.stopPreview();
					isPreviewing = false;
				}
				//保存图片到sdcard
				if(b != null){
					//设置FOCUS_MODE_CONTINUOUS_VIDEO 之后，myParam.set("rotation",90);失效
					//图片不能旋转，在这里旋转
					Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
					
					int x = rotaBitmap.getWidth()/2 - DST_RECT_WIDTH/2;
					int y = rotaBitmap.getHeight()/2 - DST_RECT_HEIGHT/2;
					Log.i(TAG, " rotaBitmap.getWidth() = "+  rotaBitmap.getWidth()); // 1080
					Log.i(TAG, "rotaBitmap.getHeight() = " + rotaBitmap.getHeight()); //1920
					Bitmap rectBitmap = Bitmap.createBitmap(rotaBitmap, x, y, DST_RECT_WIDTH, DST_RECT_HEIGHT);//得到它的正方型 or 矩形的图片
					//source:要从中截图的原始位图   int x:起始x坐标    int y:起始y坐标    int width:要截图的宽度      int height:要截图的高度
					Log.i(TAG, "DST_RECT_WIDTH " + DST_RECT_WIDTH + DST_RECT_HEIGHT +"yyyyyyyyyyyyyyyyyyyyyyy"); 
					
					Bitmap circle = getCircleBitmap(rectBitmap);
					
					FileUtil.saveBitmap(circle);
					if(rotaBitmap.isRecycled()){
						rotaBitmap.recycle();
						rotaBitmap = null;
					}
					if(circle.isRecycled()){ 
						circle.recycle();
						circle = null;
					}
					
					String thumbPath = FileUtil.getJpegName();
					File file = new File(thumbPath);
					Uri uri = Uri.fromFile(file);
					Log.d(TAG, "==============================================================="+thumbPath);
					if(thumbPath != null){
						Intent intent = new Intent();
						intent.putExtra("thumbPath", thumbPath);
						intent.setAction(FileUtil.ACTION_SERVICE);
						mContext.sendBroadcast(intent);
					}
				}
				//再次进入预览
				mCamera.startPreview();
				isPreviewing = true;
				if(!b.isRecycled()){
					b.recycle();
					b = null;
				}
			}
		};
	
		public static Bitmap getCircleBitmap(Bitmap bitmap){
			Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//			Bitmap circleBitmap = Bitmap.createBitmap(DST_RECT_WIDTH, DST_RECT_HEIGHT, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(circleBitmap);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//			final Rect rect = new Rect(0, 0, DST_RECT_WIDTH, DST_RECT_HEIGHT);
			
			Log.i(TAG, "矩形的宽度为 = "+ bitmap.getWidth() +"pppppppppppppp");
			Log.i(TAG, "矩形的高度为 = "+ bitmap.getHeight() +"zzzzzzzzzzzzzzzzzzzz");
			
			paint.setAntiAlias(true); //消除锯齿
			paint.setFilterBitmap(true); // 对bitmap进行滤波处理
			paint.setDither(true); //设置防抖动，图像不清晰
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			
			canvas.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getHeight()/2, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return circleBitmap;
		}
		
		
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
