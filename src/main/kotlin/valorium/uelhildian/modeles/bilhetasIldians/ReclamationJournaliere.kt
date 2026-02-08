package valorium.uelhildian.modeles.bilhetasIldians

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

/**
 * 📅 Enregistrement d'une réclamation journalière
 */
@Serializable
data class ReclamationJournaliere(
    val joueurUuid: String,
    val dateReclamation: String // Format ISO-8601 pour sérialisation
) {
    fun versDomaine(): Pair<UUID, LocalDate> {
        return UUID.fromString(joueurUuid) to LocalDate.parse(dateReclamation)
    }

    companion object {
        fun depuisDomaine(uuid: UUID, date: LocalDate): ReclamationJournaliere {
            return ReclamationJournaliere(
                joueurUuid = uuid.toString(),
                dateReclamation = date.toString()
            )
        }
    }
}

/**
 * 📋 Container pour la liste des réclamations
 */
@Serializable
data class ReclamationsData(
    val reclamations: List<ReclamationJournaliere> = emptyList()
)
