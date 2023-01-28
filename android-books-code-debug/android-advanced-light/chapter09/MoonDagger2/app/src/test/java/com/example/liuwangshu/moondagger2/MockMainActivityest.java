package com.example.liuwangshu.moondagger2;

import android.util.Log;

import com.example.liuwangshu.moondagger2.model.Watch;

import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/12/20 0020.
 */
public class MockMainActivityest {
    @Test
    public void testMainActivityComponent(){
        new MockMainActivity().create();
    }
    @Test
    public void testMainActivityComponentForSwordsman(){
        Assert.assertEquals("欲为大树，莫与草争" ,new MockMainActivity().swordsmanTest());
    }
}