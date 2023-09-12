package com.example.testmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.testmodule.databinding.ModulemainactivityBinding

open class ModuleMainAct : AppCompatActivity() {
    private lateinit var binding: ModulemainactivityBinding

    //Primary Constructor test..
    private val testConstructorP: TestConstructor by lazy {
        TestConstructor()
    }
    //Secondary Constructor test..
    private val testConstructorS: TestConstructor by lazy {
        TestConstructor("Secondary constructor initiate")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ModulemainactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        testConstructorP
        testConstructorS
    }
}

internal open class InternalModuleClass() {
    protected val a = "a"
    internal val b = "b"

    init {
        Log.d("tag", "internal")
    }

    //    protected class InternalInnerClass: InternalClass(){}
    internal class InternalInnerClass : InternalModuleClass() {}
}

internal class OuterInternalClass : InternalModuleClass() {}

/* Another module's inner class can not visible in other module
internal class OuterInternalClass2: InternalClass(){}*/

/* Another module's sealed class can not visible in other module..
class SubSealedClass: SealedClass(){}*/


/* Extension fun of internal class
internal fun InternalModuleClass.internalExtFunction(){}*/


//Primary and secondary Constructor declaration
private class TestConstructor() {
    private val primaryA: String = "primary constructor's property"

//    constructor(){
//        Log.d("tag-primary-constructor", primaryA)
//    }

    init {
        Log.d("tag-primary-constructor", primaryA)
    }

    constructor(secondaryA: String):this() {
        Log.d("tag-secondary-constructor", secondaryA)
    }
}