package com.demo.libkotlintest.expandtest

/**
 * Date:2021/8/17,10:32
 * author:jy
 * <p>1.扩展函数是静态解析的
 * 扩展函数是静态解析的，并不是接收者类型的虚拟成员，在调用扩展函数时，具体被调用的的是哪一个函数，由调用函数的的对象表达式来决定的，而不是动态的类型决定的:
 * </p>
 * <br/>1.增加扩展属性
 */
class ExpandMethodStaticParse {
}
open class C

class D: C()

fun C.foo() = "c"   // 扩展函数 foo

fun D.foo() = "d"   // 扩展函数 foo

fun printFoo(c: C) {
    println(c.foo())  // 类型是 C 类
}
val <T> List<T>.lastIndex: Int
    get() = size - 1
fun main(arg:Array<String>){
    printFoo(D())
    printFoo(C())

    expandPropertyTest();
}

/**
 * 扩展属性
 */
fun expandPropertyTest() {
    val list: MutableList<String> = mutableListOf();
    list.add("a")
    list.add("b")
    list.add("c")
    list.lastIndex
    println("expandPropertyTest(),list.lastIndex:" + list.lastIndex);
}
