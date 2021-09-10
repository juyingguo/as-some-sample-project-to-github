package com.wd.room.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wd.room.MainActivity;
import com.wd.room.R;
import com.wd.room.databinding.ActivityLeftRightBinding;

public class LeftRightActivity extends BaseActivity<ActivityLeftRightBinding> {
    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_left_right;
    }


    public void toClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void toClickStartRotationTestActivity(View view) {
        startActivity(new Intent(this, RotationTestActivity.class));
    }

    public void toClickStartViewModelActivity(View view) {
        startActivity(new Intent(this, ViewModelActivity.class));
    }
}
