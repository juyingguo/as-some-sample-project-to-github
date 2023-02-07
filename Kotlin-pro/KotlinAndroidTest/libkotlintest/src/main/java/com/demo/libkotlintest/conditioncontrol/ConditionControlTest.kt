package com.demo.libkotlintest.conditioncontrol

import org.junit.Test

class ConditionControlTest {

    @Test
    fun test(){
        var x = 0
        when (x) {
            0, 1 -> println("x == 0 or x == 1")
            else -> println("otherwise")
        }

        when (x) {
            1 -> println("x == 1")
            2 -> println("x == 2")
            else -> { // 注意这个块
                println("x 不是 1 ，也不是 2")
            }
        }

        when (x) {
            in 0..10 -> println("x 在该区间范围内")
            else -> println("x 不在该区间范围内")
        }
    }
}