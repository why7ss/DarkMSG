package me.uuun.darkMSG.listener;

import me.uuun.darkMSG.settings.Settings;
import me.uuun.darkMSG.fancy.FancyText;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class    PlayerListener implements Listener {
    private final Settings settings;

    public PlayerListener(Settings settings) {
        this.settings = settings;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player pl = event.getPlayer();
        if(!pl.hasPlayedBefore()) settings.allTimePlayed++;
        event.joinMessage(null);
        String msg = pl.hasPlayedBefore() ?
                "<prefix><gray>[<green>+<gray>] <white>" +
                        FancyText.toFancy(pl.getName()) :
                "<prefix><gray>[<green>+<gray>] <white>" +
                        FancyText.toFancy(pl.getName()) +
                        " <yellow>" + FancyText.toFancy("bпepbыe")
                        + " <gray>(#" + this.settings.allTimePlayed + ")";
        Bukkit.broadcast(FancyText.parse(msg));
        if(settings.sound){
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player pl = event.getPlayer();
        event.quitMessage(null);
        String msg = "<prefix><gray>[<red>-<gray>] <white>" +
                FancyText.toFancy(pl.getName());
        if(settings.sound) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        }
        Bukkit.broadcast(FancyText.parse(msg));
    }
}