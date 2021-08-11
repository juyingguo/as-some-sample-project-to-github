package com.demo.libkotlintest.designpattern.singlepattern

import java.io.Serializable

object KSingleton : Serializable {//实现Serializable序列化接口，通过私有、被实例化的readResolve方法控制反序列化
    fun doSomething() {
        println("do some thing")
    }

    private fun readResolve(): Any {//防止单例对象在反序列化时重新生成对象
        return KSingleton//由于反序列化时会调用readResolve这个钩子方法，只需要把当前的KSingleton对象返回而不是去创建一个新的对象
    }
}

//在Kotlin中使用KSingleton
fun main(args: Array<String>) {
    KSingleton.doSomething()//像调用静态方法一样，调用单例类中的方法
}