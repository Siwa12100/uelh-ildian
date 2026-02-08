package valorium.uelhildian.services.bilhetasIldians

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import valorium.uelhildian.modeles.bilhetasIldians.ReclamationJournaliere
import valorium.uelhildian.modeles.bilhetasIldians.ReclamationsData
import valorium.uelhildian.outils.bilhetasIldians.DateParser
import valorium.uelhildian.outils.bilhetasIldians.StorageHelper
import java.time.LocalDate
import java.util.UUID

/**
 * 📋 Implémentation du suivi des réclamations
 */
class SuiviReclamations(
    private val plugin: Plugin,
    private val storage: StorageHelper
) : ISuiviReclamations {

    private val reclamations = mutableMapOf<UUID, LocalDate>()
    private val mutex = Mutex()

    override suspend fun charger() = withContext(Dispatchers.IO) {
        mutex.withLock {
            val data = storage.chargerReclamations()
            reclamations.clear()
            data.reclamations.forEach { reclamation ->
                val (uuid, date) = reclamation.versDomaine()
                reclamations[uuid] = date
            }
            plugin.logger.info("📥 ${reclamations.size} réclamation(s) chargée(s)")
        }
    }

    override suspend fun sauvegarder() = withContext(Dispatchers.IO) {
        mutex.withLock {
            val data = ReclamationsData(
                reclamations = reclamations.map { (uuid, date) ->
                    ReclamationJournaliere.depuisDomaine(uuid, date)
                }
            )
            storage.sauvegarderReclamations(data)
            plugin.logger.info("💾 ${reclamations.size} réclamation(s) sauvegardée(s)")
        }
    }

    override suspend fun peutReclamer(joueur: Player): Boolean = mutex.withLock {
        val derniereReclamation = reclamations[joueur.uniqueId]

        return@withLock if (derniereReclamation == null) {
            true
        } else {
            !DateParser.estAujourdhui(derniereReclamation)
        }
    }

    override suspend fun enregistrerReclamation(joueur: Player, date: LocalDate) = mutex.withLock {
        reclamations[joueur.uniqueId] = date
        plugin.logger.info("📝 Réclamation enregistrée pour ${joueur.name}")
        sauvegarder()
    }

    override suspend fun resetReclamationsJournalieres() = mutex.withLock {
        val avant = reclamations.size
        reclamations.clear()
        plugin.logger.info("🔄 Reset des réclamations ($avant → 0)")
        sauvegarder()
    }
}
