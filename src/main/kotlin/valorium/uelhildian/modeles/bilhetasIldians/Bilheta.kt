package valorium.uelhildian.modeles.bilhetasIldians

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 🎫 Représente un bilheta Ildian
 */
data class Bilheta(
    val pseudo: String,
    val dateCreation: LocalDate,
    val estPerime: Boolean = false
) {
    /**
     * 📝 Génère le nom formaté du bilheta
     */
    fun genererNom(formatDate: String): String {
        val dateFormatee = dateCreation.format(DateTimeFormatter.ofPattern(formatDate))
        return if (estPerime) {
            "Bilheta Ildian - $dateFormatee - $pseudo - Perimit"
        } else {
            "Bilheta Ildian - $dateFormatee - $pseudo"
        }
    }
}
