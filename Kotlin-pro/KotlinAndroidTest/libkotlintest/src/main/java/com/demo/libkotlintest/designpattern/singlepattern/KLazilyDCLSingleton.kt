package com.demo.libkotlintest.designpattern.singlepattern

import java.io.Serializable

class KLazilyDCLSingleton private constructor() : Serializable {//private constructor()构造器私有化

    fun doSomething() {
        println("do some thing")
    }

    private fun readResolve(): Any {//防止单例对象在反序列化时重新生成对象
        return instance
    }

    companion object {
        //通过@JvmStatic注解，使得在Java中调用instance直接是像调用静态函数一样，
        //类似KLazilyDCLSingleton.getInstance(),如果不加注解，在Java中必须这样调用: KLazilyDCLSingleton.Companion.getInstance().
        @JvmStatic
        //使用lazy属性代理，并指定LazyThreadSafetyMode为SYNCHRONIZED模式保证线程安全
        val instance: KLazilyDCLSingleton by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { KLazilyDCLSingleton() }
    }
}

//在Kotlin中调用，直接通过KLazilyDCLSingleton类名调用instance
fun main(args: Array<String>) {
    KLazilyDCLSingleton.instance.doSomething()
}
/*
//在Java中调用
public class TestMain {
    public static void main(String[] args) {
        //加了@JvmStatic注解后，可以直接KLazilyDCLSingleton.getInstance()，不会打破Java中调用习惯，和Java调用方式一样。
        KLazilyDCLSingleton.getInstance().doSomething();
        //没有加@JvmStatic注解，只能这样通过Companion调用
        KLazilyDCLSingleton.Companion.getInstance().doSomething();
    }
}*/
