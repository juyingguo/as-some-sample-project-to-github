package com.demo.libkotlintest.expandtest

/**
 * Date:2021/8/17,11:07
 * author:jy
 */
class CompanionObjectExpand {
}
class MyClass {
    companion object { }  // 将被称为 "Companion"
}

/**
 * 有伴生对象后，就不能定义类的拓展函数了。
 */
fun MyClass.foo() {
    println("扩展函数")
}
fun MyClass.Companion.foo() {
    println("伴随对象的扩展函数")
}

val MyClass.Companion.no: Int
    get() = 10

fun main(args: Array<String>) {
    println("no:${MyClass.no}")
    MyClass.foo()
}