package valorium.uelhildian.services.bilhetasIldians

import org.bukkit.entity.Player
import java.time.LocalDate

/**
 * 📋 Interface du suivi des réclamations journalières
 */
interface ISuiviReclamations {
    /**
     * ✅ Vérifie si un joueur peut réclamer aujourd'hui
     */
    suspend fun peutReclamer(joueur: Player): Boolean

    /**
     * 📝 Enregistre une réclamation
     */
    suspend fun enregistrerReclamation(joueur: Player, date: LocalDate)

    /**
     * 🔄 Reset les réclamations journalières (appelé à minuit)
     */
    suspend fun resetReclamationsJournalieres()

    /**
     * 📥 Charge les données
     */
    suspend fun charger()

    /**
     * 💾 Sauvegarde les données
     */
    suspend fun sauvegarder()
}
