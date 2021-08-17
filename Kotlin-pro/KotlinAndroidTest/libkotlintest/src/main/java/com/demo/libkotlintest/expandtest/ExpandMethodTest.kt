package com.demo.libkotlintest.expandtest

/**
 * Date:2021/8/17,10:28
 * author:jy
 */
class ExpandMethodTest {
}
class User(var name:String)

/**扩展函数**/
fun User.Print(){
    print("用户名 $name")
}

fun main(arg:Array<String>){
    var user = User("Runoob")
    user.Print()
}