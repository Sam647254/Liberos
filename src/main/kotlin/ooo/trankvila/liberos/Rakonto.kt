package ooo.trankvila.liberos

import java.io.InputStream
import java.io.OutputStream
import java.lang.RuntimeException

class Rakonto(eniro: String) {
   private val partoj: List<Parto>

   init {
      val partoj = mutableListOf<Parto>()
      var lastaKomenco = 0
      var lastaBordo: Char? = null
      eniro.forEachIndexed { i, c ->
         when (c) {
            '[', '{' -> {
               if (lastaBordo != null) {
                  throw RuntimeException("Neparigita $lastaBordo")
               }
               partoj.add(Teksto(eniro.substring(lastaKomenco, i)))
               lastaKomenco = i + 1
               lastaBordo = c
            }
            ']' -> {
               if (lastaBordo != '[') {
                  throw RuntimeException("'[' ne trovita")
               }
               partoj.add(UnufojaVorto(eniro.substring(lastaKomenco, i)))
            }
            '}' -> {
               if (lastaBordo != '{') {
                  throw RuntimeException("'{' ne trovita")
               }
               partoj.add(MultefojaVorto(eniro.substring(lastaKomenco, i)))
            }
            else -> { }
         }
      }
      this.partoj = partoj
   }

   fun komenci(eniro: InputStream, eliro: OutputStream) {
      val vortoj = partoj.filter { it is UnufojaVorto || it is MultefojaVorto }
      val elirilo = eliro.writer()
      val legilo = eniro.bufferedReader()

      vortoj.forEachIndexed { index, vorto ->
      }
   }
}

sealed class Parto

data class Teksto(val teksto: String) : Parto()

data class UnufojaVorto(val tipo: String) : Parto() {
   override fun toString(): String {
      return tipo
   }
}

data class MultefojaVorto(val nomo: String) : Parto()