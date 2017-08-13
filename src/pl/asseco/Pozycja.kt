package pl.asseco

/**
 * Created by qq on 2016-12-13.
 */
class Pozycja :Definicja {
    var nazwa: String
    var parametry: MutableMap<String, String>

    init {
        nazwa = ""
        parametry = mutableMapOf()
    }

    fun addParametr(nazwa: String, wartosc: String) {
        parametry.put(nazwa, wartosc)
    }

    /**
     * Metoda zwracająca wartość podanego parametru lub null gdy parametr o podanej nazwie nie jest zdefiniowany
     */
    fun getParametr(nazwa: String): String = parametry.getOrElse(nazwa) { "null" }

    override fun toString(): String {
        val out: StringBuilder = StringBuilder()
        out.append("$nazwa\n")
        for ((parametr, wartosc) in parametry) {
            out.append("$parametr $wartosc")
        }
        return out.toString()
    }

    /**
     * Metoda zwraca definicję pozycji ubbconfig lub dmconfig, np. serwera
     */
    override fun getDefinicja(): String {
        val out: StringBuilder = StringBuilder()
        out.append("\"$nazwa\"\t")
        for ((parametr, wartosc) in parametry) {
            out.append("$parametr=\"$wartosc\"\t")
        }
        out.append("\n")
        return out.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Pozycja

        if (nazwa != other.nazwa) return false
        if (parametry != other.parametry) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nazwa.hashCode()
        result = 31 * result + parametry.hashCode()
        return result
    }
}

fun main(args: Array<String>) {
    val serwer: Pozycja = Pozycja()
    serwer.nazwa = "serwer"
    serwer.addParametr("parametr1", "wartosc1")
    serwer.addParametr("parametr2", "wartosc2")
    println(serwer.getParametr("parametr2"))
    println(serwer.getDefinicja())

    val serwerWzorcowy: Pozycja = Pozycja()
    serwerWzorcowy.nazwa = "serwer"
    serwerWzorcowy.addParametr("parametr1", "wartosc1")
    serwerWzorcowy.addParametr("parametr2", "wartosc2")
    println(serwer.equals(serwerWzorcowy))
}