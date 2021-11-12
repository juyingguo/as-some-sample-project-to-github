package com.ibotn.wifitransfer.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ibotn.wifitransfer.R;

public class ToastUtils {
    private static final String TAG = ToastUtils.class.getSimpleName();
//    public static Toast mToast;
//    private static Toast mCustomToast;
//    private static TextView tvToast;
    /**
     * 显示的吐司
     * @param context
     * @param msg
     */
   /* public static void showToast(final Context context, final String msg){
        if (mToast == null) {
            mToast = Toast.makeText(IbotnApplication.getInstance(), msg + "", Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER,0,0);
        }else {
            mToast.setText("" + msg);
        }
        mToast.show();
    }*/

    /**
     * 显示的吐司
     * @param context
     * @param resId
     */
    /*public static void showToast(final Context context, final int resId){
        if (mToast == null) {
            mToast = Toast.makeText(IbotnApplication.getInstance(), resId, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        }else {
            mToast.setText(resId);
        }
        mToast.show();
    }*/
    /** 需要仅仅是debug = true 时显示的toast */
/*    public static void showForDebug(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(IbotnApplication.getInstance(), "" + msg, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER,0,0);
        }
        if (Constant.Config.DEBUG)
        {
            mToast.setText("" + msg);
            mToast.show();
        }
    }*/

    /**
     * 自定义toast
     */
    public static void showCustomToast(Context context, String msg) {
        if (context == null){
            LogUtil.e(TAG,"showCustomToast context is null");
            return;
        }
            Toast mCustomToast = new Toast(context.getApplicationContext());
            TextView tvToast = (TextView) View.inflate(context, R.layout.layout_custom_toast_newui, null);
            tvToast.setText(msg);
//            tv_toast_content = (TextView)view.findViewById(R.id.tv_toast_content);
            mCustomToast.setView(tvToast);
            mCustomToast.setDuration(Toast.LENGTH_LONG);
            DisplayMetrics outMetrics = new DisplayMetrics();
            int offsetY = 0;
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                Display display = wm.getDefaultDisplay();
                if (display != null) {
                    display.getMetrics(outMetrics);
                    offsetY = outMetrics.heightPixels / 4;
                }
            }
            mCustomToast.setGravity(Gravity.CENTER, 0, offsetY);
        mCustomToast.show();
    }

    //////////////////
    private static Toast mCustomStaticToast;
    private static TextView tvStaticToast ;
    /**
     * 自定义static toast
     */
    public static void showCustomStaticToast(Context context, String msg) {
        if (context == null){
            LogUtil.e(TAG,"showCustomToast context is null");
            return;
        }
        if (mCustomStaticToast == null || tvStaticToast == null){
            mCustomStaticToast = new Toast(context.getApplicationContext());
            tvStaticToast = (TextView) View.inflate(context, R.layout.layout_custom_toast_newui, null);
            tvStaticToast.setText(msg);
            mCustomStaticToast.setView(tvStaticToast);
            mCustomStaticToast.setDuration(Toast.LENGTH_LONG);
            DisplayMetrics outMetrics = new DisplayMetrics();
            int offsetY = 0;
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                Display display = wm.getDefaultDisplay();
                if (display != null) {
                    display.getMetrics(outMetrics);
                    offsetY = outMetrics.heightPixels / 4;
                }
            }
            mCustomStaticToast.setGravity(Gravity.CENTER, 0, offsetY);
        }
        tvStaticToast.setText(msg);
        mCustomStaticToast.show();
    }

    /* */

    /**
     * 自定义toast
     *//*
    public static void showCustomToast(String msg) {
        if (mCustomToast == null) {
            mCustomToast = new Toast(IbotnApplication.getInstance());
            View view = View.inflate(IbotnApplication.getInstance(), R.layout.layout_custom_toast,null);
            tv_toast_content = (TextView)view.findViewById(R.id.tv_toast_content);
            mCustomToast.setView(view);
            mCustomToast.setDuration(Toast.LENGTH_LONG);
            mCustomToast.setGravity(Gravity.CENTER,0,0);
        }
        if (tv_toast_content != null){
            tv_toast_content.setText(msg);
        }
        mCustomToast.show();
    }
*/
    /*public static void showAlways(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(IbotnApplication.getInstance(), "", Toast.LENGTH_LONG);
        }
        mToast.setText("" + msg);
        mToast.show();
    }
*/
    /**
     * always show toast
     */
    /*public static void showAlways(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(IbotnApplication.getInstance(), "", Toast.LENGTH_LONG);
        }
        mToast.setText("" + IbotnApplication.getInstance().getString(resId));
        mToast.show();
    }*/
}
