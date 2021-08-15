package com.demo.libkotlintest.classandobj

class PersonGetSetTest {

    var lastName: String = "zhang"
        get() = field.toUpperCase()   // 将变量赋值后转换为大写
//        set                          //直接写set，可以这接赋值
        set(para){                   // set(para)带有参数，可以任意修改变量。修改意义不大，一般就是直接赋值即可。
            field = "$para append.";
        }

    var no: Int = 100
        get() = field                // 后端变量；对于var，写不写get都行，默认已经实现了。
        set(value) {
            if (value < 10) {       // 如果传入的值小于 10 返回该值
                field = value
            } else {
                field = -1         // 如果传入的值大于等于 10 返回 -1
            }
        }

    var heiht: Float = 145.4f
        private set

    val simple: Int = 102       // 类型为 Int ，默认实现 getter ，但必须在构造函数中初始化
        get() = field + 10;     //val get 返回值可以任意修改，但其中field指向该常量。
//        set                   //val 不可以重写setter
}

// 测试
fun main(args: Array<String>) {
    var person: PersonGetSetTest = PersonGetSetTest()

    person.lastName = "wang"

    println("lastName:${person.lastName}")

    person.no = 9
    println("no:${person.no}")

    person.no = 20
    println("no:${person.no}")

    println("heiht:${person.heiht}")

//    person.simple = 103; //常量不可
    println("simple:${person.simple}")

}