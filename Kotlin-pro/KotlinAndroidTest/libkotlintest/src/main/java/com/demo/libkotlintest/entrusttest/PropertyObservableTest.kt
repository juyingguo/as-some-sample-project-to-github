package com.demo.libkotlintest.entrusttest

import kotlin.properties.Delegates

/**
 * Date:2021/8/18,10:57
 * author:jy
 */
class User {
    var name: String by Delegates.observable("初始值") {
            prop, old, new -> println("旧值：$old -> 新值：$new")
    }
}

fun main(args: Array<String>) {
    val user = User()
    user.name = "第一次赋值"
    user.name = "第二次赋值"
}