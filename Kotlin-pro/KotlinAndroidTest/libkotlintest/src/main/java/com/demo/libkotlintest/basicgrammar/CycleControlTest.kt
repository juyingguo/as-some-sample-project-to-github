package com.demo.libkotlintest.basicgrammar

import org.junit.Test

class CycleControlTest {

    @Test
    fun testCycle(){
        val items = listOf("apple", "banana", "kiwi")
        for (item in items) {
            println(item)
        }

        for (index in items.indices) {
            println("item at $index is ${items[index]}")
        }
    }
    @Test
    fun testDoWhile(){
        println("----while 使用-----")
        var x = 5
        while (x > 0) {
            println( x--)
        }
        println("----do...while 使用-----")
        var y = 5
        do {
            println(y--)
        } while(y>0)
    }
    @Test
    fun testContinueBreak(){
        for (i in 1..10) {
            if (i==3) continue  // i 为 3 时跳过当前循环，继续下一次循环
            println(i)
            if (i>5) break   // i 为 6 时 跳出循环
        }
    }
    @Test
    fun testTagCycleBreak(){
       loop@ for (i in 1..10) {

           for (i in 1..10) {
               println(i)
               if (i>5) break@loop  // i 为 6 时 跳出循环
           }
        }
    }

    @Test
    fun testLambdaReturnFromOut(){
        val items = listOf(1, 2, 3,4,5)
        items.forEach {
            if (it == 3) return
            println(it)
        }
    }
    @Test
    fun testLambdaReturnFromLatestInnerCycle(){
        val items = listOf(1, 2, 3,4,5)
        items.forEach lit@{
            if (it == 3) return@lit
            println(it)
        }
    }
    @Test
    fun testLambdaReturnFromLatestInnerCycleWithImply(){
        val items = listOf(1, 2, 3,4,5)
        items.forEach {
            if (it == 3) return@forEach
            println(it)
        }
    }

    /**
     * 用一个匿名函数替代 lambda 表达式。 匿名函数内部的 return 语句将从该匿名函数自身返回
     */
    @Test
    fun testLambdaReturnWithAnonymousFun(){
        val items = listOf(1, 2, 3,4,5)
        items.forEach(fun(it:Int) {
            if (it == 3) return@forEach
            println(it)
        })
    }

    @Test
    fun testDownTo(){
        for (i in 4 downTo 1) print(i) // 打印结果为: "4321"
    }
    @Test
    fun testDownToWithStep(){
        for (i in 4 downTo 1 step 2) print(i) // 打印结果为: "42"
    }

    /**
     * 如果循环中不要最后一个范围区间的值可以使用 until 函数:
     */
    @Test
    fun testDownToWithUntil(){
        for (i in 1 until 10) { // i in [1, 10), 不包含 10
            println(i)
        }
    }
}