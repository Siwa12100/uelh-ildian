package valorium.uelhildian.commandes

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import valorium.uelhildian.config.BilhetasConfig
import valorium.uelhildian.modeles.bilhetasIldians.BoutonDistribution
import valorium.uelhildian.outils.bilhetasIldians.StorageHelper

/**
 * 🎮 Commande pour gérer le système de bilhetas
 */
class BilhetaCommand(
    private val plugin: Plugin,
    private val config: BilhetasConfig,
    private val storage: StorageHelper
) : CommandExecutor, TabCompleter {

    private val joueursEnModeSelection = mutableSetOf<Player>()

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§c❌ Cette commande ne peut être exécutée que par un joueur")
            return true
        }

        if (!sender.hasPermission("uelh.bilheta.admin")) {
            sender.sendMessage("§c❌ Vous n'avez pas la permission d'utiliser cette commande")
            return true
        }

        when (args.getOrNull(0)?.lowercase()) {
            "setbouton" -> {
                activerModeSelection(sender)
                return true
            }
            "info" -> {
                afficherInfo(sender)
                return true
            }
            "reload" -> {
                config.charger()
                sender.sendMessage("§a✅ Configuration rechargée")
                return true
            }
            else -> {
                afficherAide(sender)
                return true
            }
        }
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String> {
        if (args.size == 1) {
            return listOf("setbouton", "info", "reload")
                .filter { it.startsWith(args[0].lowercase()) }
        }
        return emptyList()
    }

    /**
     * 🎯 Active le mode sélection de bouton
     */
    private fun activerModeSelection(joueur: Player) {
        joueursEnModeSelection.add(joueur)
        joueur.sendMessage("§e✨ Mode sélection activé ! Cliquez sur un bouton pour le définir comme bouton de distribution.")

        // Enregistrer l'event listener
        plugin.server.pluginManager.registerEvents(
            object : org.bukkit.event.Listener {
                @org.bukkit.event.EventHandler
                fun onInteract(event: org.bukkit.event.player.PlayerInteractEvent) {
                    if (event.player != joueur) return
                    if (!joueursEnModeSelection.contains(joueur)) return

                    val block = event.clickedBlock ?: return

                    if (!block.type.name.contains("BUTTON")) {
                        joueur.sendMessage("§c❌ Ce n'est pas un bouton !")
                        return
                    }

                    event.isCancelled = true

                    val bouton = BoutonDistribution.depuisLocation(block.location)
                    storage.sauvegarderBouton(bouton)

                    joueursEnModeSelection.remove(joueur)
                    joueur.sendMessage(config.messageBoutonSelectionne)
                    joueur.sendMessage("§7Position: §e${block.location.blockX}, ${block.location.blockY}, ${block.location.blockZ}")

                    plugin.logger.info("🔘 Bouton de distribution défini à ${block.location}")
                }
            },
            plugin
        )
    }

    /**
     * 📊 Affiche les informations du système
     */
    private fun afficherInfo(joueur: Player) {
        joueur.sendMessage("§e§l=== Bilhetas Ildians ===")
        joueur.sendMessage("§7Billets par jour: §e${config.billetsParJour}")
        joueur.sendMessage("§7Durée de validité: §e${config.dureeValiditeJours} jours")
        joueur.sendMessage("§7Vérification: §eToutes les ${config.verificationIntervalleMinutes} minutes")

        val bouton = storage.chargerBouton()
        if (bouton != null) {
            joueur.sendMessage("§7Bouton: §e${bouton.x}, ${bouton.y}, ${bouton.z} (${bouton.monde})")
        } else {
            joueur.sendMessage("§7Bouton: §cNon défini")
        }
    }

    /**
     * 📖 Affiche l'aide de la commande
     */
    private fun afficherAide(joueur: Player) {
        joueur.sendMessage("§e§l=== Commandes Bilhetas ===")
        joueur.sendMessage("§7/bilheta setbouton §f- Définir le bouton de distribution")
        joueur.sendMessage("§7/bilheta info §f- Afficher les informations")
        joueur.sendMessage("§7/bilheta reload §f- Recharger la configuration")
    }
}
