package com.demo.libkotlintest.basicgrammar

import org.junit.Test

/**
 * Date:2021/7/30,11:38
 * author:jy
 */
class BasicGrammar {

    public fun sum(a: Int, b: Int): Int = a + b   // public 方法则必须明确写出返回类型
    @Test
    fun sumTest() {
        print(sum(50,60))
    }
    private fun printSum(a: Int, b: Int): Unit {
        print(a + b)
    }
    @Test
    fun printSumTest() {
        printSum(10,90)
    }

    fun vars(vararg v:Int){
        for(vt in v){
            print(vt)
        }
    }
    @Test
    fun varsTest(){
        vars(9,2,3,4,5)  // 输出12345
    }
    @Test
    fun sumLambdaTest(){
        val sumLambda: (Int, Int) -> Int = {x,y -> x+y}
        println(sumLambda(1,2))  // 输出 3
    }
    @Test
    fun stringTemplateTest(){
        var a = 1
        // 模板中的简单名称：
        val s1 = "a is $a"

        println("s1=$s1");

        a = 2;
        // 模板中的任意表达式：
        val s2 = "${s1.replace("is", "was")}, but now is $a"

        println("s2=$s2");
    }
}
//fun main(args: Array<String>) {

//    stringTest();
//}

