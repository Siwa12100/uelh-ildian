package valorium.uelhildian.services.bilhetasIldians

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import valorium.uelhildian.config.BilhetasConfig
import valorium.uelhildian.modeles.bilhetasIldians.Bilheta
import valorium.uelhildian.outils.bilhetasIldians.ItemCreator
import java.time.LocalDate

/**
 * 🎁 Implémentation du distributeur de bilhetas
 */
class DistributeurBilhetas(
    private val plugin: Plugin,
    private val config: BilhetasConfig
) : IDistributeurBilhetas {

    override suspend fun distribuer(joueur: Player, nombre: Int) = withContext(Dispatchers.IO) {
        plugin.logger.info("🎁 Distribution de $nombre bilheta(s) à ${joueur.name}")

        repeat(nombre) {
            val bilheta = creerBilheta(joueur)
            val nom = bilheta.genererNom("dd/MM/yy")
            val item = ItemCreator.creerBilheta(nom)

            // Ajouter à l'inventaire (sur le thread principal)
            withContext(Dispatchers.Default) {
                plugin.server.scheduler.runTask(plugin, Runnable {
                    joueur.inventory.addItem(item)
                })
            }
        }

        plugin.logger.info("✅ Distribution terminée pour ${joueur.name}")
    }

    override fun creerBilheta(joueur: Player): Bilheta {
        return Bilheta(
            pseudo = joueur.name,
            dateCreation = LocalDate.now(),
            estPerime = false
        )
    }
}
