package com.winthier.quests.goal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandGoal extends StringGoal {
        public CommandGoal() {}

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                String msg = event.getMessage();
                feed(player, msg);
        }
}
