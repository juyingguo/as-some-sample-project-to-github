package com.example.circlecamera;

import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class CircleMaskView extends ImageView {
	private static final String TAG = "CircleMaskView";
	private Paint mLinePaint;
	private Paint mAreaPaint;
	private Context mContext;

	int widthScreen , heightScreen ;//传过来为px值
	
	public CircleMaskView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
		mContext = context;
		Point p =DisplayUtil.getScreenMetrics(mContext);
		widthScreen = p.x;
		heightScreen = p.y;
	}
	
	private void initPaint(){
		mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mLinePaint.setStyle(Style.FILL_AND_STROKE);//空实心
		mLinePaint.setStrokeWidth(5f);
		mLinePaint.setAlpha(30);
		mLinePaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
		
		//绘制四周区域
		mAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mAreaPaint.setColor(Color.BLACK);
		mAreaPaint.setStyle(Style.FILL);//画笔为实心      Style.STROKE：画笔为空心
		mAreaPaint.setAlpha(120);
		mAreaPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_OVER));
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
	Log.i(TAG, "onDraw");
//		if(mCenterRect == null){
//			return;
//		}
//		//绘制四周阴影区域
//		canvas.drawRect(0, 0, widthScreen, mCenterRect.top, mAreaPaint);//top
//		canvas.drawRect(0, mCenterRect.top, mCenterRect.left - 1 , mCenterRect.bottom + 1, mAreaPaint);//left
//		canvas.drawRect(0, mCenterRect.bottom + 1, widthScreen, heightScreen, mAreaPaint); // bottom
//		canvas.drawRect(mCenterRect.right + 1, mCenterRect.top, widthScreen, mCenterRect.bottom + 1, mAreaPaint);
//		
//		//绘制拍照的透明区域
//		canvas.drawRect(mCenterRect, mLinePaint);
		canvas.drawARGB(120, 0, 0, 0);
	
		int xcenter = getWidth()/2;
		int yconter = getHeight()/2;
		canvas.drawCircle(xcenter, yconter, widthScreen/2, mLinePaint);//透明区域
		
//		canvas.drawRect(0, 0, getWidth(), getHeight(), mAreaPaint);//背景色
//		canvas.drawRect(0, 0, widthScreen, heightScreen, mAreaPaint);
//		canvas.drawCircle(xcenter, yconter, innerCircle + 1,mAreaPaint);
		super.onDraw(canvas);
		
	}
	
	
}
