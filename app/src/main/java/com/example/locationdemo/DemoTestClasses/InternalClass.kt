package com.example.locationdemo.DemoTestClasses

import android.util.Log

internal open class InternalClass {
    protected val a = "a"
    internal val b = "b"
    init {
        Log.d("tag", "internal")
    }

//    protected class InternalInnerClass: InternalClass(){}
    internal class InternalInnerClass: InternalClass(){}

}

internal class E : InternalClass() {
    lateinit var strParam: String

    tailrec fun eInternalFun(str: String) {
//        eInternalFun(str) //this is not tail recursive call, bcz not function call not at last expression.
        if (str.length > 22) {
            Log.e("tag-internal-fun", "Length is: ${str.length}")

//             higher order fun
            strParam = str
            highOrder(::sFun)

        } else eInternalFun(str)
    }

    private fun highOrder(sFunction: (str: String) -> Unit) {
        sFunction(strParam)
    }

    fun sFun(str: String) {
        if ("Internal" in str) {
            val strUpper = str.replace(str,  "operations using Higher order function".uppercase())
            Log.d("tag-higher-order-fun", strUpper)
        }
    }
}