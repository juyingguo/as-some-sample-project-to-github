package com.demo.libkotlintest.basicgrammar

/**
 * Date:2021/8/18,16:10
 * author:jy
 */
class LetTest {
}
val num:String? = "test"
fun main() {
    num?.let {
        println("num=" + num)
    }
}