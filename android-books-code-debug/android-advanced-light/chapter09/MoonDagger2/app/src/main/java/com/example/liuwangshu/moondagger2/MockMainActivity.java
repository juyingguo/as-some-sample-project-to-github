package com.example.liuwangshu.moondagger2;

import android.os.Bundle;

import com.example.liuwangshu.moondagger2.component.DaggerMockMainActivityComponent;
import com.example.liuwangshu.moondagger2.model.Swordsman;
import com.example.liuwangshu.moondagger2.model.Watch;

import javax.inject.Inject;

public class MockMainActivity {
    private static final String TAG="Dagger2";
    @Inject
    Watch watch;

    @Inject
    Swordsman swordsman;
    public void create() {
        DaggerMockMainActivityComponent.create().inject(this);
        watch.work();
    }
    public String swordsmanTest() {
        DaggerMockMainActivityComponent.create().inject(this);
        return swordsman.fighting();
    }
}
