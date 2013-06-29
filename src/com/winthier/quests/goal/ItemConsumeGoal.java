package com.winthier.quests.goal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class ItemConsumeGoal extends ItemGoal {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onItemConsume(PlayerItemConsumeEvent event) {
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                if (matches(event.getItem())) addScore(player, 1);
        }
}
