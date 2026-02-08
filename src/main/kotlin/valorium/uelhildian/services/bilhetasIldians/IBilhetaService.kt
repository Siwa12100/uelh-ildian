package valorium.uelhildian.services.bilhetasIldians

import org.bukkit.entity.Player

/**
 * 🎫 Interface du service principal de gestion des bilhetas
 */
interface IBilhetaService {
    /**
     * 🎁 Permet à un joueur de réclamer ses bilhetas journaliers
     */
    suspend fun reclamerBilhetas(joueur: Player): Boolean

    /**
     * 🔍 Vérifie et transforme les bilhetas périmés d'un joueur
     */
    suspend fun verifierPeremption(joueur: Player): Int

    /**
     * 🚀 Initialise le service
     */
    fun initialiser()

    /**
     * 🛑 Arrête le service
     */
    fun arreter()
}
