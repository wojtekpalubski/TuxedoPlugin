package pl.asseco

import pl.asseco.ReturnValue
import sun.misc.IOUtils
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

class TuxedoInputProcesor(private val srodowisko: String) {
    var psr: String = ""
    var tmunloadcf: String = ""
    var dmunloadcf: String = ""
    var ipcs: String = ""
    var execTimeout:Long=2;

    var listaSekcjiUbbconfig: List<String>
    var listaSekcjiDmconfig: List<String>

    init {
        listaSekcjiUbbconfig = mutableListOf()
        listaSekcjiDmconfig = mutableListOf()
    }

    fun isTuxedoRunning(): Boolean {
        return !execTuxedoCommand("psr").second.matches(".*entering boot mode.*".toRegex())
//        return 0 == execTuxedoCommand("psr").first
    }

    fun getSekcje(cfg: String): List<String> {
        val lista: MutableList<String>
        lista = mutableListOf()
        for (wiersz in cfg.lines()) {
            if (wiersz.startsWith("*")) {
                lista.add(wiersz)
            }
        }
        return lista
    }

    fun getNastepnaSekcjaUbbconfig(sekcja: String): String {
        return getNastepnaSekcja("ubbconfig", listaSekcjiUbbconfig, sekcja)
    }

    fun getNastepnaSekcjaDmconfig(sekcja: String): String {
        return getNastepnaSekcja("dmconfig", listaSekcjiDmconfig, sekcja)
    }

    private fun getNastepnaSekcja(typ: String, listaSekcji: List<String>, sekcja: String): String {
        var lista: List<String> = listaSekcji
        if (lista.isEmpty()) {
            if ("ubbconfig".equals(typ)) {
                if (!tmunloadcf.isNullOrBlank()) {
                    lista = getSekcje(tmunloadcf)
                    return getNastepnaSekcja(typ, lista, sekcja)
                } else {
                    return "null"
                }
            } else {
                if (!dmunloadcf.isNullOrBlank()) {
                    lista = getSekcje(dmunloadcf)
                    return getNastepnaSekcja(typ, lista, sekcja)
                } else {
                    return "null"
                }
            }
        }
        if (!lista.contains(sekcja)) {
            return "null"
        }
        try {
            return lista.get(lista.indexOf(sekcja) + 1)
        } catch (e: IndexOutOfBoundsException) {
            return "*END"
        }
    }

    private fun execSystemCommand(polecenie: String): Pair<Int, String> {
        try {
            var proces = Runtime.getRuntime().exec(polecenie)
            proces.waitFor(execTimeout,TimeUnit.MINUTES)
//            proces.outputStream.use { println(this) }
            println(proces.inputStream.bufferedReader().readText())
//            BufferedReader(StreamReader(proces.outputStream)).lines().collect(Collectors.joining("\n"))
//            println(proces.outputStream.toString())
            return Pair(proces.exitValue(), proces.errorStream.bufferedReader().readText()+"\n"+proces.inputStream.bufferedReader().readText())
        } catch(e: Exception) {
            println("${ReturnValue.BLAD.kod} "+e.toString())
            return Pair(ReturnValue.BLAD.kod, e.toString())
        }
    }

    private fun execTuxedoCommand(polecenie: String): Pair<Int, String> {
        return execSystemCommand(". $srodowisko; echo $polecenie")
    }

    private fun execTmadminCommand(polecenie: String): Pair<Int, String> {
        return execSystemCommand(". $srodowisko; echo $polecenie|tmadmin")
    }

    private fun execDmadminCommand(polecenie: String): Pair<Int, String> {
        return execSystemCommand(". $srodowisko; echo $polecenie|dmadmin")
    }

    private fun execTmunloadcf(): String {
        val out: String = execTuxedoCommand("tmunloadcf").second
        return out;
    }

    private fun execDmunloadcf(): String {
        val out: String = execTuxedoCommand("dmunloadcf").second
        return out;
    }

    private fun execTmadminPsr(): String {
        val out: String = execTmadminCommand("psr -v").second
        return out;
    }

    private fun execIpcs(): String {
        val out: String = execSystemCommand("ipcs -qbop").second
        return out;
    }

    fun getSerwery():String{
        var out:String
        val regexp="\\\\*SERVERS.\\*"+getNastepnaSekcjaUbbconfig("*SERVERS")
        val reg:Regex=Regex(regexp)
        println("re"+tmunloadcf.matches(reg))
        return regexp
    }

}

fun main(args: Array<String>) {
    val procesor: TuxedoInputProcesor = TuxedoInputProcesor("aaa")
    procesor.tmunloadcf = """*RESOURCES
*GROUPS
*SERVERS
serwer1
serwer2
*END
"""
    procesor.dmunloadcf = """*LOCAL_DOMAINS
*DOMAINS
*EXPORTS
"""
    println(procesor.getNastepnaSekcjaUbbconfig("*RESOURCES"))
    procesor.listaSekcjiUbbconfig = procesor.getSekcje(procesor.tmunloadcf)
    println(procesor.listaSekcjiUbbconfig)
    println(procesor.getNastepnaSekcjaUbbconfig("*GROUPS"))
    println(procesor.getNastepnaSekcjaUbbconfig("*SERVERS"))

    println(procesor.isTuxedoRunning())
    println(procesor.getNastepnaSekcjaDmconfig("*LOCAL_DOMAINS"))
    println(procesor.getNastepnaSekcjaDmconfig("*EXPORTS"))
    println(procesor.getSerwery())
}