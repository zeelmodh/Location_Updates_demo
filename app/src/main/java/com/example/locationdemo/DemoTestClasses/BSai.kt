package com.example.locationdemo.DemoTestClasses

import com.example.locationdemo.AbstractClass

sealed class BSai :SealedClass(){

    class InnerBSai() : BSai() {

    }
}

class BSaiAbstract() : AbstractClass() {}
