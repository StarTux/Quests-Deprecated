package com.winthier.quests.goal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class BedLeaveGoal extends QuotaGoal {
        public BedLeaveGoal() {}

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onBedLeave(PlayerBedLeaveEvent event) {
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                addScore(player, 1);
        }
}
