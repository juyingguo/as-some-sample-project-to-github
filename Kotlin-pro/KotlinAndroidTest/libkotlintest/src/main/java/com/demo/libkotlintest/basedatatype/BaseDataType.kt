package com.demo.libkotlintest.basedatatype

import org.junit.Test

class BaseDataType {
    @Test
    fun testArray(){
        //[1,2,3]
        val a = arrayOf(1, 2, 3)
        //[0,2,4]
        val b = Array(3, { i -> (i * 5) })

        //读取数组内容
        println(a[0])    // 输出结果：1
        println(a[2])    // 输出结果：3
        println(b[0])    // 输出结果：0
        println(b[2])    // 输出结果：2*5
    }
    @Test
    fun testIntArray (){
        val x: IntArray = intArrayOf(1, 2, 3)
        x[0] = x[1] + x[2]
        //读取数组内容
        println(x[0])    // 输出结果：
    }
    @Test
    fun stringTest() {
        val text = """
        多行字符串text
        菜鸟教程
        多行字符串
        Runoob
        """
        println(text)

        val textA = """
        多行字符串textA
        菜鸟教程
        多行字符串
        Runoob
        """.trimMargin()
        println(textA)    // 前置空格删除了

        val textB = """
        |多行字符串textB
        |菜鸟教程|
        |多行字符串
        s|Runoob
        """.trimMargin()
        println(textB)    // 前置空格删除了

        val text2 = """
        t多行字符串text2
        |菜鸟教程t
        |多行字符串
        |Runoob
        """.trimMargin("t")
        println(text2)
    }
}