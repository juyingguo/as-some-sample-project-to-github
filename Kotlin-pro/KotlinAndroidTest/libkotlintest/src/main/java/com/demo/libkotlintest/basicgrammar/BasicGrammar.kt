package com.demo.libkotlintest.basicgrammar

/**
 * Date:2021/7/30,11:38
 * author:jy
 */
class BasicGrammar {
}
fun main(args: Array<String>) {

    var a = 1
    // 模板中的简单名称：
    val s1 = "a is $a"

    println("s1=$s1");

    a = 2;
    // 模板中的任意表达式：
    val s2 = "${s1.replace("is", "was")}, but now is $a"

    println("s2=$s2");

    stringTest();
}

fun stringTest() {
    val text = """
    |多行字符串
    |菜鸟教程
    |多行字符串
    |Runoob
    """.trimMargin()
    println(text)    // 前置空格删除了

    val text2 = """
    t多行字符串
    |菜鸟教程t
    |多行字符串
    |Runoob
    """.trimMargin("t")
    println(text2)
}
