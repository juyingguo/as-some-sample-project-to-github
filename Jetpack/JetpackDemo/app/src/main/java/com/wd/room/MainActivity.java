package com.wd.room;

import android.os.Bundle;

import com.wd.room.activity.BaseActivity;
import com.wd.room.databinding.ActivityMainBinding;
import com.wd.room.lifecycle.MainLifeCycle;


public class MainActivity extends BaseActivity<ActivityMainBinding> {
    @Override
    protected void initView(Bundle savedInstanceState) {
        getLifecycle().addObserver(new MainLifeCycle(binding,getBaseContext()));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}
