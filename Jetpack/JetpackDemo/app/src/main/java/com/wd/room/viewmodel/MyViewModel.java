package com.wd.room.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {

    public int num;

    private MutableLiveData<Integer> currentSecond;

    public MutableLiveData<Integer> getCurrentSecond(){
        if(currentSecond == null){
            currentSecond = new MutableLiveData<>();
            currentSecond.setValue(0);
        }
        return currentSecond;
    }
}