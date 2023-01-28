package com.demo.libkotlintest.collectiont.listtest

import org.junit.Test
import kotlin.collections.ArrayList

class ArrayListTest {

    @Test
    fun testListRemoveCauseSkipElement() {
        val list = ArrayList<String>()
        list.add("a")
        list.add("b")
        list.add("c")
        println(list.size)
        for (i in list.indices) {
            list.removeAt(i)
        }
        println("after remove ,list size:" + list.size)
        println("after remove ,list :$list")
    }
}