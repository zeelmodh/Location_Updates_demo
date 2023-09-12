package com.example.locationdemo

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import com.example.locationdemo.Activity.TestActivity
import com.example.locationdemo.DemoTestClasses.BSai
import com.example.locationdemo.DemoTestClasses.E
import com.example.locationdemo.DemoTestClasses.InternalClass

class TestDemo() : AppCompatActivity() {
    private var a: String? = null
    private var book: Book? = null

    private var thisClass: TestDemo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_demo)

        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val linearLayout = findViewById<LinearLayout>(R.id.baseLin)

        val button = Button(this@TestDemo)
        button.setBackgroundColor(Color.TRANSPARENT)
        button.gravity = Gravity.CENTER
        button.layoutParams = layoutParams
        button.text = "Click to go Ahead"
        linearLayout.addView(button)

        val intent = Intent(this@TestDemo, TestActivity::class.java)
        button.setOnClickListener { startActivity(intent) }

        //Extension function
        thisClass = TestDemo()

        book = Book("name-01", "title-01")



        val bookList = listOf<Book>(Book("name-01", "title-01"),Book("name-02", "title-02")
            ,Book("name-03", "title-03"))
        val bookList2 = listOf<Book>(Book("name-01", "title-01"),Book("name-02", "title-02")
            ,Book("name-03", "title-03"))
//        val copyList = bookList.associateBy(Book::name)
        val th = listOf(bookList, bookList2)
        Log.e("list",th.flatMap { it }.toString())
        val fr = th.flatMap { it }
        Log.e("list", fr.groupBy(Book::name).toString())



        /*Smart cast not worked here....
        val st: Any = 2
        //for st's string value it will work
        // val st: Any = "2"
        var ts: String? = null
        ts = st as? String
        Log.d("tag-smart-cast", ts.toString())

        val cast = safeCastToString(3L)
        Log.d("tag-smart-cast", cast.toString())*/

        // Smart-cast worked here..
        Log.e("tag-smart-cast-ext", extConvertType(15.6, "", 0, 0))


        /* let
        a.let {
            printNonNull(a)
            a = "aaaaa"
        }*/

        /* run
        a.run {
            printNonNull(a, a)
            a = "aaaaa"
        }

        a?.run {
            printNonNull(a, a)
            a = "aaaaa"
        }*/

         /*apply
        book.apply {
            book = Book("name-02", "title-02")
        }*/

        /* with
        val b = with(book){
            printNonNull(book?.name, book?.title)
            book?.name= "name-03"
            book?.title= "title-03"
            val b = "bbbb"
            b
        }*/

        /* also
        book.also {
            printNonNull(book!!.name, book!!.title)
            it?.name = "name-04"
        }*/

        printNonNull(book!!.name, book!!.title)

//        Abstract class..
        val eAbt = B.C()
        eAbt.printClassName("Class InternalClass")


//        Sealed class..Object
        val eSealed = BSai.InnerBSai()
        eSealed.printClassName("Sealed class operation")

//        Internal class..Object
        val eInternal = E()
        eInternal.eInternalFun("Internal class operation")

        /* Local function call
        abc(10)*/
    }

    /* Local function ("direct nested functions")
    fun abc(any: Any){
       fun efg(any: Any, str: String){
           val anyStr = any.toString()
           Log.d("tag-local-functions",("$str : $anyStr"))
       }
       efg(any, "Amt")
   }*/


    /*Function for Smart cast not working..
    fun safeCastToString(obj: Any): String? = obj as? String*/

    private fun printNonNull(str: String?, str2: String?) {
        str?.let {
            Log.d("tag", it.uppercase())
        }
        str2?.let {
            Log.d("tag", it.uppercase())
        }
        Log.d("tag", book.toString())

        a = str
        Log.d("tag", thisClass?.extensionReverse(a.toString()).toString())
        //companion object extension
        Log.d("tag", TestExt.companionExt())

    }
}

//Extension function
fun TestDemo.extensionReverse(ex: String): String {
    return ex.reversed()
}

// How smart-casting works..
fun TestDemo.extConvertType(page: Any, price: Any, name: Any, title: Any): String {
    val bookDetails: BookDetails = BookDetailsFrom(0, 0.0)
    val book: BookDetails = Book("", "")

    if (page is Int && price is Double) {
        bookDetails as BookDetailsFrom
        bookDetails.pages = page
        bookDetails.price = price.toDouble()
        return bookDetails.toString()

    } else if (name is String && title is String) {
        book as Book
        book.name = name
        book.title = title
        return book.toString()

    } else if (page is Double) {
        bookDetails as BookDetailsFrom
        /* This will not work casting..
        bookDetails.pages = page as Int*/
        bookDetails.pages = page.toInt()
        return bookDetails.toString()

    }
    return ""
}

// Companion object Extension function
open class TestExt() {
    companion object {
        fun companionExt(): String {
            return "This companion object is mostly use for constants properties"
        }
    }

    val anonymousObj = object {
        val anonymousVal = "This companion object is mostly use for constants properties"
    }
}


open class BookDetails()

// Data class..
data class Book(var name: String?, var title: String) : BookDetails() {}

data class BookDetailsFrom(var pages: Int, var price: Double) : BookDetails() {}


/* nested class.. without inner keyword.
class Outer(){
    val a= "1"
     class Inner(){
        fun innerA(): String{
            return a
        }
    }
}*/


abstract class AbstractClass() {
    class A : AbstractClass() {
        val cA: String = "Abstract Class's cA value"
    }
}


// Extended Inner class..out of package
internal class B : InternalClass() {
    class C : AbstractClass() {
        fun printClassName(s: String) {
            Log.e("tag", s)
        }
    }
}