package valorium.uelhildian.services.bilhetasIldians

import kotlinx.coroutines.*
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import valorium.uelhildian.config.BilhetasConfig
import valorium.uelhildian.outils.bilhetasIldians.StorageHelper
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * 🎫 Service principal orchestrant tout le système de bilhetas
 */
class BilhetaService(
    private val plugin: Plugin,
    private val config: BilhetasConfig
) : IBilhetaService {

    private val storage = StorageHelper(plugin)
    private val distributeur: IDistributeurBilhetas = DistributeurBilhetas(plugin, config)
    private val gestionnairePeremption: IGestionnairePeremption = GestionnairePeremption(plugin, config)
    private val suiviReclamations: ISuiviReclamations = SuiviReclamations(plugin, storage)

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var tacheVerificationPeremption: BukkitTask? = null
    private var tacheResetMinuit: BukkitTask? = null

    override fun initialiser() {
        plugin.logger.info("🚀 Initialisation du service Bilhetas Ildians")

        // Charger les données
        serviceScope.launch {
            suiviReclamations.charger()
        }

        // Démarrer la vérification périodique de péremption
        demarrerVerificationPeremption()

        // Démarrer la tâche de reset à minuit
        demarrerResetMinuit()

        plugin.logger.info("✅ Service Bilhetas Ildians initialisé")
    }

    override fun arreter() {
        plugin.logger.info("🛑 Arrêt du service Bilhetas Ildians")

        // Annuler les tâches
        tacheVerificationPeremption?.cancel()
        tacheResetMinuit?.cancel()

        // Annuler le scope et attendre la fin
        runBlocking {
            serviceScope.cancel()
            serviceScope.coroutineContext[Job]?.join()
        }

        plugin.logger.info("✅ Service Bilhetas Ildians arrêté")
    }

    override suspend fun reclamerBilhetas(joueur: Player): Boolean = withContext(Dispatchers.IO) {
        plugin.logger.info("🎫 ${joueur.name} tente de réclamer des bilhetas")

        if (!suiviReclamations.peutReclamer(joueur)) {
            // Message sur le thread principal
            withContext(Dispatchers.Default) {
                plugin.server.scheduler.runTask(plugin, Runnable {
                    joueur.sendMessage(config.messageDejaReclame)
                })
            }
            plugin.logger.info("❌ ${joueur.name} a déjà réclamé aujourd'hui")
            return@withContext false
        }

        // Distribuer les bilhetas
        val nombre = config.billetsParJour
        distributeur.distribuer(joueur, nombre)

        // Enregistrer la réclamation
        suiviReclamations.enregistrerReclamation(joueur, LocalDate.now())

        // Message de succès sur le thread principal
        withContext(Dispatchers.Default) {
            plugin.server.scheduler.runTask(plugin, Runnable {
                val message = config.messageSucces.replace("{nombre}", nombre.toString())
                joueur.sendMessage(message)
            })
        }

        plugin.logger.info("✅ ${joueur.name} a reçu $nombre bilheta(s)")
        true
    }

    override suspend fun verifierPeremption(joueur: Player): Int {
        return gestionnairePeremption.verifierInventaire(joueur)
    }

    /**
     * ⏰ Démarre la vérification périodique de péremption
     */
    private fun demarrerVerificationPeremption() {
        val intervalleMinutes = config.verificationIntervalleMinutes
        val intervalleTicks = intervalleMinutes * 60 * 20 // Conversion en ticks (20 ticks = 1 seconde)

        tacheVerificationPeremption = plugin.server.scheduler.runTaskTimerAsynchronously(
            plugin,
            Runnable {
                serviceScope.launch {
                    plugin.logger.info("🔍 Vérification périodique de péremption")

                    plugin.server.onlinePlayers.forEach { joueur ->
                        val nombreTransformations = verifierPeremption(joueur)

                        if (nombreTransformations > 0) {
                            withContext(Dispatchers.Default) {
                                plugin.server.scheduler.runTask(plugin, Runnable {
                                    val message = config.messagePeremption
                                        .replace("{nombre}", nombreTransformations.toString())
                                    joueur.sendMessage(message)
                                })
                            }
                        }
                    }
                }
            },
            intervalleTicks,
            intervalleTicks
        )

        plugin.logger.info("⏰ Vérification de péremption démarrée (toutes les $intervalleMinutes minutes)")
    }

    /**
     * 🌙 Démarre la tâche de reset à minuit
     */
    private fun demarrerResetMinuit() {
        // Calculer le délai jusqu'à minuit
        val maintenant = LocalDateTime.now()
        val prochainMinuit = maintenant.toLocalDate().plusDays(1).atStartOfDay()
        val secondesJusquaMinuit = ChronoUnit.SECONDS.between(maintenant, prochainMinuit)
        val ticksJusquaMinuit = secondesJusquaMinuit * 20

        tacheResetMinuit = plugin.server.scheduler.runTaskTimerAsynchronously(
            plugin,
            Runnable {
                serviceScope.launch {
                    plugin.logger.info("🌙 Reset des réclamations journalières (minuit)")
                    suiviReclamations.resetReclamationsJournalieres()
                }
            },
            ticksJusquaMinuit,
            24 * 60 * 60 * 20L // 24 heures en ticks
        )

        plugin.logger.info("🌙 Tâche de reset à minuit programmée")
    }
}
