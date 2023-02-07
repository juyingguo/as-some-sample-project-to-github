package com.demo.libkotlintest.inherittest
/**在基类中，使用fun声明函数时，此函数默认为final修饰，不能被子类重写。如果允许子类重写该函数，那么就要手动添加 open 修饰它,
子类重写方法使用 override 关键词**/

/**用户基类**/
open class PersonOverride{
    open fun study(){       // 允许子类重写
        println("我毕业了")
    }
}

/**子类继承 Person 类**/
class StudentOverride : PersonOverride() {

    override fun study(){    // 重写方法
        println("我在读大学")
    }
    /*override fun study() {
        super.study()
    }*/
}

fun main(args: Array<String>) {
    val s =  StudentOverride()
    s.study();

}