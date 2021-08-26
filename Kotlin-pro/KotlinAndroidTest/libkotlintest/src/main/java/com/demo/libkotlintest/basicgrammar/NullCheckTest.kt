package com.demo.libkotlintest.basicgrammar

class NullCheckTest {
}

/**
 * 当一个引用可能为 null 值时, 对应的类型声明必须明确地标记为可为 null。
 * 当 str 中的字符串内容不是一个整数时, 返回 null:
 */
fun parseInt(str: String): Int? {
    return str.toInt();
}
fun main(args: Array<String>) {
    arrayTest()
    nullCheck()
    nullCheck2()
}

fun arrayTest() {
    /**字符串String数组*/
    val args: Array<String> = arrayOf("2","10")
   if (args.size < 2) {
        print("Two integers expected")
        return
    }
    val x = parseInt(args[0])
    val y = parseInt(args[1])
    // 直接使用 `x * y` 会导致错误, 因为它们可能为 null.
    if (x != null && y != null) {
        // 在进行过 null 值检查之后, x 和 y 的类型会被自动转换为非 null 变量
        println("arrayTest,x * y=" + x * y)
    }
}

fun nullCheck(){
    //类型后面加?表示可为空
//    var age: String? = "23"

    var age: String?//如果写在类中就错误了，需要初始化。如果写在非类中，是可以的。
    age = "123";

//    val age: String?
//    age = "123";

//    var age: String? = null
    //抛出空指针异常
    val ages = age!!.toInt()

    println("nullCheck,ages=$ages")
}
fun nullCheck2(){
    //类型后面加?表示可为空
//    var age: String? = "23"

    var age: String? = null//如果写在类中就错误了，需要初始化。如果写在非类中，是可以的。
    val s = age?:"123";
    println("nullCheck2,age=$age")
    println("nullCheck2,s=$s")
}