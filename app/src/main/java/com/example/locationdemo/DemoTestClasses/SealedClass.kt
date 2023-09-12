package com.example.locationdemo.DemoTestClasses

import android.util.Log

sealed class SealedClass(){
    fun printClassName(s: String){
        Log.e("tag",s)
    }
}






/* Extension fun of sealed class
fun SealedClass.sealedExtFunction(){}*/
