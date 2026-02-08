package valorium.uelhildian.outils.bilhetasIldians

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * 🎨 Création d'items spéciaux pour les bilhetas
 */
object ItemCreator {

    /**
     * 🎫 Crée un nametag bilheta enchanté
     */
    fun creerBilheta(nom: String): ItemStack {
        val item = ItemStack(Material.NAME_TAG, 1)
        val meta: ItemMeta = item.itemMeta ?: return item

        // Nom personnalisé
        meta.setDisplayName("§e✨ $nom")

        // Enchantement invisible pour marquer l'unicité
        meta.addEnchant(Enchantment.UNBREAKING, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)

        // Lore pour indiquer que c'est un item spécial
        meta.lore = listOf(
            "§7Bilheta officiel Ildian",
            "§7Non-craftable",
            "§7Périssable"
        )

        item.itemMeta = meta
        return item
    }

    /**
     * 📄 Transforme un bilheta en papier périmé
     */
    fun creerBilhetaPerime(ancienNom: String): ItemStack {
        val item = ItemStack(Material.PAPER, 1)
        val meta: ItemMeta = item.itemMeta ?: return item

        // Ajouter "Perimit" au nom
        val nomPerime = if (!ancienNom.contains("Perimit")) {
            "$ancienNom - Perimit"
        } else {
            ancienNom
        }

        meta.setDisplayName("§7📋 $nomPerime")
        meta.lore = listOf(
            "§7Bilheta périmé",
            "§cPlus de valeur"
        )

        item.itemMeta = meta
        return item
    }

    /**
     * 🔍 Vérifie si un item est un bilheta valide
     */
    fun estBilheta(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.NAME_TAG) return false

        val meta = item.itemMeta ?: return false
        val displayName = meta.displayName ?: return false

        return displayName.contains("Bilheta Ildian") &&
                !displayName.contains("Perimit") &&
                meta.hasEnchant(Enchantment.UNBREAKING)
    }

    /**
     * 📝 Extrait le nom brut (sans couleurs) d'un item
     */
    fun extraireNomBrut(item: ItemStack): String? {
        val meta = item.itemMeta ?: return null
        return meta.displayName?.replace("§[0-9a-fk-or]".toRegex(), "")?.trim()
    }
}
