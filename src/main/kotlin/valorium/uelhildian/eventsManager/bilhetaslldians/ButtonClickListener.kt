package valorium.uelhildian.eventsManagers.bilhetasIldians

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin
import valorium.uelhildian.modeles.bilhetasIldians.BoutonDistribution
import valorium.uelhildian.outils.bilhetasIldians.StorageHelper
import valorium.uelhildian.services.bilhetasIldians.IBilhetaService

/**
 * 🔘 Écoute les clics sur le bouton de distribution
 */
class ButtonClickListener(
    private val plugin: Plugin,
    private val service: IBilhetaService,
    private val storage: StorageHelper
) : Listener {

    private val scope = CoroutineScope(Dispatchers.Default)

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        // Vérifier si c'est un clic droit sur un bouton
        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        val block = event.clickedBlock ?: return

        // Vérifier si c'est un bouton
        if (!estUnBouton(block.type)) return

        // Charger la configuration du bouton
        val boutonConfig = storage.chargerBouton() ?: return

        // Vérifier si c'est le bon bouton
        val locationBouton = boutonConfig.versLocation() ?: return

        if (block.location.blockX != locationBouton.blockX ||
            block.location.blockY != locationBouton.blockY ||
            block.location.blockZ != locationBouton.blockZ ||
            block.world.name != locationBouton.world?.name) {
            return
        }

        // C'est le bon bouton !
        val joueur = event.player
        plugin.logger.info("🔘 ${joueur.name} a cliqué sur le bouton de distribution")

        // Lancer la réclamation de manière asynchrone
        scope.launch {
            service.reclamerBilhetas(joueur)
        }
    }

    /**
     * 🔍 Vérifie si un matériau est un bouton
     */
    private fun estUnBouton(material: Material): Boolean {
        return material.name.contains("BUTTON")
    }
}
