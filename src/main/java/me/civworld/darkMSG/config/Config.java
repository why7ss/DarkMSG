package me.civworld.darkMSG.config;

import me.civworld.darkMSG.settings.Settings;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public final class Config {
    private final Plugin plugin;
    public final Settings settings;

    public Config(Plugin plugin, Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    public void loadConfig() {
        this.plugin.saveDefaultConfig();

        FileConfiguration config = this.plugin.getConfig();
        if (config.contains("allplayers")) {
            this.settings.allTimePlayed = config.getInt("allplayers", 0);
        }
        if(config.contains("sound")){
            this.settings.sound = config.getBoolean("sound", false);
        }
    }

    public void saveConfigData() {
        FileConfiguration config = this.plugin.getConfig();
        config.set("allplayers", this.settings.allTimePlayed);
        this.plugin.saveConfig();
    }
}