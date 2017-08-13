package pl.asseco

/**
 * Created by qq on 2016-12-23.
 */
class Sekcja : Definicja {

    var nazwa: String = ""
    val pozycje: MutableList<Pozycja>

    init {
        pozycje = mutableListOf()
    }

    fun addPozycja(pozycja: Pozycja) {
        pozycje.add(pozycja)
    }

    override fun getDefinicja(): String {
        val out: StringBuilder = StringBuilder()
        out.append("$nazwa\n")
        for (pozycja in pozycje) {
            out.append(pozycja.getDefinicja())
            out.append("\n")
        }
        return out.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Sekcja

        if (nazwa != other.nazwa) return false
        if (pozycje != other.pozycje) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nazwa.hashCode()
        result = 31 * result + pozycje.hashCode()
        return result
    }


}

fun main(args: Array<String>) {
    val sekcjaServers: Sekcja = Sekcja()
    val sekcjaSerwersWzorcowa: Sekcja = Sekcja()

    val serwer: Pozycja = Pozycja()
    serwer.nazwa = "serwer"
    serwer.addParametr("parametr1", "wartosc1")
    serwer.addParametr("parametr2", "wartosc2")
    sekcjaServers.nazwa = "*SERVERS"
    sekcjaServers.addPozycja(serwer)

    sekcjaSerwersWzorcowa.nazwa = "*SERVERS"
    sekcjaSerwersWzorcowa.addPozycja(serwer)

    println(sekcjaServers.getDefinicja())
    println(sekcjaServers.equals(sekcjaSerwersWzorcowa))
    println(sekcjaSerwersWzorcowa.getDefinicja())
}