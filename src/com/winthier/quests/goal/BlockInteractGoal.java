package com.winthier.quests.goal;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * This goal in which one progresses by interacting with certain block
 * types. A held item may be specified.
 */
class BlockInteractGoal extends ItemGoal {
        public BlockInteractGoal() {}

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onPlayerInteract(PlayerInteractEvent event) {
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
                feed(player, event.getClickedBlock());
        }
}
