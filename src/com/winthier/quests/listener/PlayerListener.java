package com.winthier.quests.listener;

import com.winthier.quests.QuestsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
        private final QuestsPlugin plugin;

        public PlayerListener(QuestsPlugin plugin) {
                this.plugin = plugin;
        }

        public void onEnable() {
                plugin.getServer().getPluginManager().registerEvents(this, plugin);
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                        plugin.playerManager.getPlayerData(player);
                }
        }

        public void onDisable() {
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onPlayerJoin(PlayerJoinEvent event) {
                plugin.playerManager.getPlayerData(event.getPlayer());
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onPlayerQuit(PlayerQuitEvent event) {
                // TODO: clear player data
        }
}
