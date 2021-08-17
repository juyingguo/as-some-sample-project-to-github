package com.demo.libkotlintest.generictest

/**
 * Date:2021/8/17,14:46
 * author:jy
 */
class GenericMethodTest {

    fun <T> doPrintln(content: T) {

        when (content) {
            is Int -> println("整型数字为 $content")
            is String -> println("字符串转换为大写：${content.toUpperCase()}")
//            else -> println("T 不是整型，也不是字符串")
            is Boolean -> println("bool为 $content")
        }
    }
}
fun main(args: Array<String>) {
    val age = 23
    val name = "runoob"
    val bool = true
    val gm = GenericMethodTest();
    gm.doPrintln(age)    // 整型
    gm.doPrintln(name)   // 字符串
    gm.doPrintln(bool)   // 布尔型
}
