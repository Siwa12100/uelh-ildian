package valorium.uelhildian.services.bilhetasIldians

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import valorium.uelhildian.config.BilhetasConfig
import valorium.uelhildian.outils.bilhetasIldians.DateParser
import valorium.uelhildian.outils.bilhetasIldians.ItemCreator
import java.time.LocalDate

/**
 * ⏰ Implémentation du gestionnaire de péremption
 */
class GestionnairePeremption(
    private val plugin: Plugin,
    private val config: BilhetasConfig
) : IGestionnairePeremption {

    override suspend fun verifierInventaire(joueur: Player): Int = withContext(Dispatchers.IO) {
        var nombreTransformations = 0

        val inventaire = joueur.inventory.contents.filterNotNull()

        for ((index, item) in inventaire.withIndex()) {
            if (ItemCreator.estBilheta(item)) {
                val nomBrut = ItemCreator.extraireNomBrut(item) ?: continue
                val dateBillet = DateParser.extraireDateDuNom(nomBrut, config.formatDate) ?: continue

                if (estPerime(dateBillet, config.dureeValiditeJours)) {
                    // Transformer sur le thread principal
                    withContext(Dispatchers.Default) {
                        plugin.server.scheduler.runTask(plugin, Runnable {
                            val itemPerime = transformerEnPerime(item)
                            joueur.inventory.setItem(index, itemPerime)
                        })
                    }
                    nombreTransformations++
                }
            }
        }

        if (nombreTransformations > 0) {
            plugin.logger.info("⏰ $nombreTransformations bilheta(s) périmé(s) transformé(s) pour ${joueur.name}")
        }

        nombreTransformations
    }

    override fun estPerime(dateBillet: LocalDate, dureeValidite: Int): Boolean {
        val dateExpiration = dateBillet.plusDays(dureeValidite.toLong())
        return LocalDate.now().isAfter(dateExpiration)
    }

    override fun transformerEnPerime(item: ItemStack): ItemStack {
        val nomBrut = ItemCreator.extraireNomBrut(item) ?: return item
        return ItemCreator.creerBilhetaPerime(nomBrut)
    }
}
