package com.demo.imagedealdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.demo.imagedealdemo.util.DisplayUtils;
import com.demo.imagedealdemo.util.GlideRoundTransform;
import com.demo.imagedealdemo.util.LogUtil;
import com.demo.imagedealdemo.view.LoadingDialog;

/**
 * 
 * @author jy on 2017/9/21 <br/>
 * 1.适用于看图实物、以及网页图片资源加载显示<br/>
 * 2.onNewIntent时，如果图片相同就不需要重复加载 <br/>
 * 3.点击界面间隔小于300ms时，销毁该界面
 */
public class ShowImageActivity extends FullScreenActivity {

	private final String TAG = ShowImageActivity.class.getSimpleName();

	private ImageView imageView;
	private String imageUrl;
	private int imageResId;
	private long mTimeStamp =0;
	private LoadingDialog loadingDialog;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		LogUtil.w(TAG,"onCreate>>");

		setContentView(R.layout.activity_show_image);
		
		initViews();
		
		initData();

	}

	private void showDialog(){
		if (loadingDialog == null){
			loadingDialog = new LoadingDialog(this);
			loadingDialog.setContent(R.string.text_loading);
			loadingDialog.setCancelForUser(false);
		}
		loadingDialog.showLoadingDialog(loadingDialog);
	}

	private void initViews() {
		imageView = (ImageView) findViewById(R.id.iv_act_show_imgage);
	}

	private void initData() {
		//file:///android_asset/image/guoqing.jpg
		String path  = "file:///android_asset/image/guoqing.jpg";
		displayLocalImageWithGlide(path);
	}

    private void displayLocalImageWithGlide(String absolutePath) {
        int radius = DisplayUtils.dip2px(mContext, 10.0f);
        int width = DisplayUtils.getDisplayWidthHeight(mContext)[0];
        int height = DisplayUtils.getDisplayWidthHeight(mContext)[1];
			Glide.with(getApplicationContext())
					.load(absolutePath)
//					.transform(new GlideRoundTransform(mContext, radius))
                    .override(width,height)
//                    .centerCrop()
					.listener(requestListener)
					.into(imageView);
    }
    private void displayImageWithGlideByResId(int resId) {
        int radius = DisplayUtils.dip2px(mContext, 10.0f);
        int width = DisplayUtils.getDisplayWidthHeight(mContext)[0];
        int height = DisplayUtils.getDisplayWidthHeight(mContext)[1];
        Glide.with(getApplicationContext())
                .load(resId)
                .transform(new GlideRoundTransform(mContext, radius))
                .override(width,height)
                .centerCrop()
                .into(imageView);
    }
    /*private void displayLocalImageWithImageLoader(String absolutePath) {
        int width = DisplayUtils.getDisplayWidthHeight(mContext)[0];
        int height = DisplayUtils.getDisplayWidthHeight(mContext)[1];
        ImageSize targetImageSize = new ImageSize(width,height);
        CustomImageLoader.getImageLoader().loadImage(imageUrl,targetImageSize,CustomImageLoader.OPTIONS_LOCAL_PHOTO, listener);
    }*/

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
//		LogUtil.i(TAG, "dispatchTouchEvent>>ev.getAction() :"+ev.getAction() );
//		if (ev.getAction() == MotionEvent.ACTION_DOWN){
//			if (SystemClock.elapsedRealtime() - mTimeStamp < 300){
//				finish();
//			}
//			mTimeStamp = SystemClock.elapsedRealtime();
//			LogUtil.i(TAG, "dispatchTouchEvent>>mTimeStamp:"+mTimeStamp);
//		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {

        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            LogUtil.e(TAG,"onException>>Exception:" + e
                    + "\n model:" + model
                    + "\n isFirstResource:" + isFirstResource
            );
            if(loadingDialog != null){
                loadingDialog.dismissLoadingDialog(loadingDialog);
            }
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            LogUtil.e(TAG,"onResourceReady>>model:" + model
                    + "\n isFromMemoryCache:" + isFromMemoryCache
                    + "\n isFirstResource:" + isFirstResource
            );
            if(loadingDialog != null){
                loadingDialog.dismissLoadingDialog(loadingDialog);
            }
            return false;
        }
    };

	@Override
	protected void onResume() {
		super.onResume();
		LogUtil.w(TAG,"onResume>>");
	}

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.w(TAG,"onPause>>");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.w(TAG,"onStop>>");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
		if(loadingDialog != null){
			loadingDialog.dismissLoadingDialog(loadingDialog);
		}
		LogUtil.w(TAG,"onDestroy>>");
    }
    /*private void openShowImageActivity(){
        //2018/8/14
        Intent intent = new Intent(this, ShowImageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Constants.EXTRA_COMMON_IMAGE,"http://resource.doudoubot.cn/download/image/game/youxijiemian.jpg");
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }*/
}
