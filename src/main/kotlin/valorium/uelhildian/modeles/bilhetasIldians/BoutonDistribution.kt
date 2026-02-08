package valorium.uelhildian.modeles.bilhetasIldians

import kotlinx.serialization.Serializable
import org.bukkit.Bukkit
import org.bukkit.Location

/**
 * 🔘 Représente un bouton de distribution
 */
@Serializable
data class BoutonDistribution(
    val monde: String,
    val x: Int,
    val y: Int,
    val z: Int
) {
    fun versLocation(): Location? {
        val world = Bukkit.getWorld(monde) ?: return null
        return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    }

    companion object {
        fun depuisLocation(location: Location): BoutonDistribution {
            return BoutonDistribution(
                monde = location.world?.name ?: "world",
                x = location.blockX,
                y = location.blockY,
                z = location.blockZ
            )
        }
    }
}