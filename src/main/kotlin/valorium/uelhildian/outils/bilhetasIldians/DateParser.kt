package valorium.uelhildian.outils.bilhetasIldians

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * 📅 Utilitaire pour parser et formater les dates
 */
object DateParser {

    /**
     * 🔍 Extrait la date d'un nom de bilheta
     * Format attendu: "Bilheta Ildian - DD/MM/YY - Pseudo"
     */
    fun extraireDateDuNom(nomItem: String, formatter: DateTimeFormatter): LocalDate? {
        return try {
            // Regex pour extraire la date entre les tirets
            val regex = """Bilheta Ildian - (\d{2}/\d{2}/\d{2}) - .*""".toRegex()
            val matchResult = regex.find(nomItem)

            matchResult?.groupValues?.get(1)?.let { dateStr ->
                LocalDate.parse(dateStr, formatter)
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 📝 Formate une date selon le format configuré
     */
    fun formaterDate(date: LocalDate, formatter: DateTimeFormatter): String {
        return date.format(formatter)
    }

    /**
     * ⏰ Vérifie si une date est aujourd'hui
     */
    fun estAujourdhui(date: LocalDate): Boolean {
        return date == LocalDate.now()
    }

    /**
     * 📆 Calcule le nombre de jours entre deux dates
     */
    fun joursEntre(dateDebut: LocalDate, dateFin: LocalDate): Long {
        return ChronoUnit.DAYS.between(dateDebut, dateFin)
    }
}
