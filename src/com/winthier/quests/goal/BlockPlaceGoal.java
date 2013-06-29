package com.winthier.quests.goal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceGoal extends ItemGoal {
        public BlockPlaceGoal() {}

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onBlockPlace(BlockPlaceEvent event) {
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                feed(player, event.getBlock());
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onBlockBreak(BlockBreakEvent event) {
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                if (!isCompleted(player) && matches(event.getBlock())) {
                        addScore(player, -1);
                }
        }
}
