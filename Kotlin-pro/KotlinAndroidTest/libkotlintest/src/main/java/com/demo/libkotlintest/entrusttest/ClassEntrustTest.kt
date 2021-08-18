package com.demo.libkotlintest.entrusttest

/**
 * Date:2021/8/18,9:10
 * author:jy
 */
// 创建接口
interface Base {
    fun print()
}

// 实现此接口的被委托的类
class BaseImpl(val x: Int) : Base {
    override fun print() { print(x) }
}

// 通过关键字 by 建立委托类
class Derived(c: Base) : Base by c

fun main(args: Array<String>) {
    val b = BaseImpl(10)
    Derived(b).print() // 输出 10
    b.print()
}