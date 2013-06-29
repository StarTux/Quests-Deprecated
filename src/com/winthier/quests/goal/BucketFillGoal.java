package com.winthier.quests.goal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBucketFillEvent;

/**
 * Fill a bucket. Matched is the resulting ItemStack.
 */
public class BucketFillGoal extends ItemGoal {
        public BucketFillGoal() {}

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onPlayerBucketFill(PlayerBucketFillEvent event) {
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                feed(player, event.getItemStack());
        }
}
