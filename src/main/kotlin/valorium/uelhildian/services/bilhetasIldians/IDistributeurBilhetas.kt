package valorium.uelhildian.services.bilhetasIldians

import org.bukkit.entity.Player
import valorium.uelhildian.modeles.bilhetasIldians.Bilheta

/**
 * 🎁 Interface du distributeur de bilhetas
 */
interface IDistributeurBilhetas {
    /**
     * 📦 Distribue des bilhetas à un joueur
     */
    suspend fun distribuer(joueur: Player, nombre: Int)

    /**
     * 🎫 Crée un bilheta pour un joueur
     */
    fun creerBilheta(joueur: Player): Bilheta
}
