package com.demo.libkotlintest.classandobj

/**
 * val simple: Int?       // 类型为 Int ，默认实现 getter ，但必须在构造函数中初始化
 */
class  Runoob constructor(name: String,val maxLength:Int=10) {  // 类名为 Runoob
    // 大括号内是类体构成
    var url: String = "http://www.runoob.com"
    var country: String = "CN"
    var siteName = name

//    var allByDefault: Int?// 错误: 需要一个初始化语句, 默认实现了 getter 和 setter 方法。【写在类中错误；非类中是可以的】


    init {
        println("初始化网站名: ${name}")
        println("init,maxLength: ${maxLength}")
    }

    fun printTest() {
        println("我是类的函数")
    }
}

fun main(args: Array<String>) {
    val runoob = Runoob("菜鸟教程")
    println(runoob.siteName)
    println(runoob.url)
    println(runoob.country)
    runoob.printTest()
}