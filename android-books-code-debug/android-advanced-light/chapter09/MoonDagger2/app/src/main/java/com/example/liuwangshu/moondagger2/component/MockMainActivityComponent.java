package com.example.liuwangshu.moondagger2.component;

import com.example.liuwangshu.moondagger2.MockMainActivity;
import com.example.liuwangshu.moondagger2.module.SwordsmanModule;

import dagger.Component;

/**
 * Created by Administrator on 2016/12/20 0020.
 */
// 不使用module
//@Component
//使用module
@Component(modules = SwordsmanModule.class)
public interface MockMainActivityComponent {
    void inject(MockMainActivity activity);
}
