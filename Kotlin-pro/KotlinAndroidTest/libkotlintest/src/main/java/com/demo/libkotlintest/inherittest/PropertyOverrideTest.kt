package com.demo.libkotlintest.inherittest

class PropertyOverrideTest {
}
interface Foo {
    val count: Int;
    var title:String;
}

class Bar1(override val count: Int, override var title: String) : Foo

class Bar2() : Foo {
    override var count: Int = 0
    override var title: String = "";
}

fun main() {
    var bar2 = Bar2()
//    bar2.count = 100;
    println("bar2.count:" + bar2.count);
}