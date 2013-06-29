package com.winthier.quests.goal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Item;

/**
 * Catch an item with a fishing rod. This is different from hookin
 * entities.
 */
public class FishGoal extends ItemGoal {
        public FishGoal() {}

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onPlayerFish(PlayerFishEvent event) {
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
                if (!(event.getCaught() instanceof Item)) return;
                ItemStack item = ((Item)event.getCaught()).getItemStack();
                feed(player, item);
        }
}
