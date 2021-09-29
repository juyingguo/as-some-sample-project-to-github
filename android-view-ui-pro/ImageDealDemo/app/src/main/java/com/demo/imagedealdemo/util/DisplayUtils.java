package com.demo.imagedealdemo.util;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by phc on 2017/11/16 0016.
 */

public class DisplayUtils {
    // 像 素 = dp * 屏幕密度

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素) ,再四舍五入
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕的宽高
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int[] getDisplayWidthHeight(Context context)
    {
        int[] wh = new int[2];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wh[0] = wm.getDefaultDisplay().getWidth();
        wh[1] = wm.getDefaultDisplay().getHeight();
        return wh;
    }

    private static long mLastCallTime = 0;

}
