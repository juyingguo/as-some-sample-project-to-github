package com.wd.room.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var sharedName: MutableLiveData<String> = MutableLiveData()
    
    init {
        sharedName.value = "anriku"
    }
}