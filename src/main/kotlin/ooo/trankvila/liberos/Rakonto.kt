package ooo.trankvila.liberos

import java.io.InputStream
import java.io.OutputStream
import java.lang.RuntimeException

class Rakonto(eniro: String) {
   private val partoj: List<Parto>
   private val multefojaVortoj = mutableMapOf<String, String>()

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
               lastaBordo = null
               lastaKomenco = i + 1
            }
            '}' -> {
               if (lastaBordo != '{') {
                  throw RuntimeException("'{' ne trovita")
               }
               partoj.add(MultefojaVorto(eniro.substring(lastaKomenco, i)))
               lastaBordo = null
               lastaKomenco = i + 1
            }
            else -> {
            }
         }
      }
      this.partoj = partoj
   }

   fun komenci(eniro: InputStream, eliro: OutputStream) {
      val vortoj = partoj.filter { it is UnufojaVorto || it is MultefojaVorto }
      val elirilo = eliro.bufferedWriter()
      val legilo = eniro.bufferedReader()

      vortoj.forEachIndexed { index, vorto ->
         when (vorto) {
            is UnufojaVorto -> {
               elirilo.write("[${index + 1}/${vortoj.size}] $vorto: ")
               elirilo.flush()
               val eniraVorto = legilo.readLine()
               vorto.eniro = eniraVorto
            }
            is MultefojaVorto -> {
               if (!multefojaVortoj.containsKey(vorto.nomo)) {
                  elirilo.write("[${index + 1}/${vortoj.size}] $vorto: ")
                  elirilo.flush()
                  val eniraVorto = legilo.readLine()
                  multefojaVortoj[vorto.nomo] = eniraVorto
               }
               vorto.eniro = multefojaVortoj[vorto.nomo]
            }
            else -> {
            }
         }
      }

      partoj.map(Parto::toString).forEach(::print)
   }
}

sealed class Parto

data class Teksto(val teksto: String) : Parto() {
   override fun toString() = teksto
}

data class UnufojaVorto(val tipo: String) : Parto() {
   var eniro: String? = null

   override fun toString() = eniro?.let { "[$it]" } ?: tipo
}

data class MultefojaVorto(val nomo: String) : Parto() {
   var eniro: String? = null

   override fun toString() = eniro?.let { "{$it}" } ?: nomo
}