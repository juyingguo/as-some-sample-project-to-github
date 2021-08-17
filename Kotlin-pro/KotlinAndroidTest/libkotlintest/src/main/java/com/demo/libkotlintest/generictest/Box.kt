package com.demo.libkotlintest.generictest

class Box<T>(t : T) {
    var value = t
}
fun main(args: Array<String>) {
    var boxInt = Box<Int>(10)
    var boxString = Box<String>("Runoob")
    var boxDouble = Box<Double>(3.1415926)

    println(boxInt.value)
    println(boxString.value)
    println(boxDouble.value)

    boxDouble.value = 1.102
    println(boxDouble.value)
}