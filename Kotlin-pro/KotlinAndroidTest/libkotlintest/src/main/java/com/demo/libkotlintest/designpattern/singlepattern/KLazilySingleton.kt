package com.demo.libkotlintest.designpattern.singlepattern

import java.io.Serializable

class KLazilySingleton private constructor() : Serializable {
    fun doSomething() {
        println("do some thing")
    }
    companion object {
        private var mInstance: KLazilySingleton? = null
            get() {
                return field ?: KLazilySingleton()
            }

//        @JvmStatic
        @Synchronized//添加synchronized同步锁
        fun getInstance(): KLazilySingleton {
            return requireNotNull(mInstance)
        }
    }
    //防止单例对象在反序列化时重新生成对象
    private fun readResolve(): Any {
        return KLazilySingleton.getInstance()
    }
}
//在Kotlin中调用
fun main(args: Array<String>) {
    KLazilySingleton.getInstance().doSomething()
}