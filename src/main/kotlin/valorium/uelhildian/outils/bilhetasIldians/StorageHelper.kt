package valorium.uelhildian.outils.bilhetasIldians

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.plugin.Plugin
import valorium.uelhildian.modeles.bilhetasIldians.BoutonDistribution
import valorium.uelhildian.modeles.bilhetasIldians.ReclamationsData
import java.io.File

/**
 * 💾 Gestion du stockage des données
 */
class StorageHelper(private val plugin: Plugin) {

    private val dataFolder = File(plugin.dataFolder, "bilhetas")
    private val reclamationsFile = File(dataFolder, "reclamations.json")
    private val boutonFile = File(dataFolder, "bouton.json")

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    init {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }
    }

    /**
     * 📥 Charge les réclamations
     */
    fun chargerReclamations(): ReclamationsData {
        return try {
            if (reclamationsFile.exists()) {
                val contenu = reclamationsFile.readText()
                json.decodeFromString<ReclamationsData>(contenu)
            } else {
                ReclamationsData()
            }
        } catch (e: Exception) {
            plugin.logger.warning("⚠️ Erreur lors du chargement des réclamations: ${e.message}")
            ReclamationsData()
        }
    }

    /**
     * 💾 Sauvegarde les réclamations
     */
    fun sauvegarderReclamations(data: ReclamationsData) {
        try {
            val contenu = json.encodeToString(data)
            reclamationsFile.writeText(contenu)
        } catch (e: Exception) {
            plugin.logger.severe("❌ Erreur lors de la sauvegarde des réclamations: ${e.message}")
        }
    }

    /**
     * 📥 Charge le bouton de distribution
     */
    fun chargerBouton(): BoutonDistribution? {
        return try {
            if (boutonFile.exists()) {
                val contenu = boutonFile.readText()
                json.decodeFromString<BoutonDistribution>(contenu)
            } else {
                null
            }
        } catch (e: Exception) {
            plugin.logger.warning("⚠️ Erreur lors du chargement du bouton: ${e.message}")
            null
        }
    }

    /**
     * 💾 Sauvegarde le bouton de distribution
     */
    fun sauvegarderBouton(bouton: BoutonDistribution) {
        try {
            val contenu = json.encodeToString(bouton)
            boutonFile.writeText(contenu)
        } catch (e: Exception) {
            plugin.logger.severe("❌ Erreur lors de la sauvegarde du bouton: ${e.message}")
        }
    }

    /**
     * 🗑️ Supprime le bouton
     */
    fun supprimerBouton() {
        if (boutonFile.exists()) {
            boutonFile.delete()
        }
    }
}
