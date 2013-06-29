package com.winthier.quests.goal;

import com.garbagemule.MobArena.ArenaPlayer;
import com.garbagemule.MobArena.ArenaPlayerStatistics;
import com.garbagemule.MobArena.events.NewWaveEvent;
// import com.garbagemule.MobArena.events.ArenaPlayerDeathEvent;
// import com.garbagemule.MobArena.events.ArenaPlayerLeaveEvent;
import com.garbagemule.MobArena.framework.Arena;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scheduler.BukkitRunnable;

class MobArenaGoal extends QuotaGoal {
        private List<String> arenas;
        private int wave, kills;

        public MobArenaGoal() {}

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                arenas = config.getStringList("Arenas");
                wave = config.getInt("Wave", 0);
                kills = config.getInt("Kills", 0);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onNewWave(NewWaveEvent event) {
                for (Player player : event.getArena().getPlayersInArena()) {
                        if (!checkConstraints(player)) return;
                        feed(player, event.getArena(), event.getWaveNumber());
                }
        }

        // @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        // public void onArenaPlayerLeave(ArenaPlayerLeaveEvent event) {
        //         Player player = event.getPlayer();
        //         if (!checkConstraints(player)) return;
        //         feed(player, event.getArena());
        // }

        // @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        // public void onArenaPlayerDeath(ArenaPlayerDeathEvent event) {
        //         Player player = event.getPlayer();
        //         if (!checkConstraints(player)) return;
        //         feed(player, event.getArena());
        // }

        public boolean feed(final Player player, Arena arena, int wave) {
                if (this.wave > wave) return false;
                if (!arenas.isEmpty() && !arenas.contains(arena.arenaName())) return false;
                ArenaPlayer arenaPlayer = arena.getArenaPlayer(player);
                if (arenaPlayer == null) return false;
                ArenaPlayerStatistics stats = arenaPlayer.getStats();
                if (stats == null) return false;
                if (kills > stats.getInt("kills")) return false;
                return addScore(player, 1) > 0;
        }
}
