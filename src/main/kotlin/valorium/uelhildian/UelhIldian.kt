package valorium.uelhildian

import org.bukkit.plugin.java.JavaPlugin
import valorium.uelhildian.commandes.BilhetaCommand
import valorium.uelhildian.config.BilhetasConfig
import valorium.uelhildian.eventsManagers.bilhetasIldians.ButtonClickListener
import valorium.uelhildian.eventsManagers.bilhetasIldians.PlayerJoinListener
import valorium.uelhildian.outils.bilhetasIldians.StorageHelper
import valorium.uelhildian.services.bilhetasIldians.BilhetaService
import valorium.uelhildian.services.bilhetasIldians.IBilhetaService

/**
 * 🎮 Plugin principal Uèlh Ildian
 */
class UelhIldian : JavaPlugin() {

    private lateinit var bilhetasConfig: BilhetasConfig
    private lateinit var storageHelper: StorageHelper
    private lateinit var bilhetaService: IBilhetaService

    override fun onEnable() {
        logger.info("🚀 Démarrage de Uèlh Ildian...")

        // Initialiser la configuration
        bilhetasConfig = BilhetasConfig(this)

        // Initialiser le stockage
        storageHelper = StorageHelper(this)

        // Initialiser le service principal
        bilhetaService = BilhetaService(this, bilhetasConfig)
        bilhetaService.initialiser()

        // Enregistrer les event listeners
        enregistrerListeners()

        // Enregistrer les commandes
        enregistrerCommandes()

        logger.info("✅ Uèlh Ildian activé avec succès !")
    }

    override fun onDisable() {
        logger.info("🛑 Arrêt de Uèlh Ildian...")

        // Arrêter proprement le service
        bilhetaService.arreter()

        logger.info("✅ Uèlh Ildian désactivé proprement")
    }

    /**
     * 📡 Enregistre les event listeners
     */
    private fun enregistrerListeners() {
        server.pluginManager.registerEvents(
            ButtonClickListener(this, bilhetaService, storageHelper),
            this
        )

        server.pluginManager.registerEvents(
            PlayerJoinListener(this, bilhetaService),
            this
        )

        logger.info("📡 Event listeners enregistrés")
    }

    /**
     * 🎮 Enregistre les commandes
     */
    private fun enregistrerCommandes() {
        val bilhetaCommand = BilhetaCommand(this, bilhetasConfig, storageHelper)
        getCommand("bilheta")?.setExecutor(bilhetaCommand)
        getCommand("bilheta")?.tabCompleter = bilhetaCommand

        logger.info("🎮 Commandes enregistrées")
    }
}
