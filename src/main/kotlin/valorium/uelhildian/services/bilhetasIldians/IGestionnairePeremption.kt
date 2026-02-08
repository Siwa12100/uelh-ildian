package valorium.uelhildian.services.bilhetasIldians

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.time.LocalDate

/**
 * ⏰ Interface du gestionnaire de péremption
 */
interface IGestionnairePeremption {
    /**
     * 🔍 Vérifie l'inventaire d'un joueur et transforme les bilhetas périmés
     */
    suspend fun verifierInventaire(joueur: Player): Int

    /**
     * 📅 Vérifie si un bilheta est périmé
     */
    fun estPerime(dateBillet: LocalDate, dureeValidite: Int): Boolean

    /**
     * 🔄 Transforme un item en bilheta périmé
     */
    fun transformerEnPerime(item: ItemStack): ItemStack
}
