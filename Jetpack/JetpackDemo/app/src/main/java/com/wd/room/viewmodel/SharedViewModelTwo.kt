package com.wd.room.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Date:2021/9/9,15:48
 * author:jy
 * <p>
 *     当我们的ViewModel需要进行构造器需要穿参数的时候，需要借助于ViewModelProvider的Fatory来进行构造。
 */
@Suppress("UNCHECKED_CAST")
class SharedViewModelTwo(sharedName: String) : ViewModel() {
    var sharedName: MutableLiveData<String> = MutableLiveData()

    init {
        this.sharedName.value = sharedName
    }

    class SharedViewModelTwoFactory(private val sharedName: String) :
            ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SharedViewModelTwo(sharedName) as T
        }
    }
}