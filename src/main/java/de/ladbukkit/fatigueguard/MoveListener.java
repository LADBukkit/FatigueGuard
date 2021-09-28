package de.ladbukkit.fatigueguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The listener that checks if a player enters a region.
 * @author LADBukkit (Robin Eschbach)
 */
public class MoveListener implements Listener {

    /**
     * The plugin this listener belongs to.
     */
    private final FatigueGuard plugin;

    /**
     * Creates a new move listener with the plugin it belongs to.
     * @param plugin The plugin this listener belongs to.
     */
    public MoveListener(FatigueGuard plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks the regions a player is in.
     * @param e The player move event.
     */
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(p.getWorld()));
        ApplicableRegionSet regionSet = manager.getApplicableRegions(BlockVector3.at(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ()));
        List<String> slowRegions = plugin.getConfig().getStringList("regions." + p.getWorld().getName());
        Set<String> strRegions = regionSet.getRegions().stream().map(ProtectedRegion::getId).collect(Collectors.toSet());

        // check for intersections in the lists slowRegions and strRegions
        Set<String> reg = slowRegions.stream().distinct().filter(strRegions::contains).collect(Collectors.toSet());

        if(reg.size() > 0 || slowRegions.contains("__global__")) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, plugin.getConfig().getInt("fatigue")));
        } else {
            p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        }
    }
}
