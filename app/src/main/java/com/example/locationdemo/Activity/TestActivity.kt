package com.example.locationdemo.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.locationdemo.AbstractClass
import com.example.locationdemo.R
import com.example.locationdemo.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    lateinit var binding: ActivityTestBinding

    val inStr: String by lazy { "inline function operation" }
    val noInStr: String by lazy { "non - inline function operation" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvModuleAct.setOnClickListener {

            val intent = Intent(this@TestActivity,
                Class.forName("com.example.testmodule.ModuleMainAct"))
            startActivity(intent)

        }


        //Sealed class Test.
        val a = A()
        a.a()

        // Inline function call..
        inlineFun({ Log.d("tag-inline-fun", inStr) }, { Log.d("tag-no-inline-fun", noInStr) })
//        inlineFun({ Log.d("tag-inline-fun", inStr); return }, { Log.d("tag-no-inline-fun", noInStr) })
    }

    // Inline function declaration..
//    private inline fun inlineFun(funIn: (String) -> Unit, noinline funNoInt: (String) -> Unit) {
//    private inline fun inlineFun(crossinline funIn: (String) -> Unit,  funNoInt: (String) -> Unit) {
    private inline fun inlineFun(funIn: (String) -> Unit, noinline funNoInt: (String) -> Unit) {
        funNoInt(noInStr)
        funIn(inStr)
    }
}

//extend abstract class Test
class A : AbstractClass() {
    fun a() {
        val a = AbstractClass.A()
        Log.e("tag", a.cA)
    }
}