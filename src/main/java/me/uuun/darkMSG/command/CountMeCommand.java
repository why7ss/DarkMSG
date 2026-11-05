package me.uuun.darkMSG.command;

import me.uuun.darkMSG.fancy.FancyText;
import me.uuun.darkMSG.order.JoinOrder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public final class CountMeCommand implements CommandExecutor {
    private final JoinOrder joinOrder;

    public CountMeCommand(JoinOrder joinOrder) {
        this.joinOrder = joinOrder;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только для игроков.");
            return true;
        }

        Map<UUID, Integer> snap = joinOrder.snapshot();
        Integer n = snap == null ? null : snap.get(player.getUniqueId());
        if (n != null) {
            sendResult(player, n);
            return true;
        }

        // данных ещё нет (плагин только загрузился) или игрок новый и его нет в снимке
        player.sendMessage(FancyText.parse("<prefix><gray>Подождите, данные подгружаются..."));
        joinOrder.requestNow(map -> {
            if (!player.isOnline()) return;
            Integer r = map.get(player.getUniqueId());
            if (r != null) sendResult(player, r);
            else player.sendMessage(FancyText.parse("<prefix><red>Не удалось определить ваш номер."));
        });
        return true;
    }

    private void sendResult(Player player, int n) {
        player.sendMessage(FancyText.parse("<prefix><white>Вы зашли: <yellow>№" + n + "<white>."));
    }
}