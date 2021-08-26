package com.demo.libkotlintest.designpattern.singlepattern
import java.io.Serializable
class KOptimizeSingleton private constructor(): Serializable {//private constructor()构造器私有化
    companion object {
        @JvmStatic
        fun getInstance(): KOptimizeSingleton {//全局访问点
            return SingletonHolder.mInstance
        }
    }

    fun doSomething() {
        println("do some thing")
    }

    private object SingletonHolder {//静态内部类
        val mInstance: KOptimizeSingleton = KOptimizeSingleton()
    }

    private fun readResolve(): Any {//防止单例对象在反序列化时重新生成对象
        return SingletonHolder.mInstance
    }
}
fun main() {
    KOptimizeSingleton.getInstance().doSomething()
}