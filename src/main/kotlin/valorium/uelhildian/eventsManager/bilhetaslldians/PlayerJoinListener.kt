package valorium.uelhildian.eventsManagers.bilhetasIldians

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import valorium.uelhildian.services.bilhetasIldians.IBilhetaService

/**
 * 👋 Vérifie la péremption des bilhetas au join
 */
class PlayerJoinListener(
    private val plugin: Plugin,
    private val service: IBilhetaService
) : Listener {

    private val scope = CoroutineScope(Dispatchers.Default)

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val joueur = event.player

        plugin.logger.info("👋 ${joueur.name} s'est connecté, vérification de péremption...")

        // Vérifier de manière asynchrone
        scope.launch {
            service.verifierPeremption(joueur)
        }
    }
}
