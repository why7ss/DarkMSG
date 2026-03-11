package me.civworld.darkMSG;

import me.civworld.darkMSG.settings.Settings;
import me.civworld.darkMSG.config.Config;
import me.civworld.darkMSG.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class DarkMSG extends JavaPlugin {
    private Config config = null;

    public void onEnable() {
        getLogger().info("DarkMSG is enabling...");
        Settings settings = new Settings();

        getLogger().info("Loading stats...");

        this.config = new Config(this, settings);
        this.config.loadConfig();

        getLogger().info("Loaded: " + settings.allTimePlayed + " all-time players.");

        getLogger().info("Sound enabled: " + settings.sound);

        getServer().getPluginManager().registerEvents(
                new PlayerListener(settings), this);
        getLogger().info("Registered event join and quit.");
    }

    public void onDisable() {
        if (this.config != null) {
            this.config.saveConfigData();
            getLogger().info("Saving stats... (" + this.config.settings.allTimePlayed + " all-time players)");
        }
    }
}