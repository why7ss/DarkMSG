package me.uuun.darkMSG.order;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public final class JoinOrder {
    private record Entry(UUID uuid, long firstPlayed) {}

    private final Plugin plugin;
    private final AtomicBoolean loading = new AtomicBoolean(false);
    private final Queue<Consumer<Map<UUID, Integer>>> waiters = new ConcurrentLinkedQueue<>();
    private volatile Map<UUID, Integer> order = null; // null = ещё ни разу не грузили

    public JoinOrder(Plugin plugin) {
        this.plugin = plugin;
    }

    /** Запускает периодическую асинхронную перезагрузку (каждую минуту). */
    public void start() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::reload, 20L * 60, 20L * 60);
    }

    /** Текущий снимок или null, если данные ещё не загружены. */
    public Map<UUID, Integer> snapshot() {
        return order;
    }

    /** Немедленная асинхронная загрузка; callback вызовется в main thread. */
    public void requestNow(Consumer<Map<UUID, Integer>> callback) {
        waiters.add(callback);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, this::reload);
    }

    private void reload() {
        if (!loading.compareAndSet(false, true)) return; // уже грузится, ждуны получат результат от текущей загрузки

        Map<UUID, Integer> result = null;
        try {
            OfflinePlayer[] all = Bukkit.getOfflinePlayers();
            List<Entry> entries = new ArrayList<>(all.length);
            for (OfflinePlayer p : all) {
                long first = p.getFirstPlayed(); // читаем один раз, а не в компараторе
                if (first > 0) entries.add(new Entry(p.getUniqueId(), first));
            }

            // старее = раньше; при равенстве по UUID, чтобы порядок был стабильным
            entries.sort(Comparator.comparingLong(Entry::firstPlayed).thenComparing(Entry::uuid));

            Map<UUID, Integer> map = new HashMap<>(entries.size() * 2);
            int i = 1;
            for (Entry e : entries) map.put(e.uuid(), i++);

            result = Collections.unmodifiableMap(map);
            this.order = result;
        } catch (Throwable t) {
            plugin.getLogger().warning("JoinOrder reload failed: " + t.getMessage());
        } finally {
            loading.set(false);
        }

        List<Consumer<Map<UUID, Integer>>> toNotify = new ArrayList<>();
        Consumer<Map<UUID, Integer>> w;
        while ((w = waiters.poll()) != null) toNotify.add(w);

        if (!toNotify.isEmpty() && plugin.isEnabled()) {
            Map<UUID, Integer> finalResult = result != null ? result : Map.of();
            Bukkit.getScheduler().runTask(plugin, () -> toNotify.forEach(c -> c.accept(finalResult)));
        }
    }
}