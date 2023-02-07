package com.demo.libkotlintest.inherittest
open class PersonSubClassHasConstructor(var name : String, var age : Int){// 基类

}

class StudentSubClassHasConstructor(name : String, age : Int, var no : String, var score : Int) : PersonSubClassHasConstructor(name, age) {
}

// 测试
fun main(args: Array<String>) {
    val s =  StudentSubClassHasConstructor("Runoob", 18, "S12346", 89)
    println("学生名： ${s.name}")
    println("年龄： ${s.age}")
    println("学生号： ${s.no}")
    println("成绩： ${s.score}")
}