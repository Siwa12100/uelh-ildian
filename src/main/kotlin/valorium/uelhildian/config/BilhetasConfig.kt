package valorium.uelhildian.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.time.format.DateTimeFormatter

/**
 * 📋 Configuration du système de Bilhetas Ildians
 */
class BilhetasConfig(private val plugin: Plugin) {
    private val configFile: File = File(plugin.dataFolder, "bilhetas-config.yml")
    private lateinit var config: FileConfiguration

    val billetsParJour: Int
        get() = config.getInt("bilhetas.billets-par-jour", 3)

    val dureeValiditeJours: Int
        get() = config.getInt("bilhetas.duree-validite-jours", 4)

    val formatDate: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern(
            config.getString("bilhetas.format-date", "dd/MM/yy")
        )

    val verificationIntervalleMinutes: Long
        get() = config.getLong("bilhetas.verification-intervalle-minutes", 60)

    val messageDejaReclame: String
        get() = config.getString(
            "bilhetas.messages.deja-reclame",
            "§c❌ Vous avez déjà réclamé vos bilhetas aujourd'hui !"
        ) ?: "§c❌ Vous avez déjà réclamé vos bilhetas aujourd'hui !"

    val messageSucces: String
        get() = config.getString(
            "bilhetas.messages.succes",
            "§a✅ Vous avez reçu {nombre} bilheta(s) Ildian(s) !"
        ) ?: "§a✅ Vous avez reçu {nombre} bilheta(s) Ildian(s) !"

    val messageBoutonSelectionne: String
        get() = config.getString(
            "bilhetas.messages.bouton-selectionne",
            "§a✅ Bouton de distribution sélectionné !"
        ) ?: "§a✅ Bouton de distribution sélectionné !"

    val messagePeremption: String
        get() = config.getString(
            "bilhetas.messages.peremption",
            "§e⏰ {nombre} bilheta(s) ont expiré et ont été transformé(s) en papier."
        ) ?: "§e⏰ {nombre} bilheta(s) ont expiré et ont été transformé(s) en papier."

    init {
        charger()
    }

    /**
     * 📥 Charge ou crée la configuration
     */
    fun charger() {
        if (!plugin.dataFolder.exists()) {
            plugin.dataFolder.mkdirs()
        }

        if (!configFile.exists()) {
            creerConfigurationParDefaut()
        }

        config = YamlConfiguration.loadConfiguration(configFile)
        plugin.logger.info("✅ Configuration Bilhetas chargée")
    }

    /**
     * 💾 Sauvegarde la configuration
     */
    fun sauvegarder() {
        config.save(configFile)
        plugin.logger.info("💾 Configuration Bilhetas sauvegardée")
    }

    /**
     * 📝 Crée la configuration par défaut
     */
    private fun creerConfigurationParDefaut() {
        config = YamlConfiguration()

        config.set("bilhetas.billets-par-jour", 3)
        config.set("bilhetas.duree-validite-jours", 4)
        config.set("bilhetas.format-date", "dd/MM/yy")
        config.set("bilhetas.verification-intervalle-minutes", 60)

        config.set("bilhetas.messages.deja-reclame", "§c❌ Vous avez déjà réclamé vos bilhetas aujourd'hui !")
        config.set("bilhetas.messages.succes", "§a✅ Vous avez reçu {nombre} bilheta(s) Ildian(s) !")
        config.set("bilhetas.messages.bouton-selectionne", "§a✅ Bouton de distribution sélectionné !")
        config.set("bilhetas.messages.peremption", "§e⏰ {nombre} bilheta(s) ont expiré et ont été transformé(s) en papier.")

        config.save(configFile)
        plugin.logger.info("📝 Configuration Bilhetas par défaut créée")
    }
}
