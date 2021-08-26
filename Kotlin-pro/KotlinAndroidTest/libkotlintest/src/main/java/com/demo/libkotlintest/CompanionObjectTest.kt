package com.demo.libkotlintest

/**
 * Date:2021/8/26,13:33
 * author:jy
 */
class CompanionObjectTest {
    companion object {
        private var INSTANCE: CompanionObjectTest? = null

        fun getInstance(): CompanionObjectTest {
            println("getInstance() enter,INSTANCE:" + INSTANCE)
            val newInstance = INSTANCE ?: CompanionObjectTest()
            INSTANCE = newInstance
            return newInstance
        }
    }
}

fun main(){
    println("main(),CompanionObjectTest.getInstance():" + CompanionObjectTest.getInstance())
    println("main(),CompanionObjectTest.getInstance():" + CompanionObjectTest.getInstance())
}