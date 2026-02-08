package valorium.uelhildian

import kotlinx.coroutines.runBlocking
import org.bukkit.plugin.java.JavaPlugin
import valorium.uelhildian.config.ApiConfig
import valorium.uelhildian.webClients.uelhApi.IUelhApiClient
import valorium.uelhildian.webClients.uelhApi.KtorUelhApiClient

class UelhIldian : JavaPlugin() {

    private lateinit var apiClient: IUelhApiClient

    override fun onEnable() {
        logger.info("🌟 Démarrage de Uèlh Ildian...")

        // Configuration de l'API
        val apiConfig = ApiConfig(
            baseUrl = config.getString("api.base-url", "http://localhost:8080"),
            timeoutSeconds = config.getLong("api.timeout", 30),
            retryAttempts = config.getInt("api.retry", 3)
        )

        // Initialisation du client API
        apiClient = KtorUelhApiClient(apiConfig, logger)

        // Test de connexion
        runBlocking {
            apiClient.checkConnection().onSuccess { apiInfo ->
                logger.info("✅ Connecté à l'API: ${apiInfo.name} ${apiInfo.version}")
            }.onFailure { error ->
                logger.warning("⚠️ Impossible de se connecter à l'API: ${error.message}")
            }
        }

        logger.info("✅ Uèlh Ildian activé !")
    }

    override fun onDisable() {
        logger.info("🔌 Arrêt de Uèlh Ildian...")
        apiClient.close()
        logger.info("✅ Uèlh Ildian désactivé !")
    }
}

