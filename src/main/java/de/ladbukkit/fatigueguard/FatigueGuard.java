package de.ladbukkit.fatigueguard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * The main class of the fatigue guard plugin.
 * @author LADBukkit (Robin Eschbach)
 */
public class FatigueGuard extends JavaPlugin {

    /**
     * The message config for this plugin.
     */
    private MessageConfig messageConfig;

    /**
     * Creates the config and registers the commands and listeners.
     */
    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();

        try {
            this.messageConfig = new MessageConfig(new File(this.getDataFolder(), "messages.yml"), "/messages.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.reloadConfig();

        Bukkit.getPluginManager().registerEvents(new MoveListener(this), this);
        getCommand("fatigueguard").setExecutor(new FatigueGuardCommand(this));
    }

    /**
     * @return The message config of the plugin.
     */
    public MessageConfig getMessageConfig() {
        return messageConfig;
    }
}
