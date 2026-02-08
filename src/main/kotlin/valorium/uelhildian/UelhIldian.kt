package valorium.uelhildian

import org.bukkit.plugin.java.JavaPlugin

class UelhIldian : JavaPlugin() {


    override fun onEnable() {
        logger.info("🌟 Démarrage de Uèlh Ildian...")


        logger.info("✅ Uèlh Ildian activé !")
    }

    override fun onDisable() {
        logger.info("🔌 Arrêt de Uèlh Ildian...")
        logger.info("✅ Uèlh Ildian désactivé !")
    }
}

