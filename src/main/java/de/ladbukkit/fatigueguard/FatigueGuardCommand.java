package de.ladbukkit.fatigueguard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Handles the fatigue guard command.
 * @author LADBukkit (Robin Eschbach)
 */
public class FatigueGuardCommand implements CommandExecutor {

    /**
     * The plugin this belongs to.
     */
    private final FatigueGuard plugin;

    /**
     * Creates the command executor with the plugin it belongs to.
     * @param plugin The plugin this executor belongs to.
     */
    public FatigueGuardCommand(FatigueGuard plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("fatigueguard.admin")) {
            sender.sendMessage(plugin.getMessageConfig().get("nopermissions"));
            return false;
        }

        if(args.length == 3) {
            if(args[0].equalsIgnoreCase("add")) {
                if(Bukkit.getWorld(args[1]) == null) {
                    sender.sendMessage(plugin.getMessageConfig().get("worldnotfound"));
                    return false;
                }

                List<String> regions = plugin.getConfig().getStringList("regions." + args[1]);

                if(regions.contains(args[2])) {
                    sender.sendMessage(plugin.getMessageConfig().get("regionfound"));
                    return false;
                }

                regions.add(args[2]);
                plugin.getConfig().set("regions." + args[1], regions);
                plugin.saveConfig();
                sender.sendMessage(plugin.getMessageConfig().get("regionadd"));
                return false;
            }
            if(args[0].equalsIgnoreCase("remove")) {
                if(Bukkit.getWorld(args[1]) == null) {
                    sender.sendMessage(plugin.getMessageConfig().get("worldnotfound"));
                    return false;
                }

                List<String> regions = plugin.getConfig().getStringList("regions." + args[1]);

                if(!regions.contains(args[2])) {
                    sender.sendMessage(plugin.getMessageConfig().get("regionnotfound"));
                    return false;
                }

                regions.remove(args[2]);
                plugin.getConfig().set("regions." + args[1], regions);
                plugin.saveConfig();
                sender.sendMessage(plugin.getMessageConfig().get("regiondel"));
                return false;
            }
        }
        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("fatigue")) {
                try {
                    Integer level = Integer.parseInt(args[1]);
                    plugin.getConfig().set("fatigue", level);
                    plugin.saveConfig();
                    sender.sendMessage(plugin.getMessageConfig().get("fatiguelevel"));

                    Bukkit.getOnlinePlayers().forEach(p -> p.removePotionEffect(PotionEffectType.SLOW_DIGGING));
                } catch(NumberFormatException e) {
                    sender.sendMessage(plugin.getMessageConfig().get("notnumber"));
                }
                return false;
            }
        }

        sender.sendMessage(plugin.getMessageConfig().get("help"));

        return false;
    }
}
