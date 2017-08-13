package pl.asseco

import pl.asseco.funkcje.suma
import java.util.*

/**
 * Created by qq on 2016-12-13.
 *
 */
//fun main(args: Array<String>) {
//    val napis = "aaab"
//    val res = when (napis) {
//        "aaa" -> "aaaaaa"
//        else -> {
//            "bbbbbbb"
//        }
//    }
//    println(res)
//    println(suma(2, 3))
//}

class JavaKotlin {
    var nazwa: String? = null
    private var parametry: Map<String, String>

    init {
        this.parametry = HashMap()
    }

    fun getParametr(nazwa: String): String = parametry.get(nazwa) as String

//    fun addParametr(nazwa: String, wartosc: String) {
//        parametry.put(nazwa, wartosc)
//    }
}

