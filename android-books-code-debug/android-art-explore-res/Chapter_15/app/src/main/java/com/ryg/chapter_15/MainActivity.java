package com.ryg.chapter_15;

import com.ryg.chapter_15.R;
import com.ryg.chapter_15.manager.TestManager;
import com.ryg.chapter_15.manager.TestManager.OnDataArrivedListener;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;

public class MainActivity extends Activity implements OnDataArrivedListener {
    private static final String TAG = "MainActivity";

    private static Context sContext;
    private static View sView;

    private Button mButton;

//    private WeakReference<Handler> mHandler = new WeakReference<Handler>(new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    });
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sContext = this;
//        sView = new View(this);
        mButton = (Button) findViewById(R.id.button1);
//        Log.d(TAG,"onCreate,TestManager.getInstance():" + TestManager.getInstance());
//        Log.d(TAG,"onCreate,TestManager.getInstance():" + TestManager.getInstance());
//        TestManager.getInstance().registerListener(this);
//        ObjectAnimator animator = ObjectAnimator.ofFloat(mButton, "rotation",
//                0, 360).setDuration(2000);
//        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.start();
//        //animator.cancel();

//        SystemClock.sleep(30 * 1000);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SystemClock.sleep(30 * 1000);
//            }
//        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                testANR();
//            }
//        }).start();
//        SystemClock.sleep(10);
        initView();
    }

    private synchronized void testANR() {
        Log.d(TAG,"testANR");
        SystemClock.sleep(30 * 1000);
    }

    private synchronized void initView() {
        Log.d(TAG,"initView");

    }

    @Override
    public void onDataArrived(Object data) {
        Log.i(TAG, data.toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
