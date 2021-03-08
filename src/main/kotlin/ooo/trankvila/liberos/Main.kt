package ooo.trankvila.liberos

import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
   if (args.isEmpty()) {
      println("Argument: [input file]")
      exitProcess(-1)
   }
   val enhavo = File(args[0]).readText()
   val rakonto = Rakonto(enhavo)
   rakonto.komenci(System.`in`, System.out)
}