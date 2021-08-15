package com.demo.libkotlintest.basicgrammar

class TypeCheckConvert {
}
fun main(){
    typeCheck()
}
/**
 * 类型检测及自动类型转换
 * 我们可以使用 is 运算符检测一个表达式是否某类型的一个实例(类似于Java中的instanceof关键字)。
 */
fun typeCheck() {
    var str = "test";
//    var str = 123;
//    print("tpyeCheck,getStringLength(str):" + getStringLength(str))
    println("tpyeCheck,getStringLength2(str):" + getStringLength2(str))
    println("tpyeCheck,getStringLength3(str):" + getStringLength3(str))
}
fun getStringLength(obj: Any): Int? {
    if (obj is String) {
        // 做过类型判断以后，obj会被系统自动转换为String类型
        return obj.length
    }

    //在这里还有一种方法，与Java中instanceof不同，使用!is
    // if (obj !is String){
    //   // XXX
    // }

    // 这里的obj仍然是Any类型的引用
    return null
}
fun getStringLength2(obj: Any): Int? {
    if (obj !is String)
        return null
    // 在这个分支中, `obj` 的类型会被自动转换为 `String`
    return obj.length
}
fun getStringLength3(obj: Any): Int? {
    // 在 `&&` 运算符的右侧, `obj` 的类型会被自动转换为 `String`
    if (obj is String && obj.length > 0)
        return obj.length
    return null
}