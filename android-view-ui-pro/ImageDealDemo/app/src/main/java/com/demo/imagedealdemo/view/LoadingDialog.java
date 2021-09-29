package com.demo.imagedealdemo.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.demo.imagedealdemo.R;


/**
 * Created by jy on 2016/10/18.
 */
public class LoadingDialog extends Dialog {

    private Context context;
    private TextView tvContent;

    private CircleProgress progress_circle;

    /**
     * 当前Dialog是否可以取消。default :true;
     */
    private boolean cancelable = true;

    public LoadingDialog(Context context) {
        super(context, R.style.Theme_MyDialog_Shape_White_Fillet);
        this.context = context;

        //加载布局
        setContentView(R.layout.dg_loading);
        //获取控件
        findViewById();
        //设置对话框窗体属性
        setProperty(this.context);

        //default call;
        setCancelable(this.cancelable);
    }

    /**
     *  for user to call ,set the value they want
     * @param cancelable
     */
    public void setCancelForUser(boolean cancelable) {
        this.cancelable = cancelable;
        //是否可以取消
        setCancelable(this.cancelable);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideNavigationBar();
    }
    /**
     *此时只是隐藏导航功栏 ,因为activity配置了android:theme="@android:style/Theme.NoTitleBar.Fullscreen"
     */
    private void hideNavigationBar(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void findViewById() {
        tvContent = (TextView) findViewById(R.id.tv_content);
        progress_circle = (CircleProgress) findViewById(R.id.progress_circle);
    }
    /**
     * 设置对话框内容
     *
     * @param resid
     */
    public void setContent(int resid) {
        tvContent.setText(this.context.getString(resid));
    }

    private void setOnClick() {
    }

    /**
     * 设置对话框窗体属性
     *
     * @param context 上下文
     */
    private void setProperty(Context context) {
        // setCanceledOnTouchOutside(false);// 对话框以外无法取消
        setCancelable(true);
        int h = context.getResources().getDisplayMetrics().heightPixels;
        int w = context.getResources().getDisplayMetrics().widthPixels;
        if (h < w) {
            w = h;
        }
        Window window = getWindow();// 　　　得到对话框的窗口．
        WindowManager.LayoutParams lp = window.getAttributes();
        // 中间
        // lp.x = -vWidth;// 这两句设置了对话框的位置．0为中间
        // lp.y = vheight;// 这两句设置了对话框的位置．0为中间//-(292 - 45)=-247
        // 宽度
        // lp.width = lvW;// 对话框的宽 占屏幕比例的2/3
//        lp.width = w * 2 / 5;// 对话框的宽 占屏幕比例的4/5
        lp.width = lp.WRAP_CONTENT;// 对话框的宽 占屏幕比例的2/3
        // 高
        lp.height = lp.WRAP_CONTENT;// 对话框的高包裹内容
        // 透明度
        // lp.alpha = 0.9f;// 这句设置了对话框的透明度
        // 布局相对位置
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
    }


    /**
     * 设置内容文字颜色
     * @param resid
     */
    public void setContentTextColor(int resid) {
        tvContent.setTextColor(resid);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (progress_circle != null){
            progress_circle.startAnim();
        }
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
        if (progress_circle != null){
//            progress_circle.stopAnim();
            progress_circle.reset();
        }
    }

    public void showLoadingDialog(LoadingDialog loadingDialog) {
        if (null != loadingDialog && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void dismissLoadingDialog(LoadingDialog loadingDialog) {
        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
        }
    }
}